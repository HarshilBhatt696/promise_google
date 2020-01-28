package my.promise.harshil.Util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.auth.AuthUI
import my.promise.harshil.*
import my.promise.harshil.Fragment.Request_dates
import my.promise.harshil.Model.*
import my.promise.harshil.RecyclerView.Item.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.core.utilities.Clock
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_request_dates.*
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

var FirstTime = false
object FireStoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val FireabaseUserName = FireabaseClass.mAuth.currentUser?.displayName
    private val currentUserDocRef: DocumentReference
        get() {
            if (FinalUsersName == "0000000") {
                val document: DocumentReference = firestoreInstance.document("users/${FireabaseUserName}")
                return document
            } else {
                val document: DocumentReference = firestoreInstance.document("users/${FinalUsersName}")
                return document
            }




        }

    private val chatChannelCollection = firestoreInstance.collection(References.chatChannels.toString())


    fun CheckExistance(CurrentCotext : Context) {
        if (FireabaseClass.mAuth.currentUser!!.displayName != null) {

            val UserName = FireabaseClass.mAuth.currentUser!!.displayName

            val UserExists = firestoreInstance.document("users/${UserName}")

            UserExists.get().addOnSuccessListener { dataSnapshot ->
                if (!dataSnapshot.exists()) {
                    val S = (Intent(CurrentCotext, MainActivity::class.java))
                    CurrentCotext.startActivity(S)
                } else {
                    val S = (Intent(CurrentCotext, MainPage::class.java))
                    CurrentCotext.startActivity(S)
                }
            }




        } else {
            val S = (Intent(CurrentCotext, MainActivity::class.java))
            CurrentCotext.startActivity(S)



        }
    }


    fun initCurrentUserIfFirstTime(CurrentCotext: Context ,onComplete: () -> Unit) {




        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
             if (!documentSnapshot.exists()) { // If does not exist


            val CreateNewUser =                                                                                                                             // Refer to data class for User inputs in this
                User(FinalUsersName, FireabaseClass.Gender, "", null, ArrayList<String>(), mutableListOf() , FirebaseAuth.getInstance().currentUser!!.uid, ArrayList<String>() ,ArrayList<String>() , ArrayList<String>() , ArrayList<String>()  , false  , mutableMapOf()  , ArrayList<String>() , 0 , "")


            currentUserDocRef.set(CreateNewUser).addOnSuccessListener {
                onComplete()
                FirstTime = true

                val UsersDisplayName = FireabaseClass.UserName
                val user = FirebaseAuth.getInstance().currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(UsersDisplayName).build()
                user?.updateProfile(profileUpdates)

                val S = (Intent(CurrentCotext, UserInfor::class.java))
                CurrentCotext.startActivity(S)

            }

              }
               else {
                 onComplete()
                 val S = (Intent(CurrentCotext, MainPage::class.java))
                 CurrentCotext.startActivity(S)

             }
           }
        }

    // Add the starting of the app you get a new girl every time , for the other user who is already using will get a notification of new matches found
    fun NewMatch(UserMatchedWith : String ) {
        getCurrentUser { user ->

            val userFieldMap = mutableMapOf<String, Any>()
            val UpdatedDates = user.numberOfPeopleDating
            UpdatedDates.add(UserMatchedWith)
            userFieldMap["numberOfPeopleDating"] = UpdatedDates
            currentUserDocRef.update(userFieldMap)


            val otherUser: DocumentReference = firestoreInstance.document("users/${UserMatchedWith}")
            val userFieldMapOtherUser = mutableMapOf<String, Any>()




            var ExitTime = false
            firestoreInstance.collection("users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                        return@addSnapshotListener
                    }


                    querySnapshot!!.documents.forEach {


                        if (it.id == UserMatchedWith) {

                            val OtherUserInfo = it.toObject(User::class.java)!!
                            val Number = OtherUserInfo.numberOfPeopleDating
                            Number.add(user.name)
                            userFieldMapOtherUser["numberOfPeopleDating"] = Number
                            otherUser.update(userFieldMapOtherUser)
                            ExitTime = true





                        }


                    }

                }


        }


    }





    fun MatchMakeSameGender(Bool : Boolean) {
        getCurrentUser { user ->
            val userFieldMap = mutableMapOf<String, Any>()
            userFieldMap["matchMakeSameGender"] = Bool
            currentUserDocRef.update(userFieldMap)

        }
    }




    fun getOrCreateChatChannel(otherUserId: String,
                               onComplete: (channelId: String) -> Unit) {


        currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)

                    return@addOnSuccessListener
                }


                    val currentUserId = FireabaseClass.UserName

                    val newChannel = chatChannelCollection.document()
                    newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                    currentUserDocRef
                        .collection("engagedChatChannels")
                        .document(otherUserId)
                        .set(mapOf("channelId" to newChannel.id))

                    firestoreInstance.collection("users").document(otherUserId)
                        .collection("engagedChatChannels")
                        .document(currentUserId)
                        .set(mapOf("channelId" to newChannel.id))

                    onComplete(newChannel.id)

            }

    }

    fun addChatMessagesListener(channelId: String, context: Context,
                                onListen: (List<Item>) -> Unit): ListenerRegistration {
        return chatChannelCollection.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {


                    if (it["type"] == MessageType.TEXT) {
                        items.add(TestMessageItem(it.toObject(TextMessage::class.java)!!, context))
                    }
                    else {
                        items.add(ImageMessageItem(it.toObject(ImageMessage::class.java)!!, context))
                    }



                    return@forEach
                }
                onListen(items)
            }
    }










    fun UpdateUser( bio : String = "" , profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (profilePicturePath != null)
            userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)

    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {

                        onComplete(it.toObject(User::class.java)!!)

            }


        return


    }



    fun UpdateInterest(Arrays : ArrayList<String>) {
        val userFieldMap = mutableMapOf<String, Any>()
        userFieldMap["interest"] = Arrays
        currentUserDocRef.update(userFieldMap)
    }


    // send Requests
    fun UpdateOtherUsersInFO(OtherUserId : String , UserId : String, OtherUserIdRequests : ArrayList<String>  , DeclineBlocked : ArrayList<String> , user : User) {
        val document: DocumentReference = firestoreInstance.document("users/${OtherUserId}")
        val userFieldMap = mutableMapOf<String, Any>()
        val Requests = OtherUserIdRequests

        if (!Requests.contains(UserId)) {
            Requests.add(UserId)
        }

        val NoMore = user.notToBeViwedAnywhere

        if (NoMore.contains(OtherUserId)) {
            NoMore.remove(OtherUserId)
        }











        userFieldMap["requests"] = Requests




            val MyUserTable = mutableMapOf<String, Any>()



        if (!DeclineBlocked.contains(OtherUserId)) {
            DeclineBlocked.add(OtherUserId)
        }

        MyUserTable["declineOrBlocked"] = DeclineBlocked
            currentUserDocRef.update(MyUserTable)
        MyUserTable[References.notToBeViwedAnywhere.toString()]  = NoMore


           document.update(userFieldMap)


    }


    fun AcceptIt(OtherUserId: String , OtherChatTalkingToRequests : ArrayList<String> , myUser : String , MyChatRequests : ArrayList<String> , numberofDating : ArrayList<String>) {

        val document: DocumentReference = firestoreInstance.document("users/${OtherUserId}")
        val userFieldMap = mutableMapOf<String, Any>()


        val Requests = OtherChatTalkingToRequests
        if (IfConsist(Requests , myUser)) {
            Requests.add(myUser)
        }
        userFieldMap["numberOfPeopleDating"] = Requests

        document.update(userFieldMap)


        // Update my reqquests
        val MyUserField = mutableMapOf<String, Any>()
        val datingAlready = numberofDating

        if (!IfConsist(datingAlready , OtherUserId)) {
            datingAlready.add(OtherUserId)
        }
        MyUserField["numberOfPeopleDating"] = datingAlready



        // Removes Person from requests
        val RequestReceived = MyChatRequests
        RequestReceived.remove(OtherUserId)
        MyUserField["requests"] = RequestReceived

        currentUserDocRef.update(MyUserField)



    }

    // Accept the request that was sent to you by other users to start talking , dating and chating .
//

    fun DeclineRequest(OtherUserId: String , MyUserId : String, NotViewPeople : ArrayList<String> , ChatTalkingToRequests : ArrayList<String> , OtherUsersIdRequests : ArrayList<String>) {

        val MyUserField = mutableMapOf<String, Any>() // Used for both the cases if and else



        // Remove person from being viewed anymore
        val otherUser: DocumentReference = firestoreInstance.document("users/${OtherUserId}")
        otherUser.get()
        val UserField = mutableMapOf<String, Any>()

        if (!IfConsist(NotViewPeople , MyUserId)) {
            NotViewPeople.add(MyUserId)
        }
        UserField[References.declineOrBlocked.toString()] = NotViewPeople


        otherUser.update(UserField)

        // Remove Requests Once Sent




        // #2 Update My User too
        val MyChat = ChatTalkingToRequests



        if (!IfConsist(MyChat , OtherUserId) ) {
            MyChat.add(OtherUserId)
        }
        MyUserField[References.declineOrBlocked.toString()] = MyChat


        // Removes Person from interests
        val RequestReceived = OtherUsersIdRequests
        RequestReceived.remove(OtherUserId)
        MyUserField[References.requests.toString()] = RequestReceived
        currentUserDocRef.update(MyUserField)










    }


    fun Block(OtherUserId: String, MyUserId : String, NotViewPeopleArray: ArrayList<String>, MyDeclineBlocked : ArrayList<String>, NumberOfPeopleDating : ArrayList<String> , NoOfPeopleOtherUserDating : ArrayList<String> , cont : Context , ContTo : Class<Messaging>) {

        val MyUserField = mutableMapOf<String, Any>() // Used for both the cases if and else


        val otherUser: DocumentReference = firestoreInstance.document("users/${OtherUserId}")


        val UserField = mutableMapOf<String, Any>()
        val OtherUserChat = NotViewPeopleArray

        if(IfConsist(OtherUserChat , MyUserId)) {
            OtherUserChat.add(MyUserId)
        }
       val NoPeepsDating =  NoOfPeopleOtherUserDating

        if (IfConsist(NoPeepsDating ,MyUserId)) {
            NoPeepsDating.remove(MyUserId)
        }
        UserField["numberOfPeopleDating"] = NoPeepsDating
        UserField[References.declineOrBlocked.toString()] = OtherUserChat



        otherUser.update(UserField)

        // Remove Requests Once Sent




        // #2 Add to blocked users
        val DeclineArray = MyDeclineBlocked

        if (!IfConsist(DeclineArray , OtherUserId)) {
            DeclineArray.add(OtherUserId)
        }
        MyUserField[References.declineOrBlocked.toString()] = DeclineArray



        // Remove from chat activity
        val Num = NumberOfPeopleDating

        if (IfConsist(Num , OtherUserId)) {
            Num.remove(OtherUserId)
        }
        MyUserField["numberOfPeopleDating"] = Num


        currentUserDocRef.update(MyUserField)













    }



    // Send Image to the other user in message
    fun sendMessage(message: Mesage, channelId: String) {
        chatChannelCollection.document(channelId)
            .collection("messages")
            .add(message)
    }



    fun UserInfoUpload(Items : MutableMap<String , Any>) {
        currentUserDocRef.update(Items)
    }


    fun addRequestListener(user : User , Recycler : RecyclerView , TextItem: TextView ,
        RequestedUsers:  ArrayList<String>,
        context: Request_dates) {


        var RequestsUsers = ArrayList<User>()
            val NumberOfUsers = ArrayList<String>()

             firestoreInstance.collection("users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<com.xwray.groupie.kotlinandroidextensions.Item>()


                    querySnapshot!!.documents.forEach {


                        if (it.id != FirebaseAuth.getInstance().currentUser?.displayName && RequestedUsers.contains(it.id)) {


                            try {
                                //items.add(RequestClass(it.toObject(User::class.java)!!, it.id, context.context!!))

                                RequestsUsers.add(it.toObject(User::class.java)!!)

                                // From here
                                Recycler.adapter = RequestAdaptor(context.context!!, RequestsUsers , user)

                                NumberOfUsers.add("Hi")
                            } catch (throwable : Exception ) {}

                        }


                    }

                    if (NumberOfUsers.isEmpty()) {
                       TextItem.visibility = View.VISIBLE
                    }
                }

        if (context.context == null) {}
        else {
            Recycler.adapter = RequestAdaptor(context.context!!, RequestsUsers , user)
            Recycler.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            Recycler.addItemDecoration(GridItemDecoration(30, 2))
        }


    }


    var CollectionOfLastUsersMess = arrayListOf<String>()
    fun GetDateChats(MyUser : User  , TextItem : TextView  , UsersNeeded : ArrayList<String>  , context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {


        var NumberOfUsers = ArrayList<String>()
        return firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<com.xwray.groupie.kotlinandroidextensions.Item>()
                querySnapshot!!.documents.forEach {


                    if (it.id != FirebaseAuth.getInstance().currentUser?.displayName && UsersNeeded.contains(it.id)) {




                            items.add(PersonClass(it.toObject(User::class.java)!!, it.id, context))
                            CollectionOfLastUsersMess.add(it.id)
                            NumberOfUsers.add("Hi")


                    }


                }

                if (NumberOfUsers.isEmpty()) {
                    TextItem.text = "You Have no new Messages "
                }
                onListen(items)
            }

    }

    fun addMatchMakers(context: Context, UsersNeeded : ArrayList<String>, onListen: (List<com.xwray.groupie.kotlinandroidextensions.Item>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<com.xwray.groupie.kotlinandroidextensions.Item>()
                querySnapshot!!.documents.forEach {
                   if (it.id != FireabaseClass.UserName && UsersNeeded.contains(it.id)) {


                        items.add(MatchMakinProfile(it.toObject(User::class.java)!!, it.id, context))


                  }


                }

                onListen(items)
            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    // send Messages
    fun sendMessage(message: TextMessage, channelId: String) {
        chatChannelCollection.document(channelId)
            .collection("messages")
            .add(message)
    }




    // FCM Tokens registered here
    fun getFCMRegistrationTokens(onComplete: (tokens: MutableList<String>) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)!!
            onComplete(user.registrationTokens)
        }
    }

    fun setFCMRegistrationTokens(registrationTokens: MutableList<String>) {
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }

//    fun AddToDatingSystem(name : String) { // How many he is talking to
//        val userFieldMap = mutableMapOf<String, Any>()
//
//        var DatingAlready = ArrayList<String>()
//        getCurrentUser { user ->
//
//            DatingAlready = user.numberOfPeopleDating
//
//        }
//        DatingAlready.add(name)
//        userFieldMap["dating"] = name
//        currentUserDocRef.update(userFieldMap)
//    }


    fun IfConsist(ArrayContains : ArrayList<String> , UserName : String) : Boolean {

        if (ArrayContains.contains(UserName)) {
            return true
        } else {
            return false
        }


    }

    // Add New Message Received
    fun AddMessage(User : User , OtherUserId: String) {

        val otherUser: DocumentReference = firestoreInstance.document("users/${OtherUserId}")
        val MyUserField = mutableMapOf<String, Any>() // Used for both the cases if and else



        if (Messages.containsKey(User.name)) {
            GetUserNumberNotSeen = Messages[User.name]!!
        }
        Messages[User.name] = GetUserNumberNotSeen + 1

        MyUserField[References.messages.toString()] = Messages

        otherUser.update(MyUserField)

    }

    fun RemoveUser(otherUserId: String, Messages: MutableMap<String, Int>) {

        val MyUserField = mutableMapOf<String, Any>() // Used for both the cases if and else


        Messages[otherUserId] = 0

        MyUserField[References.messages.toString()] = Messages
        currentUserDocRef.update(MyUserField)


    }



    // TodaysMatchMake

    fun AddToTodaysUsers(TodaysUsers : ArrayList<User>) {

        val UserNames = ArrayList<String>()

        for (n in  TodaysUsers) {
            UserNames.add(n.name)
        }
        val MyUserField = mutableMapOf<String, Any>()

        MyUserField[References.todaysMatchMakes.toString()] = UserNames

        currentUserDocRef.update(MyUserField)

    }

    fun SentRequestToOldUsers(user : User) {
        val MyUserField = mutableMapOf<String, Any>()

        val CurrentRequestStreak = user.sentRequestsForToday

        MyUserField[References.sentRequestsForToday.toString()] = CurrentRequestStreak + 1
        currentUserDocRef.update(MyUserField)
    }

    fun RemoveTodaysUsers(user : User) {


        val MyUserField = mutableMapOf<String, Any>()

        var AddToNotToNotSeen = ArrayList<String>()
        var CurrentNotSeen = ArrayList<String>()



            AddToNotToNotSeen = user.todaysMatchMakes
            CurrentNotSeen = user.notToBeViwedAnywhere

        TodaysMatchers = ArrayList<String>()
        for (n in AddToNotToNotSeen) {
            TodaysMatchers.add(n)
        }

            for (n in AddToNotToNotSeen) {

                if (!CurrentNotSeen.contains(n)) {
                    CurrentNotSeen.add(n)
                }

            }
            MyUserField[References.todaysMatchMakes.toString()] = ArrayList<String>()
            MyUserField[References.notToBeViwedAnywhere.toString()] = CurrentNotSeen


            MyUserField[References.sentRequestsForToday.toString()] = 0

        NotToBeSeens = CurrentNotSeen

         //   TempUser.todaysMatchMakes = ArrayList<String>()

            currentUserDocRef.update(MyUserField)


    }

    fun CheckTrulyLogged(context: Context) {

        try {

            getCurrentUser { user ->
                if (user.interest.isEmpty()) {
                    FinalUsersName = FirebaseAuth.getInstance().currentUser!!.displayName!!
                    context.startActivity(Intent(context, Interest::class.java))
                } else if (user.profilePicturePath == null) {
                    context.startActivity(Intent(context, SetImage::class.java))
                    Toast.makeText(context , "Login Error" , Toast.LENGTH_SHORT).show()
                }

            }

        } catch (throwable : Exception) {
            AuthUI.getInstance()
                .signOut(context)
            context.startActivity(Intent(context, MainActivity::class.java))
            Toast.makeText(context , "Login Error" , Toast.LENGTH_SHORT).show()
        }

    }

    fun GetTimeMatch() : String {

        var Time = ""
        if(Build.VERSION.SDK_INT >= 26) {
            //only api 21 above
            Time = "${ LocalDate.now().dayOfMonth}/${ LocalDate.now().month}/${ LocalDate.now().year}"

        }else{
            //only api 21 down


            val current = Calendar.getInstance().time
            val Item =  "${ current.date}"
            Time = Item

        }







        return Time

    }

    // If Today match making was clicked. I get the same users the whole day and it saves todays date in the Firestore . Once the day changes todays date also changes . This way It will check
    // If the day is same as the previus stored .if not change it to the new date and remove todaysmatchmaked

    fun UploadTime(user : User ) : Boolean {

        var MatchMakingNewAllowed = false
        val MyUserField = mutableMapOf<String, Any>()


            val CurrentTime = GetTimeMatch()
            if (CurrentTime == user.timeSentRequests) {
                MatchMakingNewAllowed = false

            }   else {
                MatchMakingNewAllowed = true

                RemoveTodaysUsers(user)
                MyUserField[References.timeSentRequests.toString()] = CurrentTime
                RequestSent=0
                currentUserDocRef.update(MyUserField)
            }


        return MatchMakingNewAllowed


    }


    // Check if any messages are there so that the user is directed the users tab


    fun CheckUsersTab() {

        getCurrentUser { user ->



        }


    }

    val CollectedUsername = ArrayList<String>()
    fun CheckifUsersExists() {
        //val CollectedUsername = ArrayList<String>()

        firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }


                querySnapshot!!.documents.forEach {
                    val Users = it.toObject(User::class.java)!!
                    CollectedUsername.add(Users.name)

                }


            }


    }









}

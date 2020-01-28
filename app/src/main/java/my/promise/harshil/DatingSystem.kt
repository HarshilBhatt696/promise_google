package my.promise.harshil

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import my.promise.harshil.Fragment.match_makin
import my.promise.harshil.Model.User
import my.promise.harshil.RecyclerView.Item.MatchMakinProfile

import my.promise.harshil.Util.FireStoreUtil
import my.promise.harshil.Util.FirstTime
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

var DstingClass = DatingSystem()
var NotToBeSeens = ArrayList<String>()
var FinalizedItems = ArrayList<my.promise.harshil.Model.User>()

var TodaysMatchers = ArrayList<String>()


class DatingSystem() {


    private val Gender = FireabaseClass.Gender// Lets say its Male
    private var OppositeGender = "Females"


    var SameGenderTodaysMatchers = ArrayList<String>() // if the same gender exists or not


    var LoversCollected = arrayListOf<String>()
    var SameGendersCollected = arrayListOf<String>()

    // Final list collected from same gender
    var SameGCollected = ArrayList<String>()

    var FinalList = ArrayList<String>() //FINAL List for dates

    private fun CheckOppositeGender() {
        if (Gender == References.Males.toString()) {
            OppositeGender = References.Females.toString()
        } else {
            OppositeGender = References.Males.toString()

        }
    }


    // When I click Matchmake the result will be from the function below



    // Assign the number of people wanted


    fun GetUsers(Lottie : LottieAnimationView, viewPagers: ViewPager, button: Button , user : User  , fm: FragmentManager , context: Context) {

        Lottie.playAnimation()
        FinalizedItems = ArrayList<User>()
        val NumberOfPeopleDating = user.numberOfPeopleDating
        val BlockedUsers = user.declineOrBlocked
        val NotToBeSeen = user.notToBeViwedAnywhere
        val SameGenderBool = user.matchMakeSameGender
        val userRequest = user.requests


        FirebaseFirestore.getInstance().collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<com.xwray.groupie.kotlinandroidextensions.Item>()
                querySnapshot!!.documents.forEach {
                    if (it.id != FireabaseClass.UserName && NotToBeSeen.contains(it.id) && !NumberOfPeopleDating.contains(it.id) && !BlockedUsers.contains(it.id ) && !userRequest.contains(it.id )) {


                        items.add(MatchMakinProfile(it.toObject(User::class.java)!!, it.id, context))
                        FinalizedItems.add(it.toObject(User::class.java)!!)


                    }


                }
                val Extra = ArrayList<String>()
                val CheckFinalItems = ArrayList<User>()
                for (n in FinalizedItems) {
                    if (!Extra.contains(n.name)) {
                        Extra.add(n.name)
                        CheckFinalItems.add(n)
                    }
                }



                    Lottie.pauseAnimation()
                    Lottie.visibility = View.GONE
                    viewPagers.visibility = View.VISIBLE
                    viewPagers.adapter = CardStacjAdapter(fm , context, CheckFinalItems, viewPagers  , user)
                    viewPagers.offscreenPageLimit = CheckFinalItems.size - ( CheckFinalItems.size / 2).toInt()
                    viewPagers.setPageTransformer(true , match_makin.CardStackTransformer())


                    button.visibility = View.GONE








            }
    }
    var GotValues = false
    fun GetNumberOfPeopleMatched(user : User , button : Button ,  fm : FragmentManager, viewPagers : ViewPager ,DateWanted : Int, Lottie : LottieAnimationView?, context : Context,  onListen: (List<com.xwray.groupie.kotlinandroidextensions.Item> ) -> Unit): ArrayList<String> {
        CheckOppositeGender()
        GotValues = true
        // Reload

        LoversCollected = ArrayList<String>()

        val Db = FireabaseClass.myRef.child(References.Likes.toString())
        button.visibility = View.VISIBLE
        Db.addValueEventListener(object : ValueEventListener {



            override fun onDataChange(p0: DataSnapshot) {

               if (GotValues) {

                   var NumberOfPeopleDating = ArrayList<String>()




                   NumberOfPeopleDating = user.numberOfPeopleDating
                   val BlockedUsers = user.declineOrBlocked
                   val NotToBeSeen = user.notToBeViwedAnywhere
                   val SameGenderBool = user.matchMakeSameGender
                   val userRequest = user.requests
                   val YesterdaysUser = user.todaysMatchMakes

                   val InterestRecieved = user.interest


                   val TodaysMatchersCheckerColl = ArrayList<String>()
                   for (n in Interests.Category.indices) {

                       val MatchMakes = p0.child(Interests.Category[n]).child(InterestRecieved[n])
                           .child(OppositeGender).children


                       for (Lovers in MatchMakes) {

                           if (!TodaysMatchers.contains(Lovers.key.toString()) && !NumberOfPeopleDating.contains(Lovers.key.toString()) && !BlockedUsers.contains(
                                   Lovers.key.toString()
                               ) && !NotToBeSeen.contains(Lovers.key.toString()) && !userRequest.contains(Lovers.key.toString()) && Lovers.key != FireabaseClass.UserName && !YesterdaysUser.contains(
                                   Lovers.key.toString()
                               )
                           ) {


                               LoversCollected.add(0, Lovers.key.toString())

                           }

                           if (!NotToBeSeens.contains(Lovers.key.toString()) && !NumberOfPeopleDating.contains(Lovers.key.toString()) && !BlockedUsers.contains(
                                   Lovers.key.toString()
                               ) && !NotToBeSeen.contains(Lovers.key.toString()) && !userRequest.contains(Lovers.key.toString()) && Lovers.key != FireabaseClass.UserName
                           ) {


                               TodaysMatchersCheckerColl.add(0, Lovers.key.toString())

                           }


                       }


//                        val MatchMakeSameGender = p0.child(Interests.Category[n]).child(Interests.CategoryGatheredTypes[n])
//                            .child(Gender).children


                       // Same Gender Collection



                   }

                   SameGendersCollected = ArrayList()
                   SameGenderTodaysMatchers = ArrayList<String>()
                   if (SameGenderBool) {

                   for (n in Interests.Category.indices) {



                           val MatchMakeSameGender = p0.child(Interests.Category[n]).child(InterestRecieved[n])
                               .child(Gender).children
                           for (SameGender in MatchMakeSameGender) {


                               if (!NumberOfPeopleDating.contains(SameGender.key.toString()) && !BlockedUsers.contains(
                                       SameGender.key.toString()
                                   ) && !NotToBeSeen.contains(SameGender.key.toString()) && !userRequest.contains(
                                       SameGender.key.toString()
                                   ) && SameGender.key != FireabaseClass.UserName
                               ) {

                                   SameGendersCollected.add(0, SameGender.key.toString())

                               }


                               if (!NotToBeSeens.contains(SameGender.key.toString()) && !NumberOfPeopleDating.contains(
                                       SameGender.key.toString()
                                   ) && !BlockedUsers.contains(
                                       SameGender.key.toString()
                                   ) && !NotToBeSeen.contains(SameGender.key.toString()) && !userRequest.contains(
                                       SameGender.key.toString()
                                   ) && SameGender.key != FireabaseClass.UserName
                               ) {


                                   SameGenderTodaysMatchers.add(0, SameGender.key.toString())

                               }


                           }
                       }
                   }




                   // Gets the best Matches


                   FinalizedItems = ArrayList<my.promise.harshil.Model.User>()


                   // If the user Match Maked for today
                   var AllowedForNewMatchMake = false


                   if (FireStoreUtil.UploadTime(user) == false) {


                       var CheckList = false // Check if users does not consist of same gender only
                       for (n in user.todaysMatchMakes) {
                           if (TodaysMatchersCheckerColl.contains(n)) {
                               FinalList.add(n)
                               CheckList = true
                           }
                       }

                       for (n in user.todaysMatchMakes) {
                           if (SameGenderTodaysMatchers.contains(n)) {
                               FinalList.add(n)
                           }
                       }





                       if (CheckList == false ) { // checks if the users aint empty
                           AllowedForNewMatchMake = true
                           FinalList = GetMultiples(DateWanted, SameGenderBool)

                       }


                   } else {


                       AllowedForNewMatchMake = true
//                        FireStoreUtil.RemoveTodaysUsers(user)
                       FinalList = GetMultiples(DateWanted, SameGenderBool)


                   }





                   FirebaseFirestore.getInstance().collection("users")
                       .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                           if (firebaseFirestoreException != null) {
                               Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                               return@addSnapshotListener
                           }

                           val items = mutableListOf<com.xwray.groupie.kotlinandroidextensions.Item>()
                           querySnapshot!!.documents.forEach {

                               if (AllowedForNewMatchMake) {
                                   if (!TodaysMatchers.contains(it.id) && !YesterdaysUser.contains(it.id) && !NotToBeSeen.contains(
                                           it.id
                                       ) && it.id != FireabaseClass.UserName && FinalList.contains(it.id) && !NumberOfPeopleDating.contains(
                                           it.id
                                       ) && !BlockedUsers.contains(it.id) && !NotToBeSeen.contains(it.id) && !userRequest.contains(
                                           it.id
                                       )
                                   ) {


                                       items.add(MatchMakinProfile(it.toObject(User::class.java)!!, it.id, context))
                                       FinalizedItems.add(it.toObject(User::class.java)!!)

                                       onListen(items)
                                   }
                               } else {
                                   if (!NotToBeSeens.contains(it.id) && it.id != FireabaseClass.UserName && FinalList.contains(
                                           it.id
                                       ) && !NumberOfPeopleDating.contains(
                                           it.id
                                       ) && !BlockedUsers.contains(it.id) && !NotToBeSeen.contains(it.id) && !userRequest.contains(
                                           it.id
                                       )
                                   ) {


                                       items.add(MatchMakinProfile(it.toObject(User::class.java)!!, it.id, context))
                                       FinalizedItems.add(it.toObject(User::class.java)!!)

                                       onListen(items)
                                   }
                               }


                           }

                           // Avoid Duplicates
                           val Extra = ArrayList<String>()
                           val CheckFinalItems = ArrayList<User>()
                           for (n in FinalizedItems) {
                               if (!Extra.contains(n.name)) {
                                   Extra.add(n.name)
                                   CheckFinalItems.add(n)
                               }
                           }
                           FinalizedItems = CheckFinalItems


                           if (Lottie != null) {
                               Lottie.pauseAnimation()
                               Lottie.visibility = View.GONE
                               viewPagers.visibility = View.VISIBLE
                               viewPagers.adapter = CardStacjAdapter(fm, context, CheckFinalItems, viewPagers, user)
                               viewPagers.offscreenPageLimit = CheckFinalItems.size - 1
                               viewPagers.setPageTransformer(true, match_makin.CardStackTransformer())


                               //button.visibility = View.GONE


                               button.visibility = View.GONE

                               TodaysMatchers = ArrayList<String>()
                               for (n in CheckFinalItems) {
                                   TodaysMatchers.add(n.name)
                               }

                           }

                           if (AllowedForNewMatchMake) {


                               FireStoreUtil.AddToTodaysUsers(CheckFinalItems)
                               AllowedForNewMatchMake = false

                           }


                       }


               }
                GotValues = false

            }



            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }



        })

        return  FinalList



    } // Lovers Wanted

    // Algorithm
    private fun GetMultiples(PeopleWanted : Int  , SameGenderMatchMake : Boolean) : ArrayList<String> {

        val Dict = mutableMapOf<String, Int>()





        for (n in LoversCollected.indices) {

            if (Dict.contains(LoversCollected[n])) {
                Dict[LoversCollected[n]] = 1 + Dict[LoversCollected[n]]!!

            } else {

                Dict[LoversCollected[n]] = 1
            }


        }
        //{Shela=1, Stella=3, Kaira=1, Jessie=4}
        //

        val People = arrayListOf<String>()
        val Rank = arrayListOf<Int>() // [2,8,4,1,4]

        for (n in Dict) {
            People.add(n.component1())
            Rank.add(n.component2())
        }



        for (n in Rank.indices) {



            for (S in Rank.indices)

                if (Rank[n] >= Rank[S]) {
                    val Num = Rank[n]
                    Rank[n] = Rank[S]
                    Rank[S] = Num

                    val Same = People[n]
                    People[n] = People[S]
                    People[S] = Same
                }
        }


        val DateGoingFor = arrayListOf<String>() // Final Usernames going in
        if (People.size > PeopleWanted) {
            for (n in 0..PeopleWanted - 1) {
                DateGoingFor.add(People[n])

            }
        } else {
            for (n in People) {
                DateGoingFor.add(n)

            }
        }


        if (SameGenderMatchMake) {
            SameGCollected = MatchMakingCollected(2)
        }


        for (n in SameGCollected) {
            DateGoingFor.add(n)
        }





        return DateGoingFor

    }




// Same gender
    private fun MatchMakingCollected(PeopleWanted: Int) : ArrayList<String> {
        val Dict = mutableMapOf<String, Int>()

        for (n in SameGendersCollected.indices) {

            if (Dict.contains(SameGendersCollected[n])) {
                Dict[SameGendersCollected[n]] = 1 + Dict[SameGendersCollected[n]]!!

            } else {

                Dict[SameGendersCollected[n]] = 1
            }


        }
        //{Shela=1, Stella=3, Kaira=1, Jessie=4}
        //

        val People = arrayListOf<String>()
        val Rank = arrayListOf<Int>() // [2,8,4,1,4]

        for (n in Dict) {
            People.add(n.component1())
            Rank.add(n.component2())
        }



        for (n in Rank.indices) {



            for (S in Rank.indices)

                if (Rank[n] >= Rank[S]) {
                    val Num = Rank[n]
                    Rank[n] = Rank[S]
                    Rank[S] = Num

                    val Same = People[n]
                    People[n] = People[S]
                    People[S] = Same
                }
        }

        val DateGoingFor = arrayListOf<String>() // Final Usernames going in
        if (People.size >= PeopleWanted) {
            for (n in 0..PeopleWanted) {
                DateGoingFor.add(People[n])

            }
        } else {
            for (n in People) {
                DateGoingFor.add(n)

            }
        }






        return DateGoingFor



    }


    // Only for first time user
    var FirstTimeUserOnly = ArrayList<String>()
    fun FirstTimeDate(context : Context ,user : User): ArrayList<String> {
        CheckOppositeGender()
        // Reload



        val Db = FireabaseClass.myRef.child(References.Likes.toString())

        Db.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                var NumberOfPeopleDating = ArrayList<String>()



                val InterestRecieved = user.interest

                for (n in Interests.Category.indices) {

                    val MatchMakes = p0.child(Interests.Category[n]).child(InterestRecieved[n])
                        .child(OppositeGender).children


                    for (Lovers in MatchMakes) {



                            LoversCollected.add(0, Lovers.key.toString())





                    }


//                        val MatchMakeSameGender = p0.child(Interests.Category[n]).child(Interests.CategoryGatheredTypes[n])
//                            .child(Gender).children


                    // Same Gender Collection





                }


                // Gets the best Matches







                FirstTimeUserOnly = GetMultiples(1 ,false )
                FirebaseFirestore.getInstance().collection("users")
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        if (firebaseFirestoreException != null) {
                            Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                            return@addSnapshotListener
                        }


                        querySnapshot!!.documents.forEach {
                            if (it.id != FireabaseClass.UserName && FirstTimeUserOnly.contains(it.id)) {

                                FireStoreUtil.NewMatch(it.id )


                            }


                        }


                    }


            }



            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })

        return  FinalList



    }







}



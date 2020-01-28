package my.promise.harshil

import android.R
import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.block_dialog.view.*
import kotlinx.android.synthetic.main.names_taken.view.*
import kotlinx.android.synthetic.main.provide_messagee.view.*
import kotlinx.android.synthetic.main.request_bottom_navigation.view.*
import kotlinx.android.synthetic.main.small_messages.view.*
import kotlinx.android.synthetic.main.specify_gender.*
import kotlinx.android.synthetic.main.specify_gender.view.*
import kotlinx.android.synthetic.main.splash_activity.*
import kotlinx.android.synthetic.main.term_condition.view.*
import my.promise.harshil.Model.User
import my.promise.harshil.Service.MyFirebaseInstanceIDService
import my.promise.harshil.Util.FireStoreUtil
import org.w3c.dom.Text
import java.util.zip.Inflater


var FinalUsersName : String ="0000000"
var UserNameEntereed = ""
class MainActivity : AppCompatActivity() {



    fun GenderSelectorMessage(Cont : Context , UsernameGiven : String) {
        val Contenty = Dialog(Cont)
        val Dialog = LayoutInflater.from(Cont).inflate(my.promise.harshil.R.layout.specify_gender, null)

        Contenty.setContentView(Dialog)


        Dialog.Female.setOnClickListener {

            FireabaseClass.Gender = References.Females.toString()




            FireabaseClass.UserName = UsernameGiven
            FinalUsersName = UsernameGiven

            AssignUserName()




        }


        Dialog.Male.setOnClickListener {

            FireabaseClass.Gender = References.Males.toString()

            FireabaseClass.UserName = UsernameGiven
            FinalUsersName = UsernameGiven

            AssignUserName()


        }



        Contenty.show()
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(my.promise.harshil.R.layout.activity_main)
        val Code = References.Codes.toString()

        FirebaseApp.initializeApp(this) // Important


        val Database = FirebaseDatabaseClass()

        MyUserAssigned = false
        FireStoreUtil.CheckifUsersExists()
        Database.myRef.child(References.Codes.toString())

        val Context = this
        EnterToLogin.setOnClickListener {



            Toast.makeText(this , "Loading...." , Toast.LENGTH_SHORT).show()

            val UsernameGiven = TextUserName.text.toString()

            val CodeGiven = TextCode.text.toString()

            val Boy = "Boys"
            val Girls = "Girls"

            if (FinalUsersName != "0000000") {
                val editText: EditText = findViewById(my.promise.harshil.R.id.TextUserName)
                editText.setText(FinalUsersName)
            }


            Database.myRef.child(Code).addListenerForSingleValueEvent(object : ValueEventListener {



                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again





                        if (dataSnapshot.child(Boy).value == CodeGiven || dataSnapshot.child(Girls).value == CodeGiven) {



                            // Assign Username


                            if (FireStoreUtil.CollectedUsername.contains(UsernameGiven) && FinalUsersName != UsernameGiven && UsernameGiven.isNotEmpty()) {
                                ProvideWarnings().SmallDialog("Please Enter another username" , "Username Already taken" ,Context  , null )
                            } else {

                                GenderSelectorMessage(Context ,UsernameGiven )
//                                FireabaseClass.UserName = UsernameGiven
//                                FinalUsersName = UsernameGiven
//                                Callnow()
//                                AssignUserName()
                            }




                        } else {
                            ProvideWarnings().SmallDialog("Please Enter a valid code. Do Check the Insta Page to get the latest code" , "Invalid Code" ,Context  , null )
                        }




                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value

                }

            })


        }







    }




    fun Callnow() {
       // val Gender =  CheckGender(TextCode.text.toString())
     //   RegisterDetails(Gender,TextUserName.text.toString())







    }

    val Male = true
    val Female = false



    fun AssignUserName() {
        val UsersDisplayName = FireabaseClass.UserName

        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(UsersDisplayName).build()
        user?.updateProfile(profileUpdates)

        FireStoreUtil.initCurrentUserIfFirstTime(this) {




            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->

                val Token = instanceIdResult.token
                MyFirebaseInstanceIDService.addTokenToFirestore(Token)





//                val intent = Intent(this, UserInfor::class.java)
//                startActivity(intent)
            }


        }

        // If User exists it will take you to a different page
        // Check(FireabaseClass.UserName , this)
        // FireStoreUtil.IfUsersExists(FireabaseClass.UserName , this)


    }

    fun RegisterDetails(Gender : Boolean , Username : String ) {







        // Set Username



//        if (Gender == Male) {
//
//            FireabaseClass.Gender = References.Males.toString()
//
//        } else {
//
//            FireabaseClass.Gender = References.Females.toString()
//        }
//




    }



    fun CheckGender(CodeEntered : String) {

//        val Code = CodeEntered[CodeEntered.lastIndex]
//
//        if (Code == 'B') {
//            return Male
//        } else {
//            return Female
//        }
//







    }
}







var popUped = true
class ProvideWarnings() {

    fun GiveWarnings(cont: Context, Message: String, view: FrameLayout) {


        //  val Dialog = LayoutInflater.from(cont).inflate(R.layout.provide_messagee , ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT) , true )
        val self = LayoutInflater.from(cont).inflate(my.promise.harshil.R.layout.provide_messagee, null, false)
        val popupWindow = PopupWindow(
            self, // Custom view to show in popup window
            LinearLayout.LayoutParams.MATCH_PARENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT

        )
        self.textView3.text = Message
//        popupWindow.showAtLocation(Dialog, Gravity.CENTER, 0, 0);
        // popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupWindow.elevation = 5.0f


        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
        popupWindow.enterTransition = slideIn

        // Slide animation for popup window exit transition
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.RIGHT
        popupWindow.exitTransition = slideOut


        if (popUped == false) {
            TransitionManager.beginDelayedTransition(self as ViewGroup)
            popupWindow.showAtLocation(
                self, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
            popUped = true
        }


        self.button3.setOnClickListener {
            popUped = false
            popupWindow.dismiss()
        }
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    // Every new day you will get new matches. Your Matches Change at Midnight 12.
    fun ProvideDialog(
        Message: String,
        Cont: Context,
        PlayAnimation: Boolean,
        ButtonText: String,
        KeepNormal: Boolean,
        Instructions: String,
        FirstTime: Boolean
    ) {
        val Contenty = Dialog(Cont)


        val Dialog = LayoutInflater.from(Cont).inflate(my.promise.harshil.R.layout.provide_messagee, null)

        Contenty.setContentView(Dialog)




        if (KeepNormal) {
//           Dialog.textView3.text = Message
//           Dialog.Instructions.text = Instructions
        } else {
            Dialog.textView3.text = Message
            Dialog.button3.visibility = View.GONE
            Dialog.Instructions.text = Instructions
        }


        Dialog.button3.text = ButtonText
        Dialog.button3.setOnClickListener {
            Contenty.dismiss()

        }


        // Animation Part
        if (PlayAnimation) {
            Dialog.splash.visibility = View.VISIBLE
        } else {
            // Dialog.splash.visibility = View.GONE
        }


        Contenty.show()

        if (KeepNormal) {
            Dialog.splash.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    Dialog.button3.text = ButtonText
                    Dialog.button3.visibility = View.VISIBLE
                    Dialog.button3.setOnClickListener {
                        Contenty.dismiss()


                    }


                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
        } else {
            Dialog.splash.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    Dialog.button3.text = ButtonText
                    Dialog.button3.visibility = View.VISIBLE
                    Dialog.button3.setOnClickListener {
                        Contenty.dismiss()

                    }

                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
        }


    }

    fun SmallDialog(Message: String, Instructions: String, Cont: Context, SpecificLocation: Context?) {
        val Contenty = Dialog(Cont)


        val Dialog = LayoutInflater.from(Cont).inflate(my.promise.harshil.R.layout.small_messages, null)

        Contenty.setContentView(Dialog)



        Dialog.Instruct.text = Instructions
        Dialog.textView9.text = Message
        Contenty.show()

        Dialog.CoolOk.setOnClickListener {
            if (SpecificLocation == null) {
                Contenty.dismiss()
            } else {
                val intent = Intent(Cont, SpecificLocation::class.java)
                Cont.startActivity(intent)
            }
        }


    }




    fun BlockDialog(Cont: Context, user: User, Person: User) {
        val Contenty = Dialog(Cont)


        val Dialog = LayoutInflater.from(Cont).inflate(my.promise.harshil.R.layout.block_dialog, null)

        Contenty.setContentView(Dialog)

        Contenty.show()




        Dialog.Yes.setOnClickListener {
            FireStoreUtil.getCurrentUser { user ->

                FireStoreUtil.Block(
                    Person.name,
                    user.name,
                    Person.declineOrBlocked,
                    user.declineOrBlocked,
                    user.numberOfPeopleDating,
                    Person.numberOfPeopleDating,
                    Cont,
                    Messaging::class.java
                )
                Contenty.dismiss()
                // Toast.makeText(this , "${Values.Person.name} Blocked" , Toast.LENGTH_SHORT).show()
                val intent = Intent(Cont, MainPage::class.java)
                Cont.startActivity(intent)

            }
        }
        Dialog.Cancel.setOnClickListener {

            Contenty.dismiss()
        }


    }






}

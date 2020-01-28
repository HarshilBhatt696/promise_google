package my.promise.harshil


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.content.Intent
import android.widget.*
import my.promise.harshil.Service.MyFirebaseInstanceIDService
import my.promise.harshil.Util.FireStoreUtil

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.log_in_mobile_number.*

import java.util.*


class login_mobile : AppCompatActivity() {

    lateinit var Providere : List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in_mobile_number)


        MyUserAssigned = false

        FirebaseApp.initializeApp(this)



        Providere = Arrays.asList<AuthUI.IdpConfig>(



            AuthUI.IdpConfig.GoogleBuilder().build() ,
            AuthUI.IdpConfig.PhoneBuilder().build()





        )

        button2.setOnClickListener {
            SignInMethod()
        }









    }

    private fun SignInMethod() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Providere)
                .setTheme(R.style.AppTheme)
                .build() , 7117

        )
    }


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






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 7117) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // FROM HERE WE START


                 Toast.makeText(this , "Loading...." , Toast.LENGTH_SHORT).show()

                //
                 // USER Alloted to real time data
                //AssignUserName()

                FireStoreUtil.CheckExistance(this)







            } else if (resultCode == Activity.RESULT_CANCELED) {
                    if (response == null) return

                    when (response.error?.errorCode) {
                        ErrorCodes.NO_NETWORK ->
                            Toast.makeText(this , "Error : Connect to a Wifi" , Toast.LENGTH_SHORT).show()
                        ErrorCodes.UNKNOWN_ERROR ->
                            Toast.makeText(this , "Error :Uknown Error" , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }



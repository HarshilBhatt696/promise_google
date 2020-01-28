package my.promise.harshil

import android.content.Intent
import android.os.Bundle
//import android.support.design.widget.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.data.model.User

import my.promise.harshil.Fragment.Profile
import my.promise.harshil.Fragment.Request_dates
import my.promise.harshil.Fragment.match_makin
import my.promise.harshil.Util.FireStoreUtil
import my.promise.harshil.Util.FirstTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.main_page_layout.*
import java.util.*


var MyUserAssigned = false

class MainPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_layout)






//        FireStoreUtil.getCurrentUser { user ->
//            DstingClass.FirstTimeDate(this , user)
//        }
        FireStoreUtil.CheckTrulyLogged(this) // Check this everytime
        ProvideValues()

        if (FirstTime) {
            val UsersDisplayName = FireabaseClass.UserName
            val user = FirebaseAuth.getInstance().currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(UsersDisplayName).build()
            user?.updateProfile(profileUpdates)
        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        var FragmentSent = false
        FireStoreUtil.getCurrentUser { user ->

            if (!user.numberOfPeopleDating.isEmpty()) {
                replaceFragment(Messaging())
            } else {
                replaceFragment(match_makin())
            }

            FragmentSent = true

        }

        if (!FragmentSent) {
            replaceFragment(match_makin())
        }





        nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_Message -> {
                    replaceFragment(Messaging())
                    true
                }
                R.id.navigation_Profile -> {
                    replaceFragment(Profile())
                    true


                }
                R.id.navigation_MatchMake -> {
                    replaceFragment(match_makin())
                    true
                }
                R.id.navigation_request -> {
                    replaceFragment(Request_dates())
                    true
                }
                else -> false
            }
        }




    }

    fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_layout, fragment)
            .commit()
    }

    fun ProvideValues() {

        if (!MyUserAssigned) {
            FireStoreUtil.getCurrentUser { user ->


                FireabaseClass.UserName = user.name
                FireabaseClass.Gender = user.gender
                MyUserAssigned = true // Values are assigned

            }


        }

    }


    // Get a girl for the first time user logs in
    fun FirstTimeUser() {

        if (FirstTime) {

          //  val GetMatchMakers = DstingClass.GetNumberOfPeopleMatched(1, null)

//            if (GetMatchMakers.isNotEmpty()) {
//                FireStoreUtil.NewMatch(GetMatchMakers[0], this)
//            }
        }




    }


}

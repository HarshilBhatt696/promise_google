package my.promise.harshil

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.splash_activity.*



class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

//        if (FirebaseAuth.getInstance().currentUser == null) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        } else {
//
//            val intent = Intent(this, MainPage::class.java)
//            startActivity(intent)
//        }


        Splash.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                loadingScreen()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })


        Splash.playAnimation()
//        loadingScreen()
    }

    fun loadingScreen() {

            if (FirebaseAuth.getInstance().currentUser == null) {
                val intent = Intent(this, login_mobile::class.java)
                startActivity(intent)
            } else {

                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            }

    }
}


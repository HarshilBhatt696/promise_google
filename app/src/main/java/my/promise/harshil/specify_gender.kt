package my.promise.harshil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.specify_gender.*
import my.promise.harshil.Util.FireStoreUtil


class specify_gender : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(my.promise.harshil.R.layout.specify_gender)




        Female.setOnClickListener {

            FireabaseClass.Gender = References.Females.toString()

            GoToInterestPage()

        }


        Male.setOnClickListener {

            FireabaseClass.Gender = References.Males.toString()

            GoToInterestPage()

        }








    }

    fun GoToInterestPage() {

        val S = (Intent(this, MainActivity::class.java))

        startActivity(S)



    }
}
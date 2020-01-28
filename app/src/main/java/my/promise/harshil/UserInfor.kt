package my.promise.harshil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.promise.harshil.Util.FireStoreUtil
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.user_info.*


class UserInfor : AppCompatActivity()
{

    lateinit var Providere: List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_info)

        var AskQuestionNo = 0
        DescribeText.text = Ask.Interest[AskQuestionNo]

        NextButton.setOnClickListener {
            val CurrentText = DescribeText.text.toString()

            if (AnswerText.text.isNotEmpty()) {

                UserInfo.Interest[CurrentText] = AnswerText.text.toString()


                val name:String = ""
                AnswerText.setText(name)

                if (Ask.Interest.count() - 1 > AskQuestionNo) {
                    AskQuestionNo += 1
                    DescribeText.text = Ask.Interest[AskQuestionNo]




                } else { // Limit reached - Move on

                    // Upload information Online

                    FireStoreUtil.UserInfoUpload(UserInfo.Interest)
                    val intent = Intent(this, Interest::class.java)
                    startActivity(intent)

                }


            }


        }

        Back.setOnClickListener {

            if (AskQuestionNo != 0) {
                AskQuestionNo -= 1
            }
            DescribeText.text = Ask.Interest[AskQuestionNo]
        }










    }
}

var UserInfo = CurrentUserInfo() // USE THIS TO GET THE USER INFORMATION PLEASE

class CurrentUserInfo() {

    var UserName = FireabaseClass.UserName


    var Interest = mutableMapOf<String , Any>() // Mutable List




    fun ChangeInterest() {

        // Empty Now


    }




}

class AskingQuestions() {

    val Interest = arrayListOf<String>("bio")
}

val Ask = AskingQuestions()


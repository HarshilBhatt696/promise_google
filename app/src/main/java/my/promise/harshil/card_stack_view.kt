package my.promise.harshil

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions

import my.promise.harshil.Util.FireStoreUtil
import my.promise.harshil.Util.StorageUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.promise.harshil.Flide.GlideApp
import kotlinx.android.synthetic.main.date_info.view.*
import kotlinx.android.synthetic.main.match_make_cells.view.*
import my.promise.harshil.Model.User
var RequestSent = 0

class card_stack_view : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_stack_view)



//        viewPager.setPageTransformer(true , CardStackTransformer())
//        viewPager.offscreenPageLimit = 3
//        viewPager.adapter = CardStacjAdapter(supportFragmentManager , this, FinalizedItems , )



    }


}


class CardStacjAdapter(val fm: FragmentManager, val Cont: Context, val users : ArrayList<my.promise.harshil.Model.User>, val adap : ViewPager , val user : User) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return CardStacks(users[position] , Cont , adap, fm , user )
    }

    override fun getCount(): Int {

       return users.size
    }






}

class CardStacks(val Person : my.promise.harshil.Model.User, val Cont : Context, val adap : ViewPager, val fm : FragmentManager , val user : User) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val View = inflater.inflate(R.layout.match_make_cells,  null)
        View.textView7.text = Person.bio

        var NumberOfInterestMatched = 0
        var TotalItems = 0

        for (n in Person.interest) {
            if (user.interest.contains(n)) {
                NumberOfInterestMatched += 1
            }
            TotalItems+= 1
        }




        val FinalPercentage = ((NumberOfInterestMatched * 100) / TotalItems).toInt()


        View.Percentage.text = "${FinalPercentage}% Match"


        View.ViewProfile.setOnClickListener {
            val Contenty = BottomSheetDialog(this.context!!)
            val Dialog = LayoutInflater.from(context).inflate(R.layout.date_info , null )
            Contenty.setContentView(Dialog)

            Contenty.show()

            Dialog.Cancel.setOnClickListener {
                Contenty.dismiss()
            }


                Dialog.UserDescribe.text = Person.bio
                Dialog.UserNames.text = Person.name


                if (Person.profilePicturePath != null) {
                     GlideApp.with(this)
                        .load(StorageUtil.pathToReference(Person.profilePicturePath!!))
                         .into(Dialog.UserImage)
                }



                Dialog.MatchMakeChat.setOnClickListener {



                    FireStoreUtil.getCurrentUser { user ->

                        // If he sent requests for today
                        if (user.sentRequestsForToday >= 2 || RequestSent >= 2) {

                            ProvideWarnings().SmallDialog("You can only send 1 Request in a day , Requests gets updated at Midnight 12 and every day you will get new Users to select from" , "Send Requests Tomorrow" , Cont , null)

                        }
                        else {
                            // Sends request to the Date Chat User to chat with you and talk to you.
                            RequestSent += 1
                            FireStoreUtil.UpdateOtherUsersInFO(
                                Person.name,
                                user.name,
                                Person.requests,
                                user.declineOrBlocked , user
                            ) // Todo check if it works
                            FireStoreUtil.SentRequestToOldUsers(user)
                            Contenty.dismiss()
                            Toast.makeText(Cont, "Request Sent", Toast.LENGTH_SHORT).show()

                            ProvideWarnings().SmallDialog("You will Get new people in your match makes from Midnight 12. Till then let your Match make accept your requests ;)" , "Request Sent to ${Person.name}" , Cont , null)



                            adap.adapter =
                                CardStacjAdapter(fm, Cont, ArrayList(), adap, user)
                            // In a day only one match make

                        }
                    }


                }

            Dialog.DeclineCross.setOnClickListener {

                FireStoreUtil.getCurrentUser { user ->
                    Contenty.dismiss()
//                    FireStoreUtil.DeclineRequest(Person.name, user.name, Person.NotToBeViwedAnywhere, user.numberOfPeopleDating, user.requests)
//
//                    adap.adapter = CardStacjAdapter(fm, this.context!!,   , adap)
                }



            }




            }
        if (Person.profilePicturePath != null) {


            GlideApp.with(Cont)
                .load(StorageUtil.pathToReference(Person.profilePicturePath))
               // .transform(CenterCrop(), RoundedCorners(radius))
                .apply(RequestOptions.circleCropTransform())
                .into(View.imageView2)
        }
        return View


    }

}

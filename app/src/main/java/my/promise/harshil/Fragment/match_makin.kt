package my.promise.harshil.Fragment

import android.os.Bundle
//import android.support.design.widget.BottomSheetDialog
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import my.promise.harshil.*


import my.promise.harshil.RecyclerView.Item.MatchMakinProfile

import my.promise.harshil.Util.FireStoreUtil
import my.promise.harshil.Util.StorageUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ListenerRegistration
import com.promise.harshil.Flide.GlideApp
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.date_info.view.*
import kotlinx.android.synthetic.main.date_info.view.Cancel
import kotlinx.android.synthetic.main.date_info.view.UserDescribe
import kotlinx.android.synthetic.main.date_info.view.UserImage
import kotlinx.android.synthetic.main.date_info.view.UserNames
import kotlinx.android.synthetic.main.fragment_match_makin.*
import kotlinx.android.synthetic.main.fragment_match_makin.HEs
import kotlinx.android.synthetic.main.fragment_match_makin.MatchMake
import kotlinx.android.synthetic.main.fragment_match_makin.view.*
import kotlinx.android.synthetic.main.fragment_match_makin.viewPager
import kotlinx.android.synthetic.main.main_page_layout.view.*
import kotlinx.android.synthetic.main.profile_acitivity.view.*
import my.promise.harshil.R
import my.promise.harshil.Util.FirstTime
import java.lang.Exception

val CompleteTask = DidCompleteWork()
var UserUpload = 0
class DidCompleteWork() {
    var MatchMakeeUpload = false
}


class match_makin : androidx.fragment.app.Fragment() {


    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true
    private lateinit var peopleSection: Section
    var NoOfPeopleMatched  = ArrayList<String>()// Usernames of people matched




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {

        val view = inflater.inflate(R.layout.fragment_match_makin, container, false)

        view.apply {

            view.HEs.playAnimation()
            view.HEs.visibility = View.GONE
            view.viewPager.visibility = View.GONE



            fun AssignMatchMakingOn() {
                view.MatchMake.setOnClickListener {

                    HEs.playAnimation()
                    HEs.visibility = View.VISIBLE

                    MatchMakeClicker(HEs)


                    MatchMake.visibility = View.GONE

                    view.OldMatches.visibility = View.GONE

                }
            }

            fun AssignServersOff() {

                view.MatchMake.setOnClickListener {

                  //  HEs.playAnimation()
                  //  HEs.visibility = View.VISIBLE
                    ProvideWarnings().ProvideDialog("Servers will start on 2nd February. Ready Up - Set your profile - set your Profile Photo ", this.context!! , false , "Perfect!" , false  , "Servers Starting Soon." , false )


                }
            }
//            ProvideWarnings().GiveWarnings(this.context!! , "Main Page to the following" , view.factes )

           if (FirstTime) {
                ProvideWarnings().ProvideDialog("Yes I do so", this.context!! , true , "Understood" , true  , "Instructions" , FirstTime)
                FirstTime = false
            }


            viewPager.visibility = View.GONE


            try {
                FireStoreUtil.getCurrentUser { user ->

                    if (user.notToBeViwedAnywhere.isEmpty()) {


                        view.OldMatches.visibility = View.GONE
                    } else {
                        view.OldMatches.visibility = View.VISIBLE
                    }


                }
            } catch (throwable : Exception) {}


            view.OldMatches.setOnClickListener {

                view.HEs.playAnimation()
                view.HEs.visibility = View.VISIBLE
                GetUsers(viewPager , view)
                OldMatches.visibility = View.GONE
            }

            FireabaseClass.myRef.child(References.ServersOn.toString()).addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again

                  //  FireabaseClass.myRef.child("Todo1").setValue(dataSnapshot.value)

                    if (dataSnapshot.value == "true") {
                        AssignMatchMakingOn()
                    } else if (dataSnapshot.value == "false") {
                        AssignServersOff()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value

                }

            })



        }






        return view
    }
    class CardStackTransformer() : ViewPager.PageTransformer {


        override fun transformPage(page: View, position: Float) {
            if (position >= 0) {


                // Old one
//                page.scaleX = (0.8f - 0.02f * position)
//                page.scaleY = 0.8f
//
//                page.translationX = -page.width * position
//                page.translationY = 30 * position


                page.setScaleX(0.8f - 0.05f * position);
                page.setScaleY(0.7f);
                page.setTranslationX(-page.getWidth() * position);
                page.setTranslationY(30 * position);
                page.setAlpha(1f - 0.3f * position)
            } else {
                page.setAlpha(1f + 0.3f * position)
            }

        }
    }



    var Tries = 3
    var OldTries = 3
    fun GetUsers(viewPager: ViewPager , view : View ) {






            FireStoreUtil.getCurrentUser { user ->
                DstingClass.GetUsers(view.HEs, viewPager, view.button8, user, this.fragmentManager!! , this.activity!!)

                if (FinalizedItems.isEmpty()) {
                    for (n in 0..OldTries) {
                        if (FinalizedItems.isEmpty()) {
                            DstingClass.GetUsers(
                                view.HEs,
                                viewPager,
                                view.button8,
                                user,
                                this.fragmentManager!!,
                                this.activity!!
                            )
                        }

                    }
                }


            }

    }
    fun MatchMakeClicker(Anim : LottieAnimationView) {


     // NoOfPeopleMatched = DstingClass.GetNumberOfPeopleMatched(3 , Anim)
        viewPager.visibility = View.GONE


        FireStoreUtil.getCurrentUser { user ->


            DstingClass.GetNumberOfPeopleMatched(user , button8 ,this.fragmentManager!!,viewPager ,3, Anim, this.activity!!, this::updateRecyclerView)

            if (DstingClass.FinalList.isEmpty()) {
                for (n in 0..Tries) {
                    if (DstingClass.FinalList.isEmpty()) {
                        DstingClass.GetNumberOfPeopleMatched(user ,
                            button8,
                            this.fragmentManager!!,
                            viewPager,
                            3,
                            Anim,
                            this.activity!!,
                            this::updateRecyclerView
                        )
                    }
                    val FinalArray = DstingClass.FinalList
                    NoOfPeopleMatched = FinalArray


                }


            }




        }
    }















     fun updateRecyclerView(items: List<com.xwray.groupie.kotlinandroidextensions.Item>) {

        fun init() {

            MainList.apply {
                layoutManager = LinearLayoutManager(this.context)// LinearLayoutManager(this.context) // StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)



                }


            }

            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if (shouldInitRecyclerView) {
            init()
        }
        else {
            updateItems()
        }
    }





    fun Provide() {

        FireabaseClass.myRef.child(References.ServersOn.toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again

                if (dataSnapshot.value == "true") {

                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }

        })




    }




    private val onItemClick = OnItemClickListener { item, view ->
      // if (item is PersonClass) {

            val Contenty = BottomSheetDialog(this.context!!)


            val Dialog = LayoutInflater.from(context).inflate(R.layout.date_info , null)
            Contenty.setContentView(Dialog)

            Contenty.show()

            Dialog.Cancel.setOnClickListener {
                Contenty.dismiss()
            }

        if (item is MatchMakinProfile) {
           Dialog.UserDescribe.text = item.Person.bio
            Dialog.UserNames.text = item.Person.name


            if (item.Person.profilePicturePath != null) {
                GlideApp.with(this)
                    .load(StorageUtil.pathToReference(item.Person.profilePicturePath!!))
                    .transform(CenterCrop(), RoundedCorners(8))
//                    .apply(RequestOptions.circleCropTransform())
                    .into(Dialog.UserImage)
            }



            Dialog.MatchMakeChat.setOnClickListener {



                FireStoreUtil.getCurrentUser { user ->
                    // Sends request to the Date Chat User to chat with you and talk to you.
                    FireStoreUtil.UpdateOtherUsersInFO(item.Person.name , user.name , user.requests , user.declineOrBlocked  , user )
                    FireStoreUtil.addMatchMakers(this.activity!!, ArrayList<String>(), this::updateRecyclerView)
                    Contenty.dismiss()
                    Toast.makeText(this.context , "Request Sent" , Toast.LENGTH_SHORT).show()

                    // In a day only one match make
                   // FireabaseClass.SetMatchMakingOff(user.name)
                }


            }




        
        
        
        
        
        
        







        }

        }







}


//
//class MatchMakingAdopter(val context: Context , val cell : Int , val LoversList : ArrayList<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<MatchMakingAdopter.ViewHolder>() {
//
//    // THIS IS THE CONTENT
//
//
//    // Gets the number of animals in the list
//
//
//    override fun getItemCount(): Int {
//
//        return LoversList.size
//    }
//
//    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
//        return ViewHolder(LayoutInflater.from(context).inflate(cell, p0, false))
//    }
//
//    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
//
//        // Bottom Navigation
//       p0.LovePhoto.setOnClickListener {
//                val Contenty = BottomSheetDialog(context)
//
//
//            val Dialog = LayoutInflater.from(context).inflate(R.layout.date_info , null)
//            Contenty.setContentView(Dialog)
//            Dialog.UserDescribe.text = "For now its fine"
//
//
//
//            p0.Cancel.setOnClickListener {
//                Contenty.dismiss()
//            }
//        }
//
//    }
//
//
//    //    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
//////        UserN .text = items.get(p0)
////        print("Jey")
////    }
//    class ViewHolder (view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
//        val LovePhoto = view.imageView2
//        val Cancel = view.Cancel
//    }
//}





package my.promise.harshil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import my.promise.harshil.RecyclerView.Item.PersonClass
import my.promise.harshil.Util.FireStoreUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_messaging.*
import kotlinx.android.synthetic.main.fragment_messaging.view.*
import my.promise.harshil.Model.User
import kotlin.reflect.KFunction


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Messaging.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Messaging.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Messaging : Fragment() {

    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section
    lateinit var CurrentContext : Context


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Get users only  who he matchmake
val user = inflater.inflate(R.layout.fragment_messaging, container, false)

        CurrentContext = user.context

        user.button5.setOnClickListener {
            FireStoreUtil.getCurrentUser { user ->
                MyUser = user
                userListenerRegistration =
                    FireStoreUtil.GetDateChats(user , NoMoreItems , user.numberOfPeopleDating, this.activity!!, this::updateRecyclerView)
            }
        }
        var Activity = FragmentActivity()


        var Assigned = false
        try {
            Activity = this.activity!!
            Assigned = true
        } catch (throwable : Throwable) {}

        var Done = false
        try {
            val After = this::updateRecyclerView
            Done = true
        } catch (throwable : Throwable) {

        }



            if (Assigned == true && Done) {
                FireStoreUtil.getCurrentUser { kAAY ->
                    MyUser = kAAY
                    userListenerRegistration =
                        FireStoreUtil.GetDateChats(
                            kAAY,
                            user.NoMoreItems,
                            kAAY.numberOfPeopleDating,
                            Activity,
                            this::updateRecyclerView
                        )
                }
            }








        return user
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        FireStoreUtil.removeListener(userListenerRegistration)
//        shouldInitRecyclerView = true
//    }

    private fun updateRecyclerView(items: List<com.xwray.groupie.kotlinandroidextensions.Item>) {

        fun init() {
            RecylerMessagingView.apply {

                    layoutManager = LinearLayoutManager(this@Messaging.context)
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


    private val onItemClick = OnItemClickListener { item, view ->
        if (item is PersonClass) {
            USER_NAME = item.Person.name
            USER_ID = item.UserId

            Messages = item.Person.messages
            startActivity(Intent(this.context , ChatActivity::class.java))
            itemValue = item





        }
    }


}



var  USER_NAME = "USER_NAME"
var USER_ID = "USER_ID"
var GetUserNumberNotSeen = 0
lateinit var MyUser : User
var Messages : MutableMap<String, Int> = mutableMapOf()
lateinit var itemValue : Item<ViewHolder>
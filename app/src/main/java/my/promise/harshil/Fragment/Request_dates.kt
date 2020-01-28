package my.promise.harshil.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import my.promise.harshil.Model.TextMessage

import my.promise.harshil.R
import my.promise.harshil.RecyclerView.Item.RequestClass
import my.promise.harshil.Util.FireStoreUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_request_dates.*
import com.xwray.groupie.OnItemClickListener
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_request_dates.view.*
import kotlinx.android.synthetic.main.request_bottom_navigation.view.*
import my.promise.harshil.RequestAdaptor

import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Request_dates.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Request_dates.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

// Requests that I recieved.

class Request_dates : Fragment() {


    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_request_dates, container, false)
        view.apply {



            view.button4.setOnClickListener {
                Applied() // Reload Video
            }





        }
        FireStoreUtil.getCurrentUser { user ->


                FireStoreUtil.addRequestListener(user , view.RequestRecycler , view.noMoreItems ,
                    user.requests,
                    this)

                 /// Todo : add the requests received required
        }
        return view
    }


    fun Applied() {





            FireStoreUtil.getCurrentUser { user ->


                    FireStoreUtil.addRequestListener(user , RequestRecycler , noMoreItems ,
                        user.requests,
                        this
                    ) /// Todo : add the requests received required
            }



    }


    private fun updateRecyclerView(items: List<com.xwray.groupie.kotlinandroidextensions.Item>) {





//        fun init() {
//
//
//
//
//            RequestRecycler.apply {
//                layoutManager = LinearLayoutManager(this@Request_dates.context)
//                adapter = GroupAdapter<ViewHolder>().apply {
//                    peopleSection = Section(items)
//
//                    add(peopleSection)
//                    setOnItemClickListener(onItemClick)
//                }
//
//
//            }
//
//            shouldInitRecyclerView = false
//        }
//
//        fun updateItems() = peopleSection.update(items)
//
//        if (shouldInitRecyclerView) {
//            init()
//        } else {
//            updateItems()
//        }

    }

//
}






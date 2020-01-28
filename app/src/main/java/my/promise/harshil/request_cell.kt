package my.promise.harshil

import android.content.Context
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.data.model.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.core.view.View
import com.promise.harshil.Flide.GlideApp
import kotlinx.android.synthetic.main.fragment_request_dates.*
import kotlinx.android.synthetic.main.request_bottom_navigation.view.*
import kotlinx.android.synthetic.main.request_cell.*
import kotlinx.android.synthetic.main.request_cell.view.*
import my.promise.harshil.RecyclerView.Item.RequestClass
import my.promise.harshil.Util.FireStoreUtil
import my.promise.harshil.Util.StorageUtil


class RequestAdaptor( val context: Context, val ArrayOfUsers : ArrayList<my.promise.harshil.Model.User> , val user: my.promise.harshil.Model.User) : RecyclerView.Adapter<RequestAdaptor.ViewHolder>() {

    // THIS IS THE CONTENT
    


    override fun getItemCount(): Int {
       

            return ArrayOfUsers.size
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {



        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.request_cell, parent , false))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val Persons = ArrayOfUsers[p1]

        p0.textView.text = ArrayOfUsers[p1].name
        p0.bio.text = ArrayOfUsers[p1].bio

        var NumberOfInterestMatched = 0
        var TotalItems = 0

        for (n in ArrayOfUsers[p1].interest) {
            if (user.interest.contains(n)) {
                NumberOfInterestMatched += 1
            }
            TotalItems+= 1
        }




        val FinalPercentage = ((NumberOfInterestMatched * 100) / TotalItems).toInt()
        p0.MatchInterests.text =  "${FinalPercentage}% Match"


        if (Persons.profilePicturePath != null) {



            GlideApp.with(context)
                .load(StorageUtil.pathToReference(Persons.profilePicturePath))
//                .apply(RequestOptions.circleCropTransform())
                .transform(CenterCrop() , RoundedCorners(24))
                .into(p0.ReqeustImage)

        }


        p0.ViewSelect.setOnClickListener {
            val Contenty = BottomSheetDialog(this.context!!)
            val Dialog = LayoutInflater.from(context).inflate(R.layout.request_bottom_navigation, null)
            Contenty.setContentView(Dialog)

            Contenty.show()








                Dialog.AcceptIt.setOnClickListener {
                    FireStoreUtil.getCurrentUser { user ->


                        FireStoreUtil.AcceptIt(
                            Persons.name,
                            Persons.numberOfPeopleDating,
                            user.name,
                            user.requests,
                            user.numberOfPeopleDating
                        )
                        Contenty.dismiss()

                        ProvideWarnings().SmallDialog("Request Accepted !" , "Chat with ${ Persons.name} Now. From the Chat Menu" , context , null)


//                        MainPage().replaceFragment(Messaging())

                        // Once you Accept it a automatic Message will be sent to the person you selected


                        // FireStoreUtil.AcceptRequests(item.Person.name , user.name, item.Person.numberOfPeopleDating, user.numberOfPeopleDating, user.requests)


                    }

                }

                // Decline Invitation
                Dialog.DeclineIt.setOnClickListener {
                    FireStoreUtil.getCurrentUser { user ->
                        FireStoreUtil.DeclineRequest(Persons.name, user.name, Persons.declineOrBlocked, user.numberOfPeopleDating, user.requests)

                    }

                }



            }


       

    }



    class ViewHolder (view: android.view.View) : RecyclerView.ViewHolder(view) {


            var ReqeustImage = view.ReqeustImage
        var textView = view.textView
        var bio = view.bio
        var ViewSelect = view
        var MatchInterests = view.MatchBy
    }
}


class GridItemDecoration(gridSpacingPx: Int, gridSize: Int) : RecyclerView.ItemDecoration() {
    private var mSizeGridSpacingPx: Int = gridSpacingPx
    private var mGridSize: Int = gridSize

    private var mNeedLeftSpacing = false



    override fun getItemOffsets(outRect: Rect, view: android.view.View, parent: RecyclerView, state: RecyclerView.State) {
        val frameWidth = ((parent.width - mSizeGridSpacingPx.toFloat() * (mGridSize - 1)) / mGridSize).toInt()
        val padding = parent.width / mGridSize - frameWidth
        val itemPosition = (view.getLayoutParams() as RecyclerView.LayoutParams).viewAdapterPosition
        if (itemPosition < mGridSize) {
            outRect.top = 0
        } else {
            outRect.top = mSizeGridSpacingPx
        }
        if (itemPosition % mGridSize == 0) {
            outRect.left = 0
            outRect.right = padding
            mNeedLeftSpacing = true
        } else if ((itemPosition + 1) % mGridSize == 0) {
            mNeedLeftSpacing = false
            outRect.right = 0
            outRect.left = padding
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false
            outRect.left = mSizeGridSpacingPx - padding
            if ((itemPosition + 2) % mGridSize == 0) {
                outRect.right = mSizeGridSpacingPx - padding
            } else {
                outRect.right = mSizeGridSpacingPx / 2
            }
        } else if ((itemPosition + 2) % mGridSize == 0) {
            mNeedLeftSpacing = false
            outRect.left = mSizeGridSpacingPx / 2
            outRect.right = mSizeGridSpacingPx - padding
        } else {
            mNeedLeftSpacing = false
            outRect.left = mSizeGridSpacingPx / 2
            outRect.right = mSizeGridSpacingPx / 2
        }
        outRect.bottom = 0
        super.getItemOffsets(outRect, view, parent, state)
    }
}



package my.promise.harshil.RecyclerView.Item

import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.request.RequestOptions
import com.promise.harshil.Flide.GlideApp

import my.promise.harshil.R
import my.promise.harshil.Util.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.request_cell.*

class RequestClass(val Person : my.promise.harshil.Model.User, val UserId : String, private val Cont : Context) : Item() {

    @SuppressLint("RestrictedApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {

//        FireabaseClass.myRef.child(Person.Name).setValue(Person.Gender)

        viewHolder.UserNameRequestText.text = Person.name
       // viewHolder.BioRequest.text = Person.Bio
        viewHolder.textView.text = Person.bio
        if (Person.profilePicturePath != null) {



            GlideApp.with(Cont)
                .load(StorageUtil.pathToReference(Person.profilePicturePath))
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.ReqeustImage)

        }






    }

    override fun getLayout() = R.layout.request_cell

}
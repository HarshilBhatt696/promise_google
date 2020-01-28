package my.promise.harshil.RecyclerView.Item


import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.data.model.User
import com.promise.harshil.Flide.GlideApp


import my.promise.harshil.R
import my.promise.harshil.Util.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.messaging_cell.*
import kotlinx.android.synthetic.main.request_cell.*
import my.promise.harshil.MyUser

class PersonClass(val Person : my.promise.harshil.Model.User, val UserId : String, private val Cont : Context) : Item() {

    @SuppressLint("RestrictedApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {
       viewHolder.UserName.text = Person.name


        if (MyUser.messages.containsKey(Person.name)) {
            viewHolder.button6.text = MyUser.messages[Person.name].toString()

            if (MyUser.messages[Person.name] == 0) {
                viewHolder.button6.visibility = View.GONE
            }

        } else {
            viewHolder.button6.visibility = View.GONE
        }


//        FireabaseClass.myRef.child(Person.Name).setValue(Person.Gender)

        if (Person.profilePicturePath != null) {

            GlideApp.with(Cont)
                .load(StorageUtil.pathToReference(Person.profilePicturePath))
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.imageView3)

        }


    }

    override fun getLayout() = R.layout.messaging_cell

}
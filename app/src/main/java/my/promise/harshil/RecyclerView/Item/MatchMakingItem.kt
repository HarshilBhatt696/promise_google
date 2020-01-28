package my.promise.harshil.RecyclerView.Item




import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.request.RequestOptions
import com.promise.harshil.Flide.GlideApp

import my.promise.harshil.R
import my.promise.harshil.Util.StorageUtil

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.match_make_cells.*


class MatchMakinProfile(val Person : my.promise.harshil.Model.User, val UserId : String, private val Cont : Context) : Item() {

    @SuppressLint("RestrictedApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {
     //   viewHolder.UserName.text = Person.name
       // FireabaseClass.myRef.child(Person.name).setValue(Person.gender)


        viewHolder.textView7.text = Person.bio
        if (Person.profilePicturePath != null) {


            GlideApp.with(Cont)
                .load(StorageUtil.pathToReference(Person.profilePicturePath))
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.imageView2)
        }


    }

    override fun getLayout() = my.promise.harshil.R.layout.match_make_cells

}

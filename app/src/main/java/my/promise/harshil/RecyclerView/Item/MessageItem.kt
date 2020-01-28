package my.promise.harshil.RecyclerView.Item

import android.view.Gravity
import my.promise.harshil.Model.Mesage
import my.promise.harshil.R
import com.google.firebase.auth.FirebaseAuth

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_image_messasge.*
import kotlinx.android.synthetic.main.item_text_message.*
import kotlinx.android.synthetic.main.item_text_message.LinearLayoutGravity
import kotlinx.android.synthetic.main.item_text_message.textView_message_time
import my.promise.harshil.FireabaseClass

import java.text.SimpleDateFormat


abstract class MessageItem(private val message: Mesage)
    : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }

    private fun setTimeText(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.textView_message_time.text = dateFormat.format(message.time)
    }

    private fun setMessageRootGravity(viewHolder: ViewHolder) {
        if (message.senderId == FireabaseClass.UserName) {
            viewHolder.LayoutKubear.apply {

                setBackgroundResource(R.drawable.rect_oval_white)
                viewHolder.LinearLayoutGravity1.gravity = Gravity.START

            }
        }
        else {
            viewHolder.LayoutKubear.apply {


                setBackgroundResource(R.drawable.rect_round_primary_color)



                viewHolder.LinearLayoutGravity1.gravity = Gravity.END

            }
        }
    }

    override fun getLayout() = R.layout.item_image_messasge

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {

        if (other !is TestMessageItem) {
            return false
        }
        if (this.message != other.message) {
            return false
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TestMessageItem)
    }
    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + this.hashCode()
        return result
    }
}
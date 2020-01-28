package my.promise.harshil.RecyclerView.Item

import android.content.Context
import com.promise.harshil.Flide.GlideApp

import my.promise.harshil.Model.ImageMessage
import my.promise.harshil.R
import my.promise.harshil.Util.StorageUtil

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_image_messasge.*


class ImageMessageItem(val message: ImageMessage,
                       val context: Context)
    : MessageItem(message ) {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)
        GlideApp.with(context)
            .load(StorageUtil.pathToReference(message.imagePath))
            .into(viewHolder.imageView_message_image)
    }

    override fun getLayout() = R.layout.item_image_messasge

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ImageMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ImageMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}
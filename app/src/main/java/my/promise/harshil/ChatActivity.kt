package my.promise.harshil

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import my.promise.harshil.Model.ImageMessage
import my.promise.harshil.Model.TextMessage
import my.promise.harshil.RecyclerView.Item.PersonClass
import my.promise.harshil.Util.FireStoreUtil
import my.promise.harshil.Util.FireStoreUtil.getOrCreateChatChannel
import my.promise.harshil.Util.StorageUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.promise.harshil.Flide.GlideApp
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.profile_acitivity.view.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE = 2
class ChatActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var currentChannelId: String
    private lateinit var currentUser: my.promise.harshil.Model.User
    private lateinit var otherUserId: String

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = USER_NAME

        FireStoreUtil.getCurrentUser {
            currentUser = it
        }

        otherUserId = (USER_ID)


        FireStoreUtil.getCurrentUser {
            FireStoreUtil.RemoveUser(USER_ID, it.messages)
        }


        getOrCreateChatChannel(  otherUserId) { channelId ->
            currentChannelId = channelId

            messagesListenerRegistration =
                FireStoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)


            SendItems.setOnClickListener {
                if (!EditTextMessages.text!!.isEmpty()) {

                    val messageToSends = TextMessage(
                        EditTextMessages.text.toString(),
                        Calendar.getInstance().time,
                        FireabaseClass.UserName,
                        otherUserId,
                        currentUser.name
                    )


                    FireStoreUtil.getCurrentUser {
                        FireStoreUtil.AddMessage(it , otherUserId)


                    }

                    EditTextMessages.setText("")
                    FireStoreUtil.sendMessage(messageToSends, channelId)
                }
            }

            Image_Clicker.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }

        }







    }

    private fun updateRecyclerView(message: List<Item>) {


        fun init() {
            recyclerForMessages.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(message)
                    this.add(messagesSection)
                }
            }

        }
        fun update() = messagesSection.update(message)

        if (shouldInitRecyclerView) {
            init()
        }
        else {
            update()
        }

        recyclerForMessages.scrollToPosition(recyclerForMessages.adapter!!.itemCount - 1)



    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_activity, menu)



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Grids -> {

            }
            R.id.Each_user -> {
                SetBottomProfileActivty()

            }
        }


        return super.onOptionsItemSelected(item)
    }


    fun SetBottomProfileActivty() {
        val Contenty = BottomSheetDialog(this , R.style.full_screen_dialog)
        val Dialog = LayoutInflater.from(this).inflate(R.layout.profile_acitivity , null)
        Contenty.setContentView(Dialog)

        Contenty.show()

        Dialog.Cancel.setOnClickListener {
            Contenty.dismiss()
        }

        val Values = itemValue as PersonClass
        Dialog.UserNames.text = Values.Person.name
        Dialog.UserDescribe.text = Values.Person.bio

        if (Values.Person.profilePicturePath != null) {
            GlideApp.with(this)
                .load(StorageUtil.pathToReference(Values.Person.profilePicturePath))

                .into(Dialog.UserImage)
        }

        Dialog.Block.setOnClickListener {


            FireStoreUtil.getCurrentUser { user ->

//                FireStoreUtil.Block(Values.Person.name , user.name , Values.Person.declineOrBlocked , user.declineOrBlocked , user.numberOfPeopleDating , Values.Person.numberOfPeopleDating , this , Messaging::class.java)
////
////                Toast.makeText(this , "${Values.Person.name} Blocked" , Toast.LENGTH_SHORT).show()
////                val intent = Intent(this, Messaging::class.java)
////                startActivity(intent)


                ProvideWarnings().BlockDialog(this , user , Values.Person)



            }

        }

  // Everyday your matches change at midnight 12



    }


    // Upload image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null) {
            val selectedImagePath = data.data

            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            StorageUtil.uploadMessageImage(selectedImageBytes) { imagePath ->
                val messageToSend =
                    ImageMessage(imagePath, Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.displayName!!,
                        otherUserId, currentUser.name)
                FireStoreUtil.sendMessage(messageToSend, currentChannelId)
            }
        }
    }




}

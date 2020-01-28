package my.promise.harshil.Fragment


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions

//import com.example.tipster.Glide.GlideApp


import my.promise.harshil.Util.FireStoreUtil
import my.promise.harshil.Util.StorageUtil
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.promise.harshil.Flide.GlideApp


import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.Bio
import kotlinx.android.synthetic.main.fragment_profile.MatchMakeSameGender
import kotlinx.android.synthetic.main.fragment_profile.SignOut
import kotlinx.android.synthetic.main.fragment_profile.view.*
import my.promise.harshil.*

import java.io.ByteArrayOutputStream

//import com.example.tipster.FireMessageGlideModule





// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Profile : androidx.fragment.app.Fragment() {


    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {





        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.apply {

            FireabaseClass.myRef.child("Values").setValue(((71 * 100)  / 100).toInt())




            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this.context , MainActivity::class.java))
            }

            view.roundedimag.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }

            view.Confirm.setOnClickListener {
                if (::selectedImageBytes.isInitialized) {
                    StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                      // FireStoreUtil.updateCurrentUser(editText2.text.toString() , imagePath)




                    }
                } else {
                    FireStoreUtil.UpdateUser(Bio.text.toString() , null)
                }



                ProvideWarnings().SmallDialog("Don't quit The app as it may take few moments to change your Profile" , " Profile Changed" , context , null)


            }
            view.SignOut.setOnClickListener {



                AuthUI.getInstance()
                    .signOut(this@Profile.context!!)
                startActivity(Intent(this.context , login_mobile::class.java))




            }

            view.MatchMakeSameGender.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    FireStoreUtil.MatchMakeSameGender(true)
                } else {
                    FireStoreUtil.MatchMakeSameGender(false)
                }
            }








            return view
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null) {
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media
                .getBitmap(activity?.contentResolver, selectedImagePath)


            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()



           GlideApp.with(this)
               .load(selectedImageBytes)
                .apply(RequestOptions.circleCropTransform())
                 .into(roundedimag)


            pictureJustChanged = true

        }
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this.context , MainActivity::class.java))
        }


        FireStoreUtil.getCurrentUser { user ->
            if (this@Profile.isVisible) {
                Bio.setText(user.bio)
                MyName.setText(user.name)

                MatchMakeSameGender.isChecked = user.matchMakeSameGender



                if (!pictureJustChanged && user.profilePicturePath != null) {





                    GlideApp.with(this)
                        .load(StorageUtil.pathToReference(user.profilePicturePath))
                        .apply(RequestOptions.circleCropTransform())
                        .into(roundedimag)


//                    Glide.with(this)
//                        .load(StorageUtil.pathToReference(user.profilePicturePath))
//                     //   .apply(RequestOptions().placeholder(R.drawable.ic_alarm_add_black_24dp))
//                        .into(imageView)

                }







            }

        }


        }


}










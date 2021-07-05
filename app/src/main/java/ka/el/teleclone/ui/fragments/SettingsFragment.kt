package ka.el.teleclone.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.databinding.FragmentChatBinding
import ka.el.teleclone.databinding.FragmentSettingsBinding
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

        activity?.title = getString(R.string.settings)

        initFields()
    }

    private fun initFields() {
        if (!USER.photo_url.isEmpty()) {
            profile_image.downloadAndSetImage(USER.photo_url)
        }

        settings_full_name.text = USER.full_name
        settings_status.text = USER.status
        settings_phone_number.text = USER.phone_number
        settings_user_name.text = USER.user_name
        settings_bio.text = USER.bio

        settings_btn_user_name.setOnClickListener {
            replaceFragment(R.id.dataContainer, ChangeUserNameFragment())
        }
        settings_btn_bio.setOnClickListener {
            replaceFragment(R.id.dataContainer, ChangeBioFragment())
        }


        settings_btn_change_photo.setOnClickListener { changePhoto() }
    }

    private fun changePhoto() {
        CropImage.activity()
            .setRequestedSize(600, 600)
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings_exit -> {
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(RegistrationActivity())
            }

            R.id.settings_change_name -> {
                (activity as MainActivity).replaceFragment(R.id.dataContainer, ChangeNameFragment())
            }
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            && (resultCode == RESULT_OK)
            && (data != null)
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_PHOTO).child(UID)

            putImageToStorage(uri, path) {
                getUrlFromStorage(path) { photo_url ->
                    putUrlToDatabase(photo_url) {
                        showToast(getString(R.string.update_data))
                        USER.photo_url = photo_url
                        profile_image.downloadAndSetImage(photo_url)
                    }
                }
            }
        }
    }
}
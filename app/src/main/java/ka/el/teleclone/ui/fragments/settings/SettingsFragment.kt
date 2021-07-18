package ka.el.teleclone.ui.fragments.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ka.el.teleclone.R
import ka.el.teleclone.database.*
import ka.el.teleclone.ui.fragments.BaseFragment
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {


    override fun onResume() {
        super.onResume()

        setHasOptionsMenu(true)
        APP_ACTIVITY.title = getString(R.string.settings)
        initFields()
    }

    private fun initFields() {
        if (USER.photo_url.isNotEmpty()) {
            profile_image.downloadAndSetImage(USER.photo_url)
        }

        settings_full_name.text = USER.full_name
        settings_status.text = USER.state
        settings_phone_number.text = USER.phone_number
        settings_user_name.text = USER.user_name
        settings_bio.text = USER.bio

        settings_btn_user_name.setOnClickListener {
            replaceFragment(ChangeUserNameFragment())
        }
        settings_btn_bio.setOnClickListener {
            replaceFragment(ChangeBioFragment())
        }


        settings_btn_change_photo.setOnClickListener { changePhoto() }
    }

    private fun changePhoto() {
        CropImage.activity()
            .setRequestedSize(250, 250)
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITY.menuInflater.inflate(R.menu.settings_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }

            R.id.settings_change_name -> {
                replaceFragment(ChangeNameFragment())
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

            putFileToStorage(uri, path) {
                getUrlFromStorage(path) { photo_url ->
                    putUrlToDatabase(photo_url) {
                        showToast(getString(R.string.update_data))
                        USER.photo_url = photo_url

                        profile_image.downloadAndSetImage(photo_url)
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }
}
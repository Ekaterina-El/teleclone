package ka.el.teleclone.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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

        settings_full_name.setText(USER.full_name)
        settings_status.setText(USER.status)
        settings_phone_number.setText(USER.phone_number)
        settings_user_name.setText(USER.user_name)
        settings_bio.setText(USER.bio)

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
            .start(APP_ACTIVITY )
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
}
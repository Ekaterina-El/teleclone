package ka.el.teleclone.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.databinding.FragmentChatBinding
import ka.el.teleclone.databinding.FragmentSettingsBinding
import ka.el.teleclone.utils.AUTH
import ka.el.teleclone.utils.replaceActivity

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

        activity?.title = getString(R.string.settings)
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
        }

        return true
    }
}
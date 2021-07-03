package ka.el.teleclone.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.fragment_change_user_name.*
import java.util.*

class ChangeUserNameFragment : BaseFragment(R.layout.fragment_change_user_name) {
    private lateinit var mNewUserName: String
    private lateinit var oldTitle: String

    override fun onResume() {
        super.onResume()

        setHasOptionsMenu(true)
        change_user_name.setText(USER.user_name)

        oldTitle = activity?.title.toString()
        activity?.title = getString(R.string.change_user_name)
    }

    override fun onStop() {
        super.onStop()

        activity?.title = oldTitle
    }

    private fun changeUserName() {
        mNewUserName = change_user_name.text.toString().toLowerCase(Locale.getDefault())

        if (mNewUserName.isEmpty()) {
            showToast(getString(R.string.error_change_user_name_empty))
        }
        else if (mNewUserName.equals("Inactive user")) {
            showToast(getString(R.string.invalide_user_name))
        }
        else {
            REF_DATABASE_ROOT.child(NODE_USERS_NAME).child(mNewUserName).setValue(UID)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        changeUserNameDB()
                    } else {
                        showToast(it.exception.toString())
                    }
                }
        }
    }

    private fun changeUserNameDB() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USER_NAME).setValue(mNewUserName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (mNewUserName.equals(USER.user_name)) {
                        fragmentManager?.popBackStack()
                    } else {
                        deleteOldUserName()
                    }
                } else {
                    showToast(it.exception.toString())
                }
            }
    }

    private fun deleteOldUserName() {
        REF_DATABASE_ROOT.child(NODE_USERS_NAME).child(USER.user_name).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.update_data))
                    USER.user_name = mNewUserName
                    fragmentManager?.popBackStack()
                } else {
                    showToast(it.exception.toString())
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        (activity as MainActivity).menuInflater.inflate(R.menu.change_name_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.change_name_save -> {
                changeUserName()
            }
        }

        return true
    }
}
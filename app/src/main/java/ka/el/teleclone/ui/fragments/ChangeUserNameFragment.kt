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

class ChangeUserNameFragment : BaseChangeFragment(R.layout.fragment_change_user_name) {
    private lateinit var mNewUserName: String

    override fun onResume() {
        super.onResume()

        change_user_name.setText(USER.user_name)
        activity?.title = getString(R.string.change_user_name)
    }

     override fun change() {
        mNewUserName = change_user_name.text.toString().toLowerCase(Locale.getDefault())

         when {
             mNewUserName.isEmpty() -> {
                 showToast(getString(R.string.error_change_user_name_empty))
             }

             mNewUserName == "Inactive user" -> {
                 showToast(getString(R.string.invalide_user_name))
             }

             else -> {
                 REF_DATABASE_ROOT.child(NODE_USERS_NAME).child(mNewUserName).setValue(UID)
                     .addOnCompleteListener {
                         if (it.isSuccessful) {
                             changeUserNameDB(mNewUserName)
                         } else {
                             showToast(it.exception.toString())
                         }
                     }
             }
         }
    }


}
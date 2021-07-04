package ka.el.teleclone.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {


    override fun onResume() {
        super.onResume()

        setDefaultValue()
        activity?.title = getString(R.string.change_full_name)
    }

    private fun setDefaultValue() {
        val fullNameList = USER.full_name.split(" ")
        change_name.setText(fullNameList[0])
        change_surname.setText(fullNameList[1])
    }

    override fun change() {
        val name = change_name.text.toString()
        val surname = change_surname.text.toString()

        if (name.isEmpty()) {
            showToast(getString(R.string.change_name_error))
        } else {
            val full_name = "$name $surname"

            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FULL_NAME)
                .setValue(full_name).addOnCompleteListener {
                if (it.isSuccessful) {
                    USER.full_name = full_name
                    showToast("Данные обновленны")
                    fragmentManager?.popBackStack()
                }
            }
        }
    }


}
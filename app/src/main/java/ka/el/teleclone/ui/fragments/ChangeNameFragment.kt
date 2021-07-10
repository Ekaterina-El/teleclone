package ka.el.teleclone.ui.fragments

import ka.el.teleclone.R
import ka.el.teleclone.utils.USER
import ka.el.teleclone.utils.changeFullName
import ka.el.teleclone.utils.showToast
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
        if (fullNameList.size > 1) {
            change_surname.setText(fullNameList[1])
        }
    }

    override fun change() {
        val name = change_name.text.toString()
        val surname = change_surname.text.toString()

        if (name.isEmpty()) {
            showToast(getString(R.string.change_name_error))
        } else {
            changeFullName("$name $surname")
        }
    }
}
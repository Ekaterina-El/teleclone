package ka.el.teleclone.ui.fragments

import ka.el.teleclone.R
import ka.el.teleclone.utils.APP_ACTIVITY
import ka.el.teleclone.utils.USER
import ka.el.teleclone.utils.saveBio
import kotlinx.android.synthetic.main.fragment_change_bio.*

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {
    private lateinit var mNewBio: String

    override fun onResume() {
        super.onResume()

        setDefaultValue()
        activity?.title = getString(R.string.change_bio)
    }

    private fun setDefaultValue() {
        change_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()

        mNewBio = change_bio.text.toString()
        if (mNewBio == USER.bio) APP_ACTIVITY.supportFragmentManager.popBackStack() else saveBio(mNewBio)
    }
}
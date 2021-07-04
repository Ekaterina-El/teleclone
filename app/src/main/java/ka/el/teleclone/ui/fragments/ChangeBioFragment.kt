package ka.el.teleclone.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ka.el.teleclone.R
import ka.el.teleclone.utils.*
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

        if (mNewBio == USER.bio) back() else saveBio()
    }

    private fun saveBio() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_BIO).setValue(mNewBio).addOnCompleteListener {
            if (it.isSuccessful) {
                USER.bio = mNewBio
                back()
            } else showToast(it.exception.toString())
        }
    }

    private fun back() {
        fragmentManager?.popBackStack()
    }
}
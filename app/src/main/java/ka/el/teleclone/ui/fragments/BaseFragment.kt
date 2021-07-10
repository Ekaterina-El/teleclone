package ka.el.teleclone.ui.fragments

import androidx.fragment.app.Fragment
import ka.el.teleclone.utils.APP_ACTIVITY

open class BaseFragment(val layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()

        /* General func */
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }
}
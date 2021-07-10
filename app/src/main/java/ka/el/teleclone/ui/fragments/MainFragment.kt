package ka.el.teleclone.ui.fragments

import androidx.fragment.app.Fragment
import ka.el.teleclone.R
import ka.el.teleclone.utils.APP_ACTIVITY

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onResume() {
        super.onResume()

        activity?.title = getString(R.string.app_name)
        APP_ACTIVITY.mAppDrawer.enableDrawer()
    }
}
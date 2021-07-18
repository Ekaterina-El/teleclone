package ka.el.teleclone.ui.fragments.settings

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import ka.el.teleclone.R
import ka.el.teleclone.ui.fragments.BaseFragment
import ka.el.teleclone.utils.APP_ACTIVITY
import ka.el.teleclone.utils.hideKeyboard

open class BaseChangeFragment(layout: Int) : BaseFragment(layout) {
    private lateinit var oldTitle: String

    open fun change() {}

    override fun onStart() {
        super.onStart()

        oldTitle = activity?.title.toString()
        setHasOptionsMenu(true)
    }

    override fun onStop() {
        super.onStop()

        activity?.title = oldTitle
        APP_ACTIVITY.hideKeyboard()
        APP_ACTIVITY.mAppDrawer.updateHeader()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        APP_ACTIVITY.menuInflater.inflate(R.menu.change_name_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_name_save -> {
                change()
            }
        }
        return true
    }
}
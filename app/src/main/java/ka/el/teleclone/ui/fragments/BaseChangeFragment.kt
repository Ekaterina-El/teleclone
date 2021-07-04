package ka.el.teleclone.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R

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
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        (activity as MainActivity).menuInflater.inflate(R.menu.change_name_menu, menu)
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
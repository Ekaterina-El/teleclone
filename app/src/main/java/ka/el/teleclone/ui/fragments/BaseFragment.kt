package ka.el.teleclone.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R

open class BaseFragment(private val layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()

        /* General func */
        (activity as MainActivity).mAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).mAppDrawer.enableDrawer()
    }
}
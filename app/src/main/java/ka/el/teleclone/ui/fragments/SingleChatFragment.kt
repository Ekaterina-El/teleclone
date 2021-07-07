package ka.el.teleclone.ui.fragments

import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import ka.el.teleclone.R
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.utils.APP_ACTIVITY
import ka.el.teleclone.utils.downloadAndSetImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.toolbar_info.view.*

class SingleCharFragment(val contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {
    private lateinit var mToolBar:View

    override fun onResume() {
        super.onResume()
        mToolBar = APP_ACTIVITY.mainToolbar.toolbar_info
        mToolBar.visibility = View.VISIBLE

        updateToolInfo()
    }

    private fun updateToolInfo() {
        mToolBar.toolbar_info_name.text = contact.full_name
        mToolBar.toolbar_info_state.text = contact.state
        mToolBar.toolbar_info_photo.downloadAndSetImage(contact.photo_url)
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mainToolbar.toolbar_info.visibility = View.GONE
    }
}
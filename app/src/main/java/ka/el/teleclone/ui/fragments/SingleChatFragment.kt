package ka.el.teleclone.ui.fragments

import android.view.View
import com.google.firebase.database.DatabaseReference
import ka.el.teleclone.R
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.models.User
import ka.el.teleclone.ui.objects.AppValueEventListener
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.toolbar_info.view.*

class SingleCharFragment(val contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {
    private lateinit var mToolBar: View
    private lateinit var mContactInfo: User
    private lateinit var mListener: AppValueEventListener
    private lateinit var mDatabaseReference: DatabaseReference
    private val mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun onResume() {
        super.onResume()
        mToolBar = APP_ACTIVITY.mainToolbar.toolbar_info
        mToolBar.visibility = View.VISIBLE


        mListener = AppValueEventListener {
            mContactInfo = it.getUserModel()
            updateToolInfo()
        }
        mDatabaseReference = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mDatabaseReference.addValueEventListener(mListener)
        mapListeners[mDatabaseReference] = mListener
    }

    private fun updateToolInfo() {
        mToolBar.toolbar_info_name.text =
            if (mContactInfo.full_name != "Inactive user") mContactInfo.full_name else contact.full_name
        mToolBar.toolbar_info_state.text = mContactInfo.state
        mToolBar.toolbar_info_photo.downloadAndSetImage(mContactInfo.photo_url)
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mainToolbar.toolbar_info.visibility = View.GONE

        clearMemory()
    }

    private fun clearMemory() {
        mapListeners.forEach {
            it.key.removeEventListener(it.value)
        }
    }
}
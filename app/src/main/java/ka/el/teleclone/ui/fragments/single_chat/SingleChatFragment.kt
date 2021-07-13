package ka.el.teleclone.ui.fragments.single_chat

import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import ka.el.teleclone.R
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.models.User
import ka.el.teleclone.ui.fragments.BaseFragment
import ka.el.teleclone.ui.objects.AppChildEventListener
import ka.el.teleclone.ui.objects.AppValueEventListener
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*

class SingleCharFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mToolBar: View
    private lateinit var mContactInfo: User
    private lateinit var mListener: AppValueEventListener
    private lateinit var mDatabaseReference: DatabaseReference

    private lateinit var chatRecycleView: RecyclerView
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mDialogRef: DatabaseReference
    private lateinit var mDialogListener: ChildEventListener

    private var mCountMessages = 10
    private val mLoadCountMessages = 10
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val mapAppValueEventListeners = hashMapOf<DatabaseReference, AppValueEventListener>()
    private val mapAppChildEventListeners = hashMapOf<DatabaseReference, ChildEventListener>()

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        swipeRefreshLayout = sc_swipe_refresh_layout
        chatRecycleView = single_chat_list
        mAdapter = SingleChatAdapter()
        chatRecycleView.adapter = mAdapter

        mDialogListener = AppChildEventListener {
            mAdapter.addItem(it.getCommonModel(), mSmoothScrollToPosition) {
                if (mSmoothScrollToPosition) {
                    chatRecycleView.smoothScrollToPosition(mAdapter.itemCount)
                }
                swipeRefreshLayout.isRefreshing = false
            }


        }

        mDialogRef = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(contact.id)
        mDialogRef.limitToLast(mLoadCountMessages).addChildEventListener(mDialogListener)
        mapAppChildEventListeners[mDialogRef] = mDialogListener

        chatRecycleView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0) {
                    updateData()
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        mIsScrolling = false
        mSmoothScrollToPosition = false
        mCountMessages += mLoadCountMessages

        mDialogRef.removeEventListener(mDialogListener)
        mDialogRef.limitToLast(mCountMessages).addChildEventListener(mDialogListener)
        mapAppChildEventListeners[mDialogRef] = mDialogListener
    }

    private fun initToolbar() {
        mToolBar = APP_ACTIVITY.mainToolbar.toolbar_info
        mToolBar.visibility = View.VISIBLE


        mListener = AppValueEventListener {
            mContactInfo = it.getUserModel()
            updateToolInfo()
        }
        mDatabaseReference = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mDatabaseReference.addValueEventListener(mListener)
        mapAppValueEventListeners[mDatabaseReference] = mListener

        chat_btn_send_message.setOnClickListener {
            val message = chat_message_input.text.toString()

            if (message.isNotEmpty()) {
                sendMessage(message, contact.id, TYPE_TEXT) {
                    mSmoothScrollToPosition = true
                    chat_message_input.setText("")
                }
            }
        }
    }


    private fun updateToolInfo() {
        mToolBar.toolbar_info_name.text =
            if (mContactInfo.full_name != "" && mContactInfo.full_name != "Inactive user") mContactInfo.full_name else contact.full_name
        mToolBar.toolbar_info_state.text = mContactInfo.state
        mToolBar.toolbar_info_photo.downloadAndSetImage(mContactInfo.photo_url)
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mainToolbar.toolbar_info.visibility = View.GONE
        clearMemory(mapAppValueEventListeners)
        clearMemory(mapAppChildEventListeners)
    }
}
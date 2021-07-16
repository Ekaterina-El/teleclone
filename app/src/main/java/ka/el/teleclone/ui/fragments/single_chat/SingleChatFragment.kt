package ka.el.teleclone.ui.fragments.single_chat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import ka.el.teleclone.R
import ka.el.teleclone.database.*
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.models.User
import ka.el.teleclone.ui.fragments.BaseFragment
import ka.el.teleclone.ui.fragments.message_recycler_view.views.AppViewFactory
import ka.el.teleclone.ui.objects.AppChildEventListener
import ka.el.teleclone.ui.objects.AppValueEventListener
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.choice_attach.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>

    private val mapAppValueEventListeners = hashMapOf<DatabaseReference, AppValueEventListener>()
    private val mapAppChildEventListeners = hashMapOf<DatabaseReference, ChildEventListener>()

    private lateinit var mVoiceRecorder: AppVoiceRecorder

    override fun onResume() {
        super.onResume()
        mVoiceRecorder = AppVoiceRecorder()

        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_choice)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        initToolbar()
        initRecyclerView()
        initMessageSenders()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initMessageSenders() {
        chat_message_input.addTextChangedListener(AppTextWatcher {
            val messageText = chat_message_input.text.toString()

            if (messageText.isEmpty() || messageText == "Record") {
                chat_btn_attach.visibility = View.VISIBLE
                chat_btn_record_voice_message.visibility = View.VISIBLE
                chat_btn_send_message.visibility = View.GONE
            } else {
                chat_btn_attach.visibility = View.GONE
                chat_btn_record_voice_message.visibility = View.GONE
                chat_btn_send_message.visibility = View.VISIBLE
            }
        })

        chat_btn_attach.setOnClickListener { attach() }

        CoroutineScope(Dispatchers.IO).launch {
            chat_btn_record_voice_message.setOnTouchListener { _, event ->
                if (checkPermission(RECORD_AUDIO)) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        // Start record
                        chat_message_input.setText("Record")
                        chat_btn_record_voice_message.setColorFilter(
                            ContextCompat.getColor(
                                APP_ACTIVITY,
                                R.color.purple_500
                            )
                        )
                        val messageKey = getMessageKey(contact.id)
                        mVoiceRecorder.startRecord(messageKey)

                    } else if (event.action == MotionEvent.ACTION_UP) {
                        // Stop record
                        chat_message_input.setText("")
                        chat_btn_record_voice_message.setColorFilter(
                            ContextCompat.getColor(
                                APP_ACTIVITY,
                                R.color.grey
                            )
                        )
                        mVoiceRecorder.stopRecord { file, messageKey ->
                            uploadFileToStorage(contact.id, messageKey, Uri.fromFile(file), TYPE_MESSAGE_VOICE) {
                                mSmoothScrollToPosition = true
                            }
                        }
                    }
                }
                true
            }
        }
    }

    private fun attach() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        btn_sheet_file.setOnClickListener { attachFile() }
        btn_sheet_image.setOnClickListener { attachImage() }
    }

    private fun attachFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PEEK_FILE_REQUEST_CODE)
    }

    private fun attachImage() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((resultCode == Activity.RESULT_OK)
            && (data != null)) {

            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val uri = CropImage.getActivityResult(data).uri
                    val messageKey = getMessageKey(contact.id)
                    uploadFileToStorage(contact.id, messageKey, uri, TYPE_MESSAGE_IMAGE) {
                        mSmoothScrollToPosition = true
                    }
                }

                PEEK_FILE_REQUEST_CODE -> {
                    val uri = data.data
                    val messageKey = getMessageKey(contact.id)
                    uri?.let {
                        uploadFileToStorage(contact.id, messageKey, it, TYPE_MESSAGE_FILE) {
                            mSmoothScrollToPosition = true
                        }
                    }
                }
            }
        }
    }


    private fun initRecyclerView() {
        mLayoutManager = LinearLayoutManager(context)
        swipeRefreshLayout = sc_swipe_refresh_layout
        mAdapter = SingleChatAdapter()

        chatRecycleView = single_chat_list
        chatRecycleView.adapter = mAdapter
        chatRecycleView.layoutManager = mLayoutManager
        chatRecycleView.isNestedScrollingEnabled = false
        chatRecycleView.setHasFixedSize(true)

        mDialogListener = AppChildEventListener {
            val message = it.getCommonModel()
            val messageView = AppViewFactory.getView(message)

            if (mSmoothScrollToPosition) {
                mAdapter.addItemToBottom(messageView) {
                    chatRecycleView.smoothScrollToPosition(mAdapter.itemCount - 1)
                }
            } else {
                mAdapter.addItemToTop(messageView) {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        mDialogRef = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(contact.id)
        mDialogRef.limitToLast(mLoadCountMessages).addChildEventListener(mDialogListener)
        mapAppChildEventListeners[mDialogRef] = mDialogListener

        chatRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3) {
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
                sendMessage(message, contact.id, TYPE_MESSAGE_TEXT) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        mVoiceRecorder.releaseRecord()
        mAdapter.destroy()
    }
}
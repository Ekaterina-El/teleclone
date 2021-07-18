package ka.el.teleclone.ui.fragments.main_list

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.R
import ka.el.teleclone.database.*
import ka.el.teleclone.ui.objects.AppValueEventListener
import ka.el.teleclone.utils.APP_ACTIVITY
import kotlinx.android.synthetic.main.fragment_main_list.*

class MainFragment : Fragment(R.layout.fragment_main_list) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MainListAdapter
    private val refMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(UID)
    private val refUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val refMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID)


    override fun onResume() {
        super.onResume()

        activity?.title = getString(R.string.app_name)
        APP_ACTIVITY.mAppDrawer.enableDrawer()

        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = main_list_recycler_view
        mAdapter = MainListAdapter()
        mRecyclerView.adapter = mAdapter


        refMainList.addListenerForSingleValueEvent(AppValueEventListener { data ->
            val mMainListNotes = data.children.map { it.getCommonModel() }
            mMainListNotes.forEach { mMainListItem ->

                refUsers.child(mMainListItem.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { userInfo ->
                        val mUserInfo = userInfo.getCommonModel()

                        refMessages.child(mMainListItem.id).limitToLast(1)
                            .addListenerForSingleValueEvent(AppValueEventListener { lastMessages ->
                                val mLastMessages = lastMessages.children.map { it.getCommonModel() }
                                mUserInfo.last_message = mLastMessages[0].text

                                if (mUserInfo.full_name.isEmpty()) {
                                    mUserInfo.full_name = mUserInfo.phone_number
                                }

                                mAdapter.updateItem(mUserInfo)
                            })
                    })
            }
        })
    }
}
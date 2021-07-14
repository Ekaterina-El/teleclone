package ka.el.teleclone.ui.fragments

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import ka.el.teleclone.R
import ka.el.teleclone.database.*
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.ui.fragments.single_chat.SingleCharFragment
import ka.el.teleclone.ui.objects.AppValueEventListener
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.contact_item.view.*
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference

    private lateinit var mUsersListener: AppValueEventListener
    private val mapListener = hashMapOf<DatabaseReference, AppValueEventListener>()

    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fullName: TextView = view.contact_full_name
        val state: TextView = view.contact_state
        val photo: CircleImageView = view.contact_photo
    }


    override fun onResume() {
        super.onResume()
        initRecyclerView()
        APP_ACTIVITY.title = getString(R.string.contacts_title)
    }

    private fun initRecyclerView() {
        mRecyclerView = contacts_list
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(UID)


        val options = FirebaseRecyclerOptions
            .Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = layoutInflater.inflate(R.layout.contact_item, parent, false)
                return ContactsHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel()

                    holder.fullName.text =
                        if (contact.full_name != "") contact.full_name else model.full_name
                    holder.state.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photo_url)

                    holder.itemView.setOnClickListener {
                        replaceFragment(SingleCharFragment(model))
                    }
                }

                mRefUsers.addValueEventListener(mUsersListener)
                mapListener[mRefUsers] = mUsersListener
            }
        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
        clearMemory(mapListener)
    }
}

package ka.el.teleclone.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.R
import ka.el.teleclone.database.UID
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.ui.fragments.message_recycler_view.view_holders.AppHolderFactory
import ka.el.teleclone.ui.fragments.message_recycler_view.view_holders.ImageMessageHolder
import ka.el.teleclone.ui.fragments.message_recycler_view.view_holders.TextMessageHolder
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView
import ka.el.teleclone.utils.*

class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listMessagesCache = mutableListOf<MessageView>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mMessage = listMessagesCache[position]

        when (holder) {
            is ImageMessageHolder -> drawMessageImage(holder, mMessage)
            is TextMessageHolder -> drawMessageText(holder, mMessage)
            else -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return listMessagesCache[position].getTypeView()
    }

    private fun drawMessageImage(holder: ImageMessageHolder, mMessage: MessageView) {
        if (mMessage.from == UID) {
            holder.chatUserImageMessageParent.visibility = View.VISIBLE
            holder.chatReceiverImageMessageParent.visibility = View.GONE
            holder.chatUserTimeImageMessage.text = mMessage.timestamp.asTime()
            holder.chatUserImageMessageImg.downloadAndSetImage(mMessage.file_url)
        } else {
            holder.chatUserImageMessageParent.visibility = View.GONE
            holder.chatReceiverImageMessageParent.visibility = View.VISIBLE
            holder.chatReceiverTimeImageMessage.text = mMessage.timestamp.asTime()
            holder.chatReceiverImageMessageImg.downloadAndSetImage(mMessage.file_url)
        }
    }

    private fun drawMessageText(holder: TextMessageHolder, mMessage: MessageView) {

        if (mMessage.from == UID) {
            holder.chatUserMessageParent.visibility = View.VISIBLE
            holder.chatReceiverMessageParent.visibility = View.GONE
            holder.chatUserTextMessage.text = mMessage.text
            holder.chatUserTimeMessage.text = mMessage.timestamp.asTime()
        } else {
            holder.chatUserMessageParent.visibility = View.GONE
            holder.chatReceiverMessageParent.visibility = View.VISIBLE
            holder.chatReceiverTextMessage.text = mMessage.text
            holder.chatReceiverTimeMessage.text = mMessage.timestamp.asTime()
        }
    }

    override fun getItemCount(): Int = listMessagesCache.size

    fun addItemToTop(item: MessageView, onSuccess: () -> Unit) {
        if (!listMessagesCache.contains(item)) {
            listMessagesCache.add(item)
            listMessagesCache.sortBy { it.timestamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }

    fun addItemToBottom(item: MessageView, onSuccess: () -> Unit) {
        if (!listMessagesCache.contains(item)) {
            listMessagesCache.add(item)
            notifyItemInserted(listMessagesCache.size)
        }
        onSuccess()
    }
}


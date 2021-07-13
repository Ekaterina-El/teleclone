package ka.el.teleclone.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.R
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.message_item.view.*

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatViewHolder>() {

    private var listMessagesCache = mutableListOf<CommonModel>()

    class SingleChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // [Text message]
        // Receiver
        var chatReceiverMessageParent: LinearLayout = view.chat_receiver_message_parent
        var chatReceiverTextMessage: TextView = view.chat_receiver_text_message
        var chatReceiverTimeMessage: TextView = view.chat_receiver_time_message

        // User
        var chatUserMessageParent: LinearLayout = view.chat_user_message_parent
        var chatUserTextMessage: TextView = view.chat_user_text_message
        var chatUserTimeMessage: TextView = view.chat_user_time_message


        // [Image message]
        // Receiver
        var chatReceiverImageMessageParent: LinearLayout = view.chat_receiver_image_message_parent
        var chatReceiverImageMessageImg: ImageView = view.chat_receiver_image_message_img
        var chatReceiverTimeImageMessage: TextView = view.chat_receiver_time_image_message

        // User
        var chatUserImageMessageParent: LinearLayout = view.chat_user_image_message_parent
        var chatUserImageMessageImg: ImageView = view.chat_user_image_message_img
        var chatUserTimeImageMessage: TextView = view.chat_user_time_image_message

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SingleChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatViewHolder, position: Int) {
        val mMessage = listMessagesCache[position]

        when (mMessage.type) {
            TYPE_MESSAGE_TEXT -> drawMessageText(holder, mMessage)
            TYPE_MESSAGE_IMAGE -> drawMessageImage(holder, mMessage)
        }


    }

    private fun drawMessageImage(holder: SingleChatViewHolder, mMessage: CommonModel) {
        holder.chatReceiverMessageParent.visibility = View.GONE
        holder.chatUserMessageParent.visibility = View.GONE

        if (mMessage.from == UID) {
            holder.chatUserImageMessageParent.visibility = View.VISIBLE
            holder.chatReceiverImageMessageParent.visibility = View.GONE
            holder.chatUserTimeImageMessage.text = mMessage.timestamp.toString().asTime()
            holder.chatUserImageMessageImg.downloadAndSetImage(mMessage.image_url)
        } else {
            holder.chatUserImageMessageParent.visibility = View.GONE
            holder.chatReceiverImageMessageParent.visibility = View.VISIBLE
            holder.chatReceiverTimeImageMessage.text = mMessage.timestamp.toString().asTime()
            holder.chatReceiverImageMessageImg.downloadAndSetImage(mMessage.image_url)
        }
    }

    private fun drawMessageText(holder: SingleChatViewHolder, mMessage: CommonModel) {
        holder.chatReceiverImageMessageParent.visibility = View.GONE
        holder.chatUserImageMessageParent.visibility = View.GONE


        if (mMessage.from == UID) {
            holder.chatUserMessageParent.visibility = View.VISIBLE
            holder.chatReceiverMessageParent.visibility = View.GONE
            holder.chatUserTextMessage.text = mMessage.text
            holder.chatUserTimeMessage.text = mMessage.timestamp.toString().asTime()
        } else {
            holder.chatUserMessageParent.visibility = View.GONE
            holder.chatReceiverMessageParent.visibility = View.VISIBLE
            holder.chatReceiverTextMessage.text = mMessage.text
            holder.chatReceiverTimeMessage.text = mMessage.timestamp.toString().asTime()
        }
    }

    override fun getItemCount(): Int = listMessagesCache.size

    fun addItemToTop(item: CommonModel, onSuccess: () -> Unit) {
        if (!listMessagesCache.contains(item)) {
            listMessagesCache.add(item)
            listMessagesCache.sortBy { it.timestamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }

    fun addItemToBottom(item: CommonModel, onSuccess: () -> Unit) {
        if (!listMessagesCache.contains(item)) {
            listMessagesCache.add(item)
            notifyItemInserted(listMessagesCache.size)
        }
        onSuccess()
    }
}


package ka.el.teleclone.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.R
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.utils.UID
import ka.el.teleclone.utils.asTime
import kotlinx.android.synthetic.main.message_item.view.*

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatViewHolder>() {

    private var listMessagesCache = mutableListOf<CommonModel>()

    class SingleChatViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var chatReceiverMessageParent: LinearLayout = view.chat_receiver_message_parent
        var chatReceiverTextMessage: TextView = view.chat_receiver_text_message
        var chatReceiverTimeMessage: TextView = view.chat_receiver_time_message

        var chatUserMessageParent: LinearLayout = view.chat_user_message_parent
        var chatUserTextMessage: TextView = view.chat_user_text_message
        var chatUserTimeMessage: TextView = view.chat_user_time_message
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

    fun addItem(item: CommonModel, toBottom: Boolean, onSuccess: () -> Unit) {
        if (!listMessagesCache.contains(item)) {
            listMessagesCache.add(item)
            if (toBottom) {
                notifyItemInserted(listMessagesCache.size)
            } else {
                listMessagesCache.sortBy { it.timestamp.toString() }
                notifyItemInserted(0)
            }
        }
        onSuccess()
    }
}


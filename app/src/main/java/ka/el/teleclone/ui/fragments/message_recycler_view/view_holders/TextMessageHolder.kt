package ka.el.teleclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.database.UID
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView
import ka.el.teleclone.utils.asTime
import kotlinx.android.synthetic.main.message_item_text.view.*

class TextMessageHolder(view: View): RecyclerView.ViewHolder(view) {
    // Receiver
    var chatReceiverMessageParent: LinearLayout = view.chat_receiver_message_parent
    var chatReceiverTextMessage: TextView = view.chat_receiver_text_message
    var chatReceiverTimeMessage: TextView = view.chat_receiver_time_message

    // User
    var chatUserMessageParent: LinearLayout = view.chat_user_message_parent
    var chatUserTextMessage: TextView = view.chat_user_text_message
    var chatUserTimeMessage: TextView = view.chat_user_time_message


    fun drawMessageText(holder: TextMessageHolder, mMessage: MessageView) {
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
}
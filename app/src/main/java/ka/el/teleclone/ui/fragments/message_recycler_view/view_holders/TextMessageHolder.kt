package ka.el.teleclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.database.UID
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView
import ka.el.teleclone.utils.asTime
import kotlinx.android.synthetic.main.message_item_text.view.*

class TextMessageHolder(view: View): RecyclerView.ViewHolder(view), MessageHolder {
    // Receiver
    var chatReceiverMessageParent: LinearLayout = view.chat_receiver_message_parent
    var chatReceiverTextMessage: TextView = view.chat_receiver_text_message
    var chatReceiverTimeMessage: TextView = view.chat_receiver_time_message

    // User
    var chatUserMessageParent: LinearLayout = view.chat_user_message_parent
    var chatUserTextMessage: TextView = view.chat_user_text_message
    var chatUserTimeMessage: TextView = view.chat_user_time_message

    override fun drawMessage(message: MessageView) {
        if (message.from == UID) {
            chatUserMessageParent.visibility = View.VISIBLE
            chatReceiverMessageParent.visibility = View.GONE
            chatUserTextMessage.text = message.text
            chatUserTimeMessage.text = message.timestamp.asTime()
        } else {
            chatUserMessageParent.visibility = View.GONE
            chatReceiverMessageParent.visibility = View.VISIBLE
            chatReceiverTextMessage.text = message.text
            chatReceiverTimeMessage.text = message.timestamp.asTime()
        }
    }
}
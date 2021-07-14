package ka.el.teleclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

}
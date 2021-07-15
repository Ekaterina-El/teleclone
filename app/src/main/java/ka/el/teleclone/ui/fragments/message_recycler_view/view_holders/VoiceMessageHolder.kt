package ka.el.teleclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.database.UID
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView
import ka.el.teleclone.utils.asTime
import kotlinx.android.synthetic.main.message_item_image.view.*
import kotlinx.android.synthetic.main.message_item_voice.view.*

class VoiceMessageHolder(view: View): RecyclerView.ViewHolder(view) {

    // Receiver
    var chatReceiverVoiceMessageParent: LinearLayout = view.chat_receiver_voice_message_parent
    var chatReceiverTimeVoiceMessage: TextView = view.chat_receiver_time_voice_message
    var chatReceiverStartVoiceMessage: ImageView = view.chat_receiver_voice_message_start
    var chatReceiverStopVoiceMessage: ImageView = view.chat_receiver_voice_message_stop

    // User
    var chatUserVoiceMessageParent: LinearLayout = view.chat_user_voice_message_parent
    var chatUserTimeVoiceMessage: TextView = view.chat_user_time_voice_message
    var chatUserStartVoiceMessage: ImageView = view.chat_user_voice_message_start
    var chatUserStopVoiceMessage: ImageView = view.chat_user_voice_message_stop

    fun drawMessageVoice(holder: VoiceMessageHolder, mMessage: MessageView) {
        if (mMessage.from == UID) {
            holder.chatUserVoiceMessageParent.visibility = View.VISIBLE
            holder.chatReceiverVoiceMessageParent.visibility = View.GONE
            holder.chatUserTimeVoiceMessage.text = mMessage.timestamp.asTime()
        } else {
            holder.chatUserVoiceMessageParent.visibility = View.GONE
            holder.chatReceiverVoiceMessageParent.visibility = View.VISIBLE
            holder.chatReceiverTimeVoiceMessage.text = mMessage.timestamp.asTime()

        }
    }
}
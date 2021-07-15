package ka.el.teleclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.database.UID
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView
import ka.el.teleclone.utils.AppVoicePlayer
import ka.el.teleclone.utils.asTime
import kotlinx.android.synthetic.main.message_item_voice.view.*

class VoiceMessageHolder(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val mVoicePlayer = AppVoicePlayer()

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

    override fun drawMessage(message: MessageView) {
        if (message.from == UID) {
            chatUserVoiceMessageParent.visibility = View.VISIBLE
            chatReceiverVoiceMessageParent.visibility = View.GONE
            chatUserTimeVoiceMessage.text = message.timestamp.asTime()
        } else {
            chatUserVoiceMessageParent.visibility = View.GONE
            chatReceiverVoiceMessageParent.visibility = View.VISIBLE
            chatReceiverTimeVoiceMessage.text = message.timestamp.asTime()

        }
    }

    override fun onAttach(view: MessageView) {
        mVoicePlayer.init()

        if (view.from == UID) {
            chatUserStartVoiceMessage.visibility = View.VISIBLE
            chatUserStopVoiceMessage.visibility = View.GONE

            chatUserStartVoiceMessage.setOnClickListener {
                chatUserStartVoiceMessage.visibility = View.GONE
                chatUserStopVoiceMessage.visibility = View.VISIBLE

                play(view) {
                    chatUserStartVoiceMessage.visibility = View.VISIBLE
                    chatUserStopVoiceMessage.visibility = View.GONE
                }
            }

            chatUserStopVoiceMessage.setOnClickListener {
                mVoicePlayer.stop {
                    chatUserStartVoiceMessage.visibility = View.VISIBLE
                    chatUserStopVoiceMessage.visibility = View.GONE
                }
            }
        } else {
            chatReceiverStartVoiceMessage.visibility = View.VISIBLE
            chatReceiverStopVoiceMessage.visibility = View.GONE

            chatReceiverStartVoiceMessage.setOnClickListener {
                chatReceiverStartVoiceMessage.visibility = View.GONE
                chatReceiverStopVoiceMessage.visibility = View.VISIBLE

                play(view) {
                    chatReceiverStartVoiceMessage.visibility = View.VISIBLE
                    chatReceiverStopVoiceMessage.visibility = View.GONE
                }
            }

            chatReceiverStopVoiceMessage.setOnClickListener {
                mVoicePlayer.stop {
                    chatReceiverStartVoiceMessage.visibility = View.VISIBLE
                    chatReceiverStopVoiceMessage.visibility = View.GONE
                }
            }
        }

    }

    private fun play(view: MessageView, function: () -> Unit) {
        mVoicePlayer.play(view.id, view.file_url) { function() }
    }

    override fun onDetach() {
        mVoicePlayer.release()
        chatUserStartVoiceMessage.setOnClickListener(null)
        chatUserStopVoiceMessage.setOnClickListener(null)
        chatReceiverStartVoiceMessage.setOnClickListener(null)
        chatReceiverStopVoiceMessage.setOnClickListener(null)
    }
}
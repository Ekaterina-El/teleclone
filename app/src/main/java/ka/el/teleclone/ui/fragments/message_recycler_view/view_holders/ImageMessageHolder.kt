package ka.el.teleclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.database.UID
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView
import ka.el.teleclone.utils.asTime
import ka.el.teleclone.utils.downloadAndSetImage
import kotlinx.android.synthetic.main.message_item_image.view.*

class ImageMessageHolder(view: View): RecyclerView.ViewHolder(view) {
    // Receiver
    var chatReceiverImageMessageParent: LinearLayout = view.chat_receiver_image_message_parent
    var chatReceiverImageMessageImg: ImageView = view.chat_receiver_image_message_img
    var chatReceiverTimeImageMessage: TextView = view.chat_receiver_time_image_message

    // User
    var chatUserImageMessageParent: LinearLayout = view.chat_user_image_message_parent
    var chatUserImageMessageImg: ImageView = view.chat_user_image_message_img
    var chatUserTimeImageMessage: TextView = view.chat_user_time_image_message

    fun drawMessageImage(holder: ImageMessageHolder, mMessage: MessageView) {
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
}
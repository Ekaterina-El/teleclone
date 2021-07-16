package ka.el.teleclone.ui.fragments.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.database.UID
import ka.el.teleclone.database.getFileFromStorage
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView
import ka.el.teleclone.utils.WRITE_FILES
import ka.el.teleclone.utils.asTime
import ka.el.teleclone.utils.checkPermission
import ka.el.teleclone.utils.showToast
import kotlinx.android.synthetic.main.message_item_file.view.*
import java.io.File
import java.lang.Exception

class FileMessageHolder(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    // Receiver
    private val chatReceiverFileMessageParent = view.chat_receiver_file_message_parent
    private val chatReceiverFileMessageBtn = view.chat_receiver_file_message_btn
    private val chatReceiverFileMessageProgress = view.chat_receiver_file_message_progress
    private val chatReceiverFileMessageFilename = view.chat_receiver_file_message_filename
    private val chatReceiverTimeFileMessage = view.chat_receiver_time_file_message

    // User
    private val chatUserFileMessageParent = view.chat_user_file_message_parent
    private val chatUserFileMessageBtn = view.chat_user_file_message_btn
    private val chatUserFileMessageProgress = view.chat_user_file_message_progress
    private val chatUserFileMessageFilename = view.chat_user_file_message_file_name
    private val chatUserTimeFileMessage = view.chat_user_time_file_message

    override fun drawMessage(message: MessageView) {
        if (message.from == UID) {
            chatReceiverFileMessageParent.visibility = View.GONE
            chatUserFileMessageParent.visibility = View.VISIBLE
            chatUserFileMessageProgress.visibility = View.GONE
            chatUserFileMessageFilename.text = message.text
            chatUserTimeFileMessage.text = message.timestamp.asTime()
        } else {
            chatReceiverFileMessageParent.visibility = View.VISIBLE
            chatUserFileMessageParent.visibility = View.GONE
            chatReceiverFileMessageProgress.visibility = View.GONE
            chatReceiverFileMessageBtn.setOnClickListener { downloadFile(message) }
            chatReceiverFileMessageFilename.text = message.text
            chatReceiverTimeFileMessage.text = message.timestamp.asTime()
        }
    }

    private fun downloadFile(message: MessageView) {
        try {
            if (checkPermission(WRITE_FILES)) {
                if (message.from == UID) {
                    chatUserFileMessageProgress.visibility = View.VISIBLE
                    chatUserFileMessageBtn.visibility = View.INVISIBLE
                } else {
                    chatReceiverFileMessageProgress.visibility = View.VISIBLE
                    chatReceiverFileMessageBtn.visibility = View.INVISIBLE
                }
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    message.text
                )
                getFileFromStorage(file, message.file_url) {
                    showToast("Файл \"${message.text}\" загружен в папку \"Загрузки\"")
                    if (message.from == UID) {
                        chatUserFileMessageProgress.visibility = View.INVISIBLE
                        chatUserFileMessageBtn.visibility = View.VISIBLE
                    } else {
                        chatReceiverFileMessageProgress.visibility = View.INVISIBLE
                        chatReceiverFileMessageBtn.visibility = View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {showToast(e.message.toString())}
    }

    override fun onAttach(view: MessageView) {
        if (view.from == UID) {
            chatUserFileMessageBtn.setOnClickListener { downloadFile(view) }
        } else {
            chatReceiverFileMessageBtn.setOnClickListener { downloadFile(view) }
        }
    }

    override fun onDetach() {
        chatUserFileMessageBtn.setOnClickListener(null)
        chatReceiverFileMessageBtn.setOnClickListener(null)
    }

}
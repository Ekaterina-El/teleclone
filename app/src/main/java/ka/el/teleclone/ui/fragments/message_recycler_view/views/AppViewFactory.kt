package ka.el.teleclone.ui.fragments.message_recycler_view.views

import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.utils.TYPE_MESSAGE_IMAGE

class AppViewFactory {
    companion object {
        fun getView(message: CommonModel): MessageView {
            return when (message.type) {
                TYPE_MESSAGE_IMAGE -> ImageMessageView(message.id, message.from, message.timestamp.toString(), message.file_url)
                else -> TextMessageView(message.id, message.from, message.timestamp.toString(), message.file_url, message.text)
            }
        }
    }
}
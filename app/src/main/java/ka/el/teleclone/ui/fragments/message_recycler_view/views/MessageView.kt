package ka.el.teleclone.ui.fragments.message_recycler_view.views

interface MessageView {
    val id: String
    val from: String
    val timestamp: String
    val file_url: String
    val text: String


    companion object {
        val MESSAGE_IMAGE: Int
            get() = 0

        val MESSAGE_TEXT: Int
            get() = 1

        val MESSAGE_VOICE: Int
            get() = 2

        val MESSAGE_FILE: Int
            get() = 3
    }

    fun getTypeView(): Int
}
package ka.el.teleclone.ui.fragments.message_recycler_view.views

data class VoiceMessageView(
    override val id: String,
    override val from: String,
    override val timestamp: String,
    override val file_url: String,
    override val text: String = ""
): MessageView {

    override fun getTypeView(): Int {
        return MessageView.MESSAGE_VOICE
    }

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }

}

package ka.el.teleclone.ui.fragments.message_recycler_view.view_holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.R
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView

class AppHolderFactory {
    companion object {
        fun getHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            return when (viewType) {
                MessageView.MESSAGE_IMAGE -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_image, parent, false)
                    ImageMessageHolder(view)
                }

                MessageView.MESSAGE_VOICE -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_voice, parent, false)
                    VoiceMessageHolder(view)
                }

                else -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_text, parent, false)
                    TextMessageHolder(view)
                }
            }
        }
    }
}
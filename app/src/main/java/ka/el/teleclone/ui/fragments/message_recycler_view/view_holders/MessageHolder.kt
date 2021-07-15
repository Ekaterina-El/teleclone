package ka.el.teleclone.ui.fragments.message_recycler_view.view_holders

import android.view.View
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView

interface MessageHolder {
    fun drawMessage(message: MessageView)
    fun onAttach(view: MessageView)
    fun onDetach()
}
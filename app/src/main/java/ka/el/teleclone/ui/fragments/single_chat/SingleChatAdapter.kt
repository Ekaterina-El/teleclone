package ka.el.teleclone.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ka.el.teleclone.R
import ka.el.teleclone.database.UID
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.ui.fragments.message_recycler_view.view_holders.*
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView
import ka.el.teleclone.utils.*

class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listMessagesCache = mutableListOf<MessageView>()
    private val mVoiceMessagesHolders = mutableListOf<MessageHolder>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).drawMessage(listMessagesCache[position])
    }

    override fun getItemViewType(position: Int): Int {
        return listMessagesCache[position].getTypeView()
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onAttach(listMessagesCache[holder.adapterPosition])
        mVoiceMessagesHolders.add(holder as MessageHolder)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onDetach()
        mVoiceMessagesHolders.remove(holder as MessageHolder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int = listMessagesCache.size

    fun addItemToTop(item: MessageView, onSuccess: () -> Unit) {
        if (!listMessagesCache.contains(item)) {
            listMessagesCache.add(item)
            listMessagesCache.sortBy { it.timestamp }
            notifyItemInserted(0)
        }
        onSuccess()
    }

    fun addItemToBottom(item: MessageView, onSuccess: () -> Unit) {
        if (!listMessagesCache.contains(item)) {
            listMessagesCache.add(item)
            notifyItemInserted(listMessagesCache.size)
        }
        onSuccess()
    }

    fun destroy() {
        mVoiceMessagesHolders.forEach { it.onDetach() }
    }
}


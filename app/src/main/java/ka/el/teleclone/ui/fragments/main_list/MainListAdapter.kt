package ka.el.teleclone.ui.fragments.main_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import ka.el.teleclone.R
import ka.el.teleclone.models.CommonModel
import ka.el.teleclone.ui.fragments.message_recycler_view.views.MessageView
import ka.el.teleclone.utils.downloadAndSetImage
import kotlinx.android.synthetic.main.main_list_item.view.*

class MainListAdapter: RecyclerView.Adapter<MainListAdapter.MainListViewHolder>() {

    private val mListItems = mutableListOf<CommonModel>()


    class MainListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val mainListItemPhoto: CircleImageView = view.main_list_item_photo
        val mainListItemFullName: TextView = view.main_list_item_full_name
        val mainListItemLastMessage: TextView = view.main_list_item_last_message
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return MainListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        val item = mListItems[position]

        holder.mainListItemPhoto.downloadAndSetImage(item.photo_url)
        holder.mainListItemFullName.text = item.full_name
        holder.mainListItemLastMessage.text = item.last_message
    }

    override fun getItemCount(): Int = mListItems.size

    fun updateItem(item: CommonModel) {
        mListItems.add(item)
        this.notifyItemInserted(mListItems.size)
    }
}
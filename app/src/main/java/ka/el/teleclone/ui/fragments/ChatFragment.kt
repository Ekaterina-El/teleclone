package ka.el.teleclone.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ka.el.teleclone.R
import ka.el.teleclone.databinding.FragmentChatBinding

class ChatFragment : Fragment(R.layout.fragment_chat) {

    override fun onResume() {
        super.onResume()

        activity?.title = getString(R.string.app_name)
    }

}
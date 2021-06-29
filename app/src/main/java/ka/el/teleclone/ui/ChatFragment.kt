package ka.el.teleclone.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ka.el.teleclone.R
import ka.el.teleclone.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private lateinit var mBinding: FragmentChatBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentChatBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()

    }

}
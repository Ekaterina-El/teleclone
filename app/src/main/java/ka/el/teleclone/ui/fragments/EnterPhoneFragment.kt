package ka.el.teleclone.ui.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment
import ka.el.teleclone.R
import ka.el.teleclone.utils.showToast
import kotlinx.android.synthetic.main.fragment_enter_phone.*

class EnterPhoneFragment : Fragment(R.layout.fragment_enter_phone) {
    override fun onStart() {
        super.onStart()

        enter_phone_fab_next.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (enter_phone.text.toString().equals("")) {
            showToast(getString(R.string.enter_phone))
        } else {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.registrationDataContainer, EnterCodeFragment())?.commit()
            activity?.title = getString(R.string.verifiPhone)
        }
    }
}
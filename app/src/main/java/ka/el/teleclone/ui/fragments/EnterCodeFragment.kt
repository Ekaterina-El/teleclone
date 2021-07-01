package ka.el.teleclone.ui.fragments

import androidx.fragment.app.Fragment
import ka.el.teleclone.R
import ka.el.teleclone.utils.AppTextWatcher
import ka.el.teleclone.utils.showToast
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment : Fragment(R.layout.fragment_enter_code) {
    override fun onStart() {
        super.onStart()

        enter_code.addTextChangedListener(AppTextWatcher {
            val code = enter_code.text.toString()

            if (code.length == 6) {
                verifiCode()
            }
        })
    }


    private fun verifiCode() {
        showToast("OK")
    }
}
package ka.el.teleclone.ui.fragments

import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.utils.AUTH
import ka.el.teleclone.utils.AppTextWatcher
import ka.el.teleclone.utils.replaceActivity
import ka.el.teleclone.utils.showToast
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(val mPhoneNumber: String, val id: String) : Fragment(R.layout.fragment_enter_code) {
    override fun onStart() {
        super.onStart()

        (activity as RegistrationActivity).title = mPhoneNumber

        enter_code.addTextChangedListener(AppTextWatcher {
            val code = enter_code.text.toString()

            if (code.length == 6) {
                verifiCode()
            }
        })
    }


    private fun verifiCode() {
        val code = enter_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)

        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast(getString(R.string.welcome))
                (activity as RegistrationActivity).replaceActivity(MainActivity())
            } else showToast(task.exception.toString())
        }
    }
}
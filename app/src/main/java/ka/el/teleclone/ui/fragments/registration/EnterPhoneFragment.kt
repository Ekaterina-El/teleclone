package ka.el.teleclone.ui.fragments.registration

import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import ka.el.teleclone.R
import ka.el.teleclone.ui.fragments.BaseFragment
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.fragment_enter_phone.*
import java.util.concurrent.TimeUnit

class EnterPhoneFragment : Fragment(R.layout.fragment_enter_phone) {


    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onResume() {
        super.onResume()

        APP_ACTIVITY.title = getString(R.string.enter_phone)
    }

    override fun onStart() {
        super.onStart()

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast(getString(R.string.welcome))
                        restartActivity()
                    } else showToast(task.exception.toString())
                }
                replaceFragment(
                    EnterCodeFragment(mPhoneNumber, id.toString()),
                    true
                )

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)
                replaceFragment(
                    EnterCodeFragment(mPhoneNumber, id),
                    true
                )
            }

        }

        enter_phone_fab_next.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        mPhoneNumber = enter_phone.text.toString()
        if (mPhoneNumber == "") {
            showToast(getString(R.string.enter_phone))
        } else {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber,
                60,
                TimeUnit.SECONDS,
                APP_ACTIVITY,
                mCallback
            )
        }
    }
}
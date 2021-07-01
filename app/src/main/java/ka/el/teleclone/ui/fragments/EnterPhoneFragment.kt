package ka.el.teleclone.ui.fragments

import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.utils.AUTH
import ka.el.teleclone.utils.replaceActivity
import ka.el.teleclone.utils.replaceFragment
import ka.el.teleclone.utils.showToast
import kotlinx.android.synthetic.main.fragment_enter_phone.*
import java.util.concurrent.TimeUnit

class EnterPhoneFragment : Fragment(R.layout.fragment_enter_phone) {


    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast(getString(R.string.welcome))
                        (activity as RegistrationActivity).replaceActivity(MainActivity())
                    } else showToast(task.exception.toString())
                }
                replaceFragment(R.id.registrationDataContainer, EnterCodeFragment(mPhoneNumber, id.toString()))

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("TAG", p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)
                (activity as RegistrationActivity).replaceFragment(R.id.registrationDataContainer, EnterCodeFragment(mPhoneNumber, id))
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
                (activity as RegistrationActivity),
                mCallback
            )
        }
    }
}
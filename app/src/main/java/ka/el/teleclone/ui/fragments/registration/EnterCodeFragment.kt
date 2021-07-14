package ka.el.teleclone.ui.fragments.registration

import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import ka.el.teleclone.R
import ka.el.teleclone.database.*
import ka.el.teleclone.ui.objects.AppValueEventListener
import ka.el.teleclone.utils.*
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(private val mPhoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = mPhoneNumber
    }

    override fun onStart() {
        super.onStart()

        enter_code.addTextChangedListener(AppTextWatcher {
            val code = enter_code.text.toString()

            if (code.length == 6) {
                verificationCode()
            }
        })
    }


    private fun verificationCode() {
        val code = enter_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)

        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser!!.uid

                val dataUserMap = mutableMapOf<String, Any>()
                dataUserMap[CHILD_ID] = uid
                dataUserMap[CHILD_PHONE_NUMBER] = mPhoneNumber

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).addListenerForSingleValueEvent(AppValueEventListener {
                    if (!it.hasChild(CHILD_USER_NAME)) {
                        dataUserMap[CHILD_USER_NAME] = uid
                    }

                    addPhoneToDatabase(uid, mPhoneNumber) {
                        addUserToDatabase(uid, dataUserMap)
                    }
                })

            } else showToast(task.exception.toString())
        }
    }
}
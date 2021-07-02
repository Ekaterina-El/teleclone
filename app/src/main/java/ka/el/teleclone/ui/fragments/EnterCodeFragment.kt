package ka.el.teleclone.ui.fragments

import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.utils.*
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

        Log.d("verifiCode", "verifiCode")

        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("verifiCode", "is successful")

                val uid = AUTH.currentUser!!.uid

                val dataUserMap = mutableMapOf<String, Any>()
                dataUserMap[CHILD_ID] = uid
                dataUserMap[CHILD_PHONE_NUMBER] = mPhoneNumber
                dataUserMap[CHILD_USER_NAME] = uid


                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataUserMap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("verifiCode", "update successful")

                        showToast(getString(R.string.welcome))
                        (activity as RegistrationActivity).replaceActivity(MainActivity())
                    }
                }
            } else showToast(task.exception.toString())
        }
    }
}
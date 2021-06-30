package ka.el.teleclone.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ka.el.teleclone.R
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment : Fragment(R.layout.fragment_enter_code) {
    override fun onStart() {
        super.onStart()

        enter_code.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifiCode()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun verifiCode() {
        val code = enter_code.text.toString()
        Toast.makeText(activity, "text: ${enter_code.text}", Toast.LENGTH_SHORT).show()

        if (code.length == 6) {
            Toast.makeText(activity, "OK", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "OK")
        }
    }
}
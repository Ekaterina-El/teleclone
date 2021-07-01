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
import ka.el.teleclone.utils.AppTextWatcher
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
//        Toast.makeText(, "OK", Toast.LENGTH_SHORT).show()
        Log.d("TAG", "OK")

    }
}
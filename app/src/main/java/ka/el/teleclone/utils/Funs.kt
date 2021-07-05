package ka.el.teleclone.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import ka.el.teleclone.R

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceFragment(container: Int, fragment: Fragment, addToBackStack:Boolean = true) {
    if (addToBackStack) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(container, fragment)
            .commit()
    } else {
        supportFragmentManager
            .beginTransaction()
            .add(container, fragment)
            .commit()
    }

}

fun Fragment.replaceFragment(container: Int, fragment: Fragment) {
    fragmentManager
        ?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(container, fragment)
        ?.commit()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun  AppCompatActivity.hideKeyboard() {
    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun CircleImageView.downloadAndSetImage(url: String) {
    Log.d("TAG", "Downloading photo")
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.default_photo)
        .into(this)
}

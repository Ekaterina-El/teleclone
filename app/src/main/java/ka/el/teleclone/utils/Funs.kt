package ka.el.teleclone.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
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

fun ImageView.downloadAndSetImage(url: String) {
    val uUrl = if (url == "") "empty" else url
    Picasso.get()
        .load(uUrl)
        .fit()
        .placeholder(R.drawable.default_photo)
        .into(this)
}

fun View.setMargins(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom,
) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        setMargins(left, top, right, bottom)
    }
}

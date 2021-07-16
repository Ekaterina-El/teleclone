package ka.el.teleclone.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import ka.el.teleclone.MainActivity
import ka.el.teleclone.R
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
    if (addToBackStack) {
        APP_ACTIVITY.supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.dataContainer, fragment)
            .commit()
    } else {
        APP_ACTIVITY.supportFragmentManager
            .beginTransaction()
            .add(R.id.dataContainer, fragment)
            .commit()
    }

}

fun restartActivity() {
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()
}

fun AppCompatActivity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

fun String.asTime(): String {
    val date = Date(this.toLong())
    val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormatter.format(date)
}

fun getFileName(uri: Uri): String {
    var result = ""
    val cursor = APP_ACTIVITY.contentResolver.query(uri, null, null, null, null)

    try {
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    } catch (e: Exception) {
        showToast(e.message.toString())
    } finally {
        cursor?.close()
        return result
    }
}
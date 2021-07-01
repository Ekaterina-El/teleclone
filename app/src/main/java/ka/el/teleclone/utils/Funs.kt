package ka.el.teleclone.utils

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
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
            .replace(container, fragment)
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


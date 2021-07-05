package ka.el.teleclone

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.theartofdev.edmodo.cropper.CropImage
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.databinding.ActivityMainBinding
import ka.el.teleclone.models.User
import ka.el.teleclone.ui.fragments.ChatFragment
import ka.el.teleclone.ui.objects.AppDrawer
import ka.el.teleclone.ui.objects.AppValueEventListener
import ka.el.teleclone.utils.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        APP_ACTIVITY = this
        initFirebase()
        initUser {
            initFields()
            initFunc()
        }


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initFunc() {

        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)
            replaceFragment(R.id.dataContainer, ChatFragment(), false)
            mAppDrawer.create()
        } else {
            replaceActivity(RegistrationActivity())
        }

    }


    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
    }
}
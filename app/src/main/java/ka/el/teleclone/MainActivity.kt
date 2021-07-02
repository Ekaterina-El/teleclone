package ka.el.teleclone

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.databinding.ActivityMainBinding
import ka.el.teleclone.ui.fragments.ChatFragment
import ka.el.teleclone.ui.objects.AppDrawer
import ka.el.teleclone.utils.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    private lateinit var mAppDrawer: AppDrawer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
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
        initFirebase()
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
    }

}
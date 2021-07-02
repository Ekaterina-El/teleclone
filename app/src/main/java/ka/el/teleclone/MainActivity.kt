package ka.el.teleclone

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.databinding.ActivityMainBinding
import ka.el.teleclone.ui.fragments.ChatFragment
import ka.el.teleclone.ui.objects.AppDrawer
import ka.el.teleclone.utils.AUTH
import ka.el.teleclone.utils.initFirebase
import ka.el.teleclone.utils.replaceActivity
import ka.el.teleclone.utils.replaceFragment

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
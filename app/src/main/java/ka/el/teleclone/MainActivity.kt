package ka.el.teleclone

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import ka.el.teleclone.activities.RegistrationActivity
import ka.el.teleclone.databinding.ActivityMainBinding
import ka.el.teleclone.ui.fragments.ChatFragment
import ka.el.teleclone.ui.objects.AppDrawer
import java.util.concurrent.RecursiveAction

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
        if (false) {
            setSupportActionBar(mToolbar)
            supportFragmentManager.beginTransaction().replace(R.id.dataContainer, ChatFragment()).commit()
            mAppDrawer.create()
        } else {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }


    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
    }

}
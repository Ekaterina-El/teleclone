package ka.el.teleclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import ka.el.teleclone.R
import ka.el.teleclone.databinding.ActivityRegistrationBinding
import ka.el.teleclone.ui.fragments.ChatFragment
import ka.el.teleclone.ui.fragments.EnterPhoneFragment
import ka.el.teleclone.ui.objects.AppDrawer

class RegistrationActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegistrationBinding
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        title = "Ваш телефон"
        supportFragmentManager.beginTransaction().add(R.id.registrationDataContainer, EnterPhoneFragment()).commit()
    }


    private fun initFields() {
        mToolbar = mBinding.registrationToolbar
    }

}
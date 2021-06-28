package ka.el.teleclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import ka.el.teleclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        createHeader()
        createDrawer()
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(mHeader)
                .withSelectedItem(-1)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        PrimaryDrawerItem()
                                .withIdentifier(100)
                                .withIconTintingEnabled(true)
                                .withName("Создать группу")
                                .withSelectable(false)
                )
                .build()
    }

    private fun createHeader() {
        mHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        ProfileDrawerItem().withName("Name").withEmail("+79802411673")
                )
                .build()
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
    }

}
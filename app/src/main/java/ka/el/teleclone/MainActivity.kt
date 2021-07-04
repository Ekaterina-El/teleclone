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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        APP_ACTIVITY = this
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

        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                USER = it.getValue(User::class.java) ?: User()
            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            && (resultCode == RESULT_OK)
            && (data != null)) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_PHOTO).child(UID)

            path.putFile(uri).addOnCompleteListener {
                if (it.isSuccessful) {
                    path.downloadUrl.addOnCompleteListener { tDownloadUrl ->
                        if (tDownloadUrl.isSuccessful) {
                            val url = tDownloadUrl.result.toString()
                            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PHOTO_URl).setValue(url).addOnCompleteListener { tChangeUserData ->
                                if (tChangeUserData.isSuccessful) {
                                    showToast(getString(R.string.update_data))
                                    USER.photo_url = url
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    fun hideKeyboard() {
        val imm:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}
package ka.el.teleclone.utils

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ka.el.teleclone.models.User
import ka.el.teleclone.ui.objects.AppValueEventListener

/*RealtimeDatabase*/

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference

lateinit var USER: User
lateinit var UID: String

const val NODE_USERS = "users"

const val CHILD_ID = "id"
const val CHILD_PHONE_NUMBER = "phone_number"
const val CHILD_USER_NAME = "user_name"
const val CHILD_FULL_NAME = "full_name"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URl = "photo_url"

const val NODE_USERS_NAME = "users_name"


/* Storage */

lateinit var REF_STORAGE_ROOT: StorageReference
const val FOLDER_PROFILE_PHOTO = "profile_images"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference

    UID = AUTH.currentUser?.uid.toString()
    USER = User()
}


inline fun putUrlToDatabase(photoUrl: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PHOTO_URl)
        .setValue(photoUrl)
        .addOnCompleteListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnCompleteListener { function(it.result.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(User::class.java) ?: User()
            function()
        })
}
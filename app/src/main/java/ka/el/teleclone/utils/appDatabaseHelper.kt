package ka.el.teleclone.utils

import android.net.Uri
import android.provider.ContactsContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ka.el.teleclone.models.CommonModel
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
const val CHILD_STATE = "state"

const val NODE_USERS_NAME = "users_name"

const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"


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
    if (UID != null) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                USER = it.getValue(User::class.java) ?: User()
                function()
            })
    } else {
        function()
    }

}


fun initContacts() {
    if (checkPermission(READ_CONTACTS)) {

        val arrayContacts = arrayListOf<CommonModel>()

        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

        cursor?.let {
            while (cursor.moveToNext()) {
                val full_name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone_number =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                val newModel = CommonModel()
                newModel.full_name = full_name ?: "Error"
                newModel.phone_number = phone_number.replace(Regex("[\\s,-]"), "")

                arrayContacts.add(newModel)
            }
        }

        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }
}

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
        it.children.forEach { snapshot ->
            arrayContacts.forEach { contact ->
                if (snapshot.key == contact.phone_number && snapshot.key != USER.phone_number) {
                    REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(UID)
                        .child(snapshot.value.toString())
                        .child(CHILD_ID)
                        .removeValue()
                        .addOnSuccessListener {
                            REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(UID)
                                .child(snapshot.value.toString())
                                .child(CHILD_ID)
                                .setValue(snapshot.value.toString())
                                .addOnFailureListener { error -> showToast(error.message.toString()) }
                        }
                        .addOnFailureListener { error -> showToast(error.message.toString()) }

                }
            }
        }

    })
}

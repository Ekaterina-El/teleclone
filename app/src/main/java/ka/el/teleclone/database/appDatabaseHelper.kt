package ka.el.teleclone.utils

import android.net.Uri
import android.provider.ContactsContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ka.el.teleclone.R
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

const val NODE_MESSAGES = "messages"
const val CHILD_TEXT = "text"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "timestamp"
const val CHILD_TYPE = "type"
const val CHILD_IMAGE_URL = "image_url"


/* Storage */

lateinit var REF_STORAGE_ROOT: StorageReference
const val FOLDER_PROFILE_PHOTO = "profile_images"
const val FOLDER_MESSAGE_IMAGE = "message_image"

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
        .addOnCompleteListener {
            function()
        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnCompleteListener {
            function(it.result.toString())
        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener {
            function()
        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

inline fun initUser(crossinline function: () -> Unit) {
    if (UID != null) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                USER = it.getValue(User::class.java) ?: User()
                if (USER.id != "" && USER.full_name == "") {
                    USER.full_name = USER.user_name
                }
                function()
            })
    } else {
        function()
    }

}


fun initContacts() {
    if (checkPermission(READ_CONTACTS) && UID != "null") {

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

                            REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(UID)
                                .child(snapshot.value.toString())
                                .child(CHILD_FULL_NAME)
                                .setValue(contact.full_name)
                                .addOnFailureListener { error -> showToast(error.message.toString()) }
                        }
                        .addOnFailureListener { error -> showToast(error.message.toString()) }

                }
            }
        }

    })
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): User = this.getValue(User::class.java) ?: User()


fun sendMessage(message: String, receivingUserId: String, type: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"

    val messageId = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_ID] = messageId.toString()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TYPE] = type
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_TEXT] = message

    val mapDialogs = hashMapOf<String, Any>()
    mapDialogs["$refDialogUser/$messageId"] = mapMessage
    mapDialogs["$refDialogReceivingUser/$messageId"] = mapMessage

    REF_DATABASE_ROOT.updateChildren(mapDialogs)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun changeUserNameDB(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USER_NAME).setValue(newUserName)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                if (newUserName == USER.user_name) {
                    APP_ACTIVITY.supportFragmentManager.popBackStack()
                } else {
                    deleteOldUserName(newUserName)
                }
            } else {
                showToast(it.exception.toString())
            }
        }
}

private fun deleteOldUserName(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS_NAME).child(USER.user_name).removeValue()
        .addOnCompleteListener {
            if (it.isSuccessful) {
                showToast(APP_ACTIVITY.getString(R.string.update_data))
                USER.user_name = newUserName
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            } else {
                showToast(it.exception.toString())
            }
        }
}

fun changeFullName(full_name: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FULL_NAME)
        .setValue(full_name).addOnCompleteListener {
            if (it.isSuccessful) {
                USER.full_name = full_name
                showToast("Данные обновленны")
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }
        }
}

fun saveBio(newBio: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_BIO).setValue(newBio).addOnCompleteListener {
        if (it.isSuccessful) {
            USER.bio = newBio
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        } else showToast(it.exception.toString())
    }
}

fun clearMemory(listenersList: Map<DatabaseReference, AppValueEventListener>) {
    listenersList.forEach {
        it.key.removeEventListener(it.value)
    }
}

@JvmName("clear_memory_for_childEventListener")
fun clearMemory(listenersList: Map<DatabaseReference, ChildEventListener>) {
    listenersList.forEach {
        it.key.removeEventListener(it.value)
    }
}


fun addUserToDatabase(uid: String, dataUserMap: Map<String, Any>) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataUserMap)
        .addOnCompleteListener {
            showToast(APP_ACTIVITY.getString(R.string.welcome))
            restartActivity()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun addPhoneToDatabase(uid: String, phoneNumber: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
        .addOnSuccessListener {
            function()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun sendMessageAsImage(receivingUserId: String, messageId: String, url: Uri?) {
    val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_ID] = messageId
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TYPE] = TYPE_MESSAGE_IMAGE
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_IMAGE_URL] = url.toString()


    val mapDialogs = hashMapOf<String, Any>()
    mapDialogs["$refDialogUser/$messageId"] = mapMessage
    mapDialogs["$refDialogReceivingUser/$messageId"] = mapMessage

    REF_DATABASE_ROOT.updateChildren(mapDialogs)
        .addOnFailureListener { showToast(it.message.toString()) }
}
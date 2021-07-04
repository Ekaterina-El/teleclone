package ka.el.teleclone.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ka.el.teleclone.models.User

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference

lateinit var USER:User
lateinit var UID:String

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference

    UID = AUTH.currentUser?.uid.toString()
    USER = User()
}

const val NODE_USERS = "users"

const val CHILD_ID = "id"
const val CHILD_PHONE_NUMBER = "phone_number"
const val CHILD_USER_NAME = "user_name"
const val CHILD_FULL_NAME = "full_name"
const val CHILD_BIO = "bio"

const val NODE_USERS_NAME = "users_name"


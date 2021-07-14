package ka.el.teleclone.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import ka.el.teleclone.models.User

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
lateinit var REF_STORAGE_ROOT: StorageReference
const val FOLDER_PROFILE_PHOTO = "profile_images"
const val FOLDER_MESSAGES_FILES = "messages_files"
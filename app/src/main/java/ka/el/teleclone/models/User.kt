package ka.el.teleclone.models

data class User(
    val id: String = "",
    var phone_number: String = "",
    var user_name: String = "Inactive user",
    var full_name: String = "",
    var photoURI: String = "",
    var status: String = "",
    var bio: String = "",
)

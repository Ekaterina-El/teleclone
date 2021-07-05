package ka.el.teleclone.models

data class User(
    val id: String = "",
    var phone_number: String = "",
    var user_name: String = "",
    var full_name: String = "Inactive user",
    var photo_url: String = "",
    var state: String = "",
    var bio: String = "",
)

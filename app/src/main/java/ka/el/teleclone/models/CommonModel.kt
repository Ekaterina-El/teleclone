package ka.el.teleclone.models

data class CommonModel(
    val id: String = "",
    var phone_number: String = "",
    var user_name: String = "",
    var full_name: String = "",
    var photo_url: String = "",
    var state: String = "",
    var bio: String = "",

    val text: String = "",
    val from: String = "",
    val timestamp: Any = "",
    val type: String = "",
)

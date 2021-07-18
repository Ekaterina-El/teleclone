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
    val file_url: String = "empty",

    var last_message: String = "",
) {
    override fun equals(other: Any?): Boolean {
        return (other as CommonModel).id == this.id
    }
}

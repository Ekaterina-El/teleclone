package ka.el.teleclone.utils

enum class AppStates(val state: String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает");

    companion object {
        fun updateState(appStates: AppStates) {
            if (UID != "null") {
                REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnCompleteListener { USER.state = appStates.state }
                    .addOnFailureListener { showToast(it.message.toString()) }
            }

        }
    }
}
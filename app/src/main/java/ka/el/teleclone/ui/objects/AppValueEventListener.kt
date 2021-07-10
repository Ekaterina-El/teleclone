package ka.el.teleclone.ui.objects

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ka.el.teleclone.utils.showToast

class AppValueEventListener(val onSuccess: (DataSnapshot) -> Unit) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        onSuccess(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
        showToast(error.message)
    }
}
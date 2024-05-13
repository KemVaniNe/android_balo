package com.example.balo.shareview.forgotpass

import androidx.lifecycle.ViewModel
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.User
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class UpdatePassViewModel : ViewModel() {
    private val db = Firebase.firestore

    fun updatePassword(
        phone: String,
        newPassword: String,
        handleSuccess: () -> Unit,
        handleNotRegister: () -> Unit,
        handleError: (String) -> Unit
    ) {
        db.collection(Collection.USER.collectionName).whereEqualTo(User.PHONE.property, phone).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userId = document.id
                    val hashedPassword = Utils.hashPassword(newPassword)
                    val updateData = hashMapOf(
                        User.PASSWORD.property to hashedPassword
                    )
                    db.collection(Collection.USER.collectionName).document(userId)
                        .update(updateData as Map<String, Any>)
                        .addOnSuccessListener {
                            handleSuccess.invoke()
                        }
                        .addOnFailureListener { e ->
                            handleError.invoke(e.message.toString())
                        }
                    return@addOnSuccessListener
                }
                handleNotRegister.invoke()
            }
            .addOnFailureListener { exception ->
                handleError.invoke(exception.message.toString())
            }
    }
}
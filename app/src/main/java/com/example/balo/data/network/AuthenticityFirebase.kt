package com.example.balo.data.network

import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.User
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AuthenticityFirebase {
    private val db = Firebase.firestore

    fun login(
        phoneNumber: String,
        password: String,
        handleSuccess: (UserEntity) -> Unit,
        handleFail: (String) -> Unit,
    ) {
        db.collection(Collection.USER.collectionName).whereEqualTo(User.PHONE.property, phoneNumber)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val storedPassword = document.getString(User.PASSWORD.property)
                    if (storedPassword != null && Utils.verifyPassword(password, storedPassword)) {
                        handleSuccess.invoke(Utils.convertDocToUser(document))
                        return@addOnSuccessListener
                    }
                }
                handleFail.invoke("Số điện thoại hoặc mật khẩu không chính xác")
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }
}
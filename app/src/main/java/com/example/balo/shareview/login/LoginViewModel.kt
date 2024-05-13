package com.example.balo.shareview.login

import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.User
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class LoginViewModel : ViewModel() {
    private val db = Firebase.firestore

    fun login(
        phoneNumber: String,
        password: String,
        handleSuccess: (UserEntity) -> Unit,
        handleFail: () -> Unit,
        handleError: (String) -> Unit
    ) {
        db.collection(Collection.USER.collectionName).whereEqualTo(User.PHONE.property, phoneNumber)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val storedPassword = document.getString(User.PASSWORD.property)
                    if (storedPassword != null && Utils.verifyPassword(password, storedPassword)) {
                        val userEntity = UserEntity(
                            document.id,
                            document.getString(User.NAME.property) ?: "",
                            document.getString(User.PHONE.property) ?: "",
                            document.getString(User.PASSWORD.property) ?: "",
                            document.getBoolean(User.ROLE.property) ?: false)
                        handleSuccess.invoke(userEntity)
                        return@addOnSuccessListener
                    }
                }
                handleFail.invoke()
            }
            .addOnFailureListener { exception ->
                handleError.invoke(exception.message.toString())
            }
    }
}
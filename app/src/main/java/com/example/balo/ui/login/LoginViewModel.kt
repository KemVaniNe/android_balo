package com.example.balo.ui.login

import androidx.lifecycle.ViewModel
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
        handleSuccess: (Boolean) -> Unit,
        handleFail: () -> Unit,
        handleError: (String) -> Unit
    ) {
        val db = Firebase.firestore
        val usersRef = db.collection(Collection.USER.collectionName)
        usersRef.whereEqualTo(User.PHONE.property, phoneNumber)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val storedPassword = document.getString(User.PASSWORD.property)
                    if (storedPassword != null && Utils.verifyPassword(password, storedPassword)) {
                        if (document.getBoolean(User.ROLE.property) != null) {
                            handleSuccess.invoke(document.getBoolean(User.ROLE.property)!!)
                        }
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
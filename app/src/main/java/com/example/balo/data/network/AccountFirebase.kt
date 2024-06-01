package com.example.balo.data.network

import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.User
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AccountFirebase {
    private val db = Firebase.firestore

    fun updateUser(
        user: UserEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val updateData = Utils.userToMap(user)
        db.collection(Collection.USER.collectionName).document(user.id)
            .update(updateData)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun getUserBaseId(handleSuccess: (UserEntity) -> Unit, handleFail: (String) -> Unit) {
        if (Pref.idUser != Constants.ID_GUEST) {
            db.collection(Collection.USER.collectionName)
                .document(Pref.idUser)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        handleSuccess.invoke(Utils.convertDocToUser(document))
                    } else {
                        handleFail.invoke("Không tìm thấy người dùng!")
                    }
                }
                .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
        }
    }

    fun getAllUser(handleSuccess: (List<UserEntity>) -> Unit, handleFail: (String) -> Unit) {
        val users = mutableListOf<UserEntity>()
        db.collection(Collection.USER.collectionName)
            .whereEqualTo(User.ROLE.property, false)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    users.add(Utils.convertDocToUser(doc))
                }
                handleSuccess.invoke(users)
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }
}
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

    fun register(user: UserEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        isPhoneRegister(
            phone = user.phone,
            isRegister = { isExits ->
                if (isExits) {
                    handleFail.invoke("Số điện thoại này đã tồn tại!")
                } else {
                    createUser(
                        user = user,
                        handleSuccess = { handleSuccess.invoke() },
                        handleFail = { handleFail.invoke(it) }
                    )
                }
            },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun forgetPassword(
        phone: String,
        newPass: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.USER.collectionName).whereEqualTo(User.PHONE.property, phone).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    updatePassword(
                        password = newPass,
                        id = document.id,
                        handleSuccess = { handleSuccess.invoke() },
                        handleFail = { handleFail.invoke(it) }
                    )
                    return@addOnSuccessListener
                }
                handleFail.invoke("SDT này chưa đăng ký tài khoản")
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    private fun updatePassword(
        password: String,
        id: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val hashedPassword = Utils.hashPassword(password)
        val updateData = hashMapOf(
            User.PASSWORD.property to hashedPassword
        )
        db.collection(Collection.USER.collectionName).document(id)
            .update(updateData as Map<String, Any>)
            .addOnSuccessListener {
                handleSuccess.invoke()
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    private fun createUser(
        user: UserEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.userToMap(user)
        db.collection(Collection.USER.collectionName)
            .add(data)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    private fun isPhoneRegister(
        phone: String,
        isRegister: (Boolean) -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.USER.collectionName)
            .whereEqualTo(User.PHONE.property, phone)
            .get()
            .addOnSuccessListener { querySnapshot -> isRegister.invoke(!querySnapshot.isEmpty) }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }
}
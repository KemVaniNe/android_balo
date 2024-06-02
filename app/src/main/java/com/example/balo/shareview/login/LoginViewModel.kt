package com.example.balo.shareview.login

import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.example.balo.data.network.AuthenticityFirebase

class LoginViewModel : ViewModel() {

    private val authenticityFirebase = AuthenticityFirebase()

    fun login(
        phone: String,
        password: String,
        handleSuccess: (UserEntity) -> Unit,
        handleFail: (String) -> Unit
    ) {
        authenticityFirebase.login(
            phoneNumber = phone,
            password = password,
            handleSuccess = { handleSuccess.invoke(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
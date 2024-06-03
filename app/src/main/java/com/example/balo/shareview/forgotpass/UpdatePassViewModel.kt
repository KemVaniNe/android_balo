package com.example.balo.shareview.forgotpass

import androidx.lifecycle.ViewModel
import com.example.balo.data.network.AuthenticityFirebase

class UpdatePassViewModel : ViewModel() {
    private val authenticityFirebase = AuthenticityFirebase()

    fun updatePassword(
        phone: String,
        newPassword: String,
        handleSuccess: () -> Unit,
        handleError: (String) -> Unit
    ) {
        authenticityFirebase.forgetPassword(
            phone = phone,
            newPass = newPassword,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleError.invoke(it) }
        )
    }
}
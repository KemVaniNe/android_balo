package com.example.balo.shareview.register

import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.example.balo.data.network.AuthenticityFirebase


class RegisterViewModel : ViewModel() {
    private val authenticityFirebase = AuthenticityFirebase()

    fun register(
        account: UserEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        authenticityFirebase.register(
            user = account,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
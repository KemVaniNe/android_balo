package com.example.balo.client.clientaccout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.example.balo.data.network.AccountFirebase

class ClientAccountVM : ViewModel() {
    private val _account = MutableLiveData<UserEntity?>(null)
    val account = _account

    private val accountFirebase = AccountFirebase()

    fun updateAccount(handleFail: (String) -> Unit) {
        accountFirebase.getUserBaseId(
            handleSuccess = { _account.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun updateInfo(
        user: UserEntity,
        handleSuccess: () -> Unit,
        handleError: (String) -> Unit
    ) {
        accountFirebase.updateUser(
            user = user,
            handleSuccess = {
                _account.postValue(user)
                handleSuccess.invoke()
            },
            handleFail = { handleError.invoke(it) }
        )
    }
}
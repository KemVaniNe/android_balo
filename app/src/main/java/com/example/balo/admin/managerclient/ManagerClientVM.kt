package com.example.balo.admin.managerclient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.example.balo.data.network.AccountFirebase

class ManagerClientVM : ViewModel() {
    private val _clients = MutableLiveData<List<UserEntity>>(emptyList())
    val clients = _clients

    private val accountFirebase = AccountFirebase()

    fun getClients(handleError: (String) -> Unit) {
        accountFirebase.getAllUser(
            handleSuccess = { _clients.postValue(it) },
            handleFail = { handleError.invoke(it) }
        )
    }
}
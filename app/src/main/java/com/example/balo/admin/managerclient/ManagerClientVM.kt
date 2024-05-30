package com.example.balo.admin.managerclient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BillEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.network.AccountFirebase
import com.example.balo.data.network.BillFirebase

class ManagerClientVM : ViewModel() {
    private val _clients = MutableLiveData<List<UserEntity>>(emptyList())
    val clients = _clients

    private val _bills = MutableLiveData<List<BillEntity>>(emptyList())
    val bills = _bills

    var totalPrice = "0"

    private val accountFirebase = AccountFirebase()

    private val billFirebase = BillFirebase()

    fun getClients(handleError: (String) -> Unit) {
        accountFirebase.getAllUser(
            handleSuccess = { _clients.postValue(it) },
            handleFail = { handleError.invoke(it) }
        )
    }

    fun getBillsBaseUser(id: String, handleFail: (String) -> Unit) {
        billFirebase.getBillBaseUser(
            id = id,
            handleSuccess = {
                totalPrice = it.second
                _bills.postValue(it.first)
            },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
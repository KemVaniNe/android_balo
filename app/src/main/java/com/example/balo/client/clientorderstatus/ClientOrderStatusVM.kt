package com.example.balo.client.clientorderstatus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.network.OrderFirebase

class ClientOrderStatusVM : ViewModel() {

    private val _orders = MutableLiveData<List<OrderEntity>>()
    val orders = _orders

    private val orderFirebase = OrderFirebase()

    fun getOrders(handleFail: (String) -> Unit) {
        orderFirebase.getOrdersBaseUser(
            handleSuccess = { _orders.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
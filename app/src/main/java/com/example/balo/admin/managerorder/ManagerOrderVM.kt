package com.example.balo.admin.managerorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.network.OrderFirebase

class ManagerOrderVM : ViewModel() {

    private val _orders = MutableLiveData<List<OrderEntity>>()
    val orders = _orders

    private val _detail = MutableLiveData<OrderEntity?>(null)
    val detail = _detail

    private val orderFirebase = OrderFirebase()

    fun getOrders(handleFail: (String) -> Unit) {
        orderFirebase.getAllOrders(
            handleSuccess = { _orders.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getDetail(id: String, handleFail: (String) -> Unit) {
        orderFirebase.getOrderBaseId(
            id = id,
            handleSuccess = { _detail.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun updateOrder(order: OrderEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        orderFirebase.updateOrder(
            order = order,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
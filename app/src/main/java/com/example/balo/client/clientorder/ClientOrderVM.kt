package com.example.balo.client.clientorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.network.AccountFirebase
import com.example.balo.data.network.CartFirebase
import com.example.balo.data.network.OrderFirebase

class ClientOrderVM : ViewModel() {
    private val _account = MutableLiveData<UserEntity?>(null)
    val account = _account

    private val _orderDetailEntity = MutableLiveData<List<OrderDetailEntity>>(emptyList())
    val orderDetail = _orderDetailEntity

    private val cartFirebase = CartFirebase()
    private val accountFirebase = AccountFirebase()
    private val orderFirebase = OrderFirebase()

    fun loadData(order: List<OrderDetailEntity>, handleFail: (String) -> Unit) {
        loadUser(handleFail)
        getCart(order, handleFail)
    }

    fun createOrder(order: OrderEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        orderFirebase.createOrder(
            order = order,
            handleSuccess = { handleSuccess.invoke()},
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun deleteCards(
        ids: List<String>,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = mutableListOf<CartEntity>()
        ids.forEach { data.add(CartEntity(idCart = it)) }
        cartFirebase.deleteCardsBaseId(
            ids = data,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    private fun loadUser(handleFail: (String) -> Unit) {
        accountFirebase.getUserBaseId(
            handleSuccess = { _account.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    private fun getCart(orders: List<OrderDetailEntity>, handleFail: (String) -> Unit) {
        orderFirebase.getOrderDetails(
            orders = orders,
            handleSuccess = { _orderDetailEntity.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
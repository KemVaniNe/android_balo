package com.example.balo.client.clientcart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.CartEntity
import com.example.balo.data.network.CartFirebase

class ClientCartVM : ViewModel() {

    private val _carts = MutableLiveData<List<CartEntity>?>(null)
    val carts = _carts

    private val cartFirebase = CartFirebase()

    fun getCart(idUser: String, handleFail: (String) -> Unit) {
        cartFirebase.getCartsBaseUser(
            idUser = idUser,
            handleSuccess = { _carts.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun deleteCart(ids: List<CartEntity>, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        cartFirebase.deleteCardsBaseId(
            ids = ids,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun updateCart(
        cart: CartEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        cartFirebase.updateCart(
            cart = cart,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
package com.example.balo.client.clientdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.CartEntity
import com.example.balo.data.network.CartFirebase
import com.example.balo.data.network.ProductFirebase

class ClientDetailVM : ViewModel() {

    private val _productCurrent = MutableLiveData<BaloEntity?>(null)
    val productCurrent = _productCurrent

    private val productFirebase = ProductFirebase()
    private val cartFirebase = CartFirebase()

    fun getProducts(id: String, handleFail: (String) -> Unit) {
        productFirebase.getProductBaseId(
            idProduct = id,
            handleSuccess = { _productCurrent.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    private fun createNewCart(
        cart: CartEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        cartFirebase.createNewCart(
            cart = cart,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun createCart(
        cart: CartEntity,
        handleExits: () -> Unit,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit,
        handleFull: () -> Unit,
    ) {
        cartFirebase.isCartProductExits(
            cart = cart,
            handleExits = { handleExits.invoke() },
            handleNotExit = { createNewCart(cart, handleSuccess, handleFail) },
            handleFail = { handleFail.invoke(it) },
            handleFull = { handleFull.invoke() }
        )
    }
}
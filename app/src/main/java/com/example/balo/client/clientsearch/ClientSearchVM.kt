package com.example.balo.client.clientsearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.network.ProductFirebase
import com.example.balo.utils.Constants

class ClientSearchVM : ViewModel() {

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val product = _products

    private val productFirebase = ProductFirebase()

    private var listCurrent = listOf<BaloEntity>()

    fun getProducts(idBrand: String, handleFail: (String) -> Unit) {
        if (idBrand == Constants.ID_BRAND_OTHER) {
            getAllProduct(handleFail)
        } else {
            getProductBaseBrand(idBrand, handleFail)
        }
    }

    fun searchProduct(name: String) {
        val data = mutableListOf<BaloEntity>()
        listCurrent.forEach {
            if (it.name.lowercase().contains(name.lowercase())) {
                data.add(it)
            }
        }
        _products.postValue(data)
    }

    private fun getProductBaseBrand(idBrand: String, handleFail: (String) -> Unit) {
        productFirebase.getProductBaseBrand(
            idBrand = idBrand,
            handleSuccess = {
                _products.postValue(it)
                listCurrent = it
            },
            handleFail = { handleFail(it) }
        )
    }

    private fun getAllProduct(handleFail: (String) -> Unit) {
        productFirebase.getProducts(
            handleSuccess = {
                _products.postValue(it)
                listCurrent = it
            },
            handleFail = { handleFail(it) }
        )
    }
}
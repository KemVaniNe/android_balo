package com.example.balo.admin.managerproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.network.CartFirebase
import com.example.balo.data.network.ProductFirebase
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ManagerProductVM : ViewModel() {
    private val db = Firebase.firestore

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val products = _products

    private val _productCurrent = MutableLiveData<BaloEntity?>(null)
    val productCurrent = _productCurrent

    private val productFirebase = ProductFirebase()

    fun getProducts(id: String, handleFail: (String) -> Unit) {
        productFirebase.getProductBaseId(
            idProduct = id,
            handleSuccess = { _productCurrent.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun deleteProducts(ids: List<String>, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        productFirebase.deleteProducts(
            ids = ids,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getAllProducts(handleFail: (String) -> Unit) {
        productFirebase.getProducts(
            handleSuccess = { _products.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
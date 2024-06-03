package com.example.balo.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BillEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.network.BillFirebase
import com.example.balo.data.network.BrandFirebase
import com.example.balo.data.network.ProductFirebase

class AdminVM : ViewModel() {

    private val _brands = MutableLiveData<List<BrandEntity>?>(null)
    val brands = _brands

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val products = _products

    private val _bills = MutableLiveData<List<BillEntity>>(emptyList())
    val bills = _bills

    private val brandFirebase = BrandFirebase()

    private val productFirebase = ProductFirebase()

    private val billFirebase = BillFirebase()

    fun getAllBrands(handleFail: (String) -> Unit) {
        brandFirebase.getAllBrands(
            handleSuccess = { _brands.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getAllProducts(handleFail: (String) -> Unit) {
        productFirebase.getProducts(
            handleSuccess = { _products.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getBills(handleFail: (String) -> Unit) {
        billFirebase.getBills(
            handleSuccess = { _bills.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
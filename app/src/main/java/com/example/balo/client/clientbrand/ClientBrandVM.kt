package com.example.balo.client.clientbrand

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.network.BrandFirebase
import com.example.balo.data.network.ProductFirebase

class ClientBrandVM : ViewModel() {

    private val _currentBrand = MutableLiveData<BrandEntity?>(null)
    val currentBrand = _currentBrand

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val products = _products

    private val brandFirebase = BrandFirebase()

    private val productFirebase = ProductFirebase()

    fun getBrandById(brandId: String, handleFail: (String) -> Unit) {
        brandFirebase.getBrandsBaseId(
            idBrand = brandId,
            handleSuccess = { _currentBrand.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getProductsBaseBrand(id: String, handleFail: (String) -> Unit) {
        productFirebase.getProductBaseBrand(
            idBrand = id,
            handleSuccess = { _products.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
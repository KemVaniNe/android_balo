package com.example.balo.admin.managerbrand

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.network.BrandFirebase

class ManagerBrandVM : ViewModel() {

    private val _brands = MutableLiveData<List<BrandEntity>?>(null)
    val brands = _brands

    private val _currentBrand = MutableLiveData<BrandEntity?>(null)
    val currentBrand = _currentBrand

    private val brandFirebase = BrandFirebase()
    fun createBrand(
        brand: BrandEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        brandFirebase.createBrand(
            brand = brand,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun updateBrand(
        brand: BrandEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        brandFirebase.updateBrand(
            brand = brand,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getAllBrands(handleFail: (String) -> Unit) {
        brandFirebase.getAllBrands(
            handleSuccess = { _brands.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun deleteBrand(
        documentId: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        brandFirebase.deleteBrand(
            documentId = documentId,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getBrandById(brandId: String, handleFail: (String) -> Unit) {
        brandFirebase.getBrandsBaseId(
            idBrand = brandId,
            handleSuccess = { currentBrand.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun deleteBrands(ids: List<String>, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        brandFirebase.deleteBrands(
            ids = ids,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun resetCurrentBrand() {
        _currentBrand.postValue(null)
    }

}
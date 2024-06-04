package com.example.balo.admin.managerproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.network.BrandFirebase
import com.example.balo.data.network.ProductFirebase
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils

class ManagerProductVM : ViewModel() {

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val products = _products

    private val _productCurrent = MutableLiveData<BaloEntity?>(null)
    val productCurrent = _productCurrent

    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading

    var currentProduct: BaloEntity? = null
    var brandCurrent: BrandEntity? = null

    private val productFirebase = ProductFirebase()

    private val brandFirebase = BrandFirebase()

    fun getProducts(id: String, handleFail: (String) -> Unit) {
        _isLoading.postValue(true)
        productFirebase.getProductBaseId(
            idProduct = id,
            handleSuccess = {
                _productCurrent.postValue(it)
                currentProduct = it
                getBrandById(currentProduct!!.id)
            },
            handleFail = {
                _isLoading.postValue(false)
                handleFail.invoke(it)
            }
        )
    }

    fun deleteProducts(
        products: List<BaloEntity>,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        productFirebase.deleteProducts(
            products = products,
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

    fun createProduct(
        product: BaloEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit
    ) {
        productFirebase.createProduct(
            product = product,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun updateProduct(
        numProductAdd: Int,
        product: BaloEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val totalImport =
            Utils.stringToInt(product.totalImport) + Utils.stringToInt(product.priceImport) * numProductAdd
        val quantity = Utils.stringToInt(product.quantitiy) + numProductAdd
        product.totalImport = totalImport.toString()
        product.quantitiy = quantity.toString()
        productFirebase.updateProduct(
            balo = product,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun deleteProduct(
        documentId: String, handleSuccess: () -> Unit, handleFail: (String) -> Unit
    ) {
        productFirebase.deleteProduct(
            documentId = documentId,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun resetCurrentProduct() {
        currentProduct = null
    }

    private fun getBrandById(id: String) {
        if (id == Constants.ID_BRAND_OTHER) {
            brandCurrent = Utils.otherBrand("")
            _isLoading.postValue(false)
        } else {
            brandFirebase.getBrandsBaseId(
                idBrand = id,
                handleSuccess = {
                    brandCurrent = it
                    _isLoading.postValue(false)
                },
                handleFail = {
                    brandCurrent = Utils.otherBrand("")
                    _isLoading.postValue(false)
                }
            )
        }
    }
}
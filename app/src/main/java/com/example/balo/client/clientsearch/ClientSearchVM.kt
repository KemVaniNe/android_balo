package com.example.balo.client.clientsearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.network.BrandFirebase
import com.example.balo.data.network.ProductFirebase
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils

class ClientSearchVM : ViewModel() {

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val product = _products

    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading

    private val productFirebase = ProductFirebase()

    private val brandFirebase = BrandFirebase()

    private var listCurrent = listOf<BaloEntity>()

    var listBrand: List<BrandEntity> = emptyList()

    fun searchProduct(name: String, sort: Int, brandId: String) {
        val data = mutableListOf<BaloEntity>()
        listCurrent.forEach {
            if (it.name.lowercase().contains(name.lowercase())) {
                if (brandId == Constants.ID_BRAND_ALL || brandId == it.idBrand) {
                    data.add(it)
                }
            }
        }
        when (sort) {
            Constants.TYPE_NONE -> {
                _products.postValue(data)
            }

            Constants.TYPE_AZ -> {
                _products.postValue(data.sortedBy { it.name })
            }

            Constants.TYPE_ZA -> {
                _products.postValue(data.sortedByDescending { it.name })
            }
        }
    }

    private fun getBrands(handleFail: (String) -> Unit) {
        val data = mutableListOf<BrandEntity>()
        data.add(Utils.brandAll())
        brandFirebase.getAllBrands(
            handleSuccess = {
                data.addAll(it)
                listBrand = data
                _isLoading.postValue(false)
            },
            handleFail = { handleFail.invoke(it) })
    }

//    fun getAllProduct(handleFail: (String) -> Unit) {
//        productFirebase.getProducts(
//            handleSuccess = {
//                _products.postValue(it)
//                listCurrent = it
//            },
//            handleFail = { handleFail(it) })
//    }

    fun getProduct(handleFail: (String) -> Unit) {
        _isLoading.postValue(true)
        productFirebase.getProducts(
            handleSuccess = { list ->
                _products.postValue(list)
                listCurrent = list
                getBrands {
                    handleFail.invoke(it)
                    _isLoading.postValue(false)
                }
            },
            handleFail = { handleFail(it) })
    }
}
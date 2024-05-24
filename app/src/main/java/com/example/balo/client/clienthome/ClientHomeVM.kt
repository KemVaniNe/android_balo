package com.example.balo.client.clienthome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.network.BrandFirebase
import com.example.balo.utils.Banner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClientHomeVM : ViewModel() {

    private val _banners = MutableStateFlow<List<Int>>(emptyList())
    val banner = _banners.asStateFlow()

    private val _brands = MutableLiveData<List<BrandEntity>?>(null)
    val brands = _brands

    init {
        _banners.tryEmit(Banner.banners)
    }

    private val brandFirebase = BrandFirebase()

    fun getAllBrands(handleFail: (String) -> Unit) {
        brandFirebase.getAllBrands(
            handleSuccess = { _brands.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
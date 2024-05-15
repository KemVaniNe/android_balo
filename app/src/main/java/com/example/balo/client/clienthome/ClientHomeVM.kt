package com.example.balo.client.clienthome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Banner
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClientHomeVM : ViewModel() {
    private val _banners = MutableStateFlow<List<Int>>(emptyList())
    val banner = _banners.asStateFlow()

    init {
        _banners.tryEmit(Banner.banners)
    }

    private val _brands = MutableLiveData<List<BrandEntity>?>(null)
    val brands = _brands

    private val db = Firebase.firestore

    fun getAllBrands(handleFail: (String) -> Unit) {
        val data = mutableListOf<BrandEntity>()
        db.collection(Collection.BRAND.collectionName)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    data.add(Utils.convertDocToBrand(document))
                }
                _brands.postValue(data)
            }
            .addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }
}
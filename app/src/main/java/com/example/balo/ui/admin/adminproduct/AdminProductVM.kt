package com.example.balo.ui.admin.adminproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Collection
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class AdminProductVM : ViewModel() {

    private val _brands = MutableLiveData<List<BrandEntity>?>(null)
    val brands = _brands

    private val db = Firebase.firestore

    fun getAllBrands(handleFail: (String) -> Unit) {
        val data = mutableListOf<BrandEntity>()
        db.collection(Collection.BRAND.collectionName)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val brand = BrandEntity(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        des = document.getString("des") ?: "",
                        pic = document.getString("pic") ?: ""
                    )
                    data.add(brand)
                }
                _brands.postValue(data)
            }
            .addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }
}
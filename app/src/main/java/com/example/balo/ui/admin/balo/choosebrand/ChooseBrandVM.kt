package com.example.balo.ui.admin.balo.choosebrand

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ChooseBrandVM : ViewModel() {
    private val db = Firebase.firestore

    private val _brands = MutableLiveData<List<BrandEntity>?>(null)
    val brands = _brands
    fun getAllBrands(id: String, handleFail: (String) -> Unit) {
        val data = mutableListOf<BrandEntity>()
        db.collection(Collection.BRAND.collectionName).get().addOnSuccessListener { result ->
            for (document in result) {
                val brand = BrandEntity(
                    id = document.id,
                    name = document.getString(Brand.NAME.property) ?: "",
                    des = document.getString(Brand.DES.property) ?: "",
                    pic = document.getString(Brand.PIC.property) ?: "",
                    isSelected = id == document.id
                )
                data.add(brand)
            }
            data.add(Utils.otherBrand(id))
            _brands.postValue(data)
        }.addOnFailureListener { exception ->
            data.add(Utils.otherBrand(id))
            _brands.postValue(data)
            handleFail(exception.message.toString())
        }
    }

}
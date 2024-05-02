package com.example.balo.ui.admin.adminbrand

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Collection
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class AdminBrandVM : ViewModel() {

    private val _brands = MutableLiveData<List<BrandEntity>?>(null)
    val brands = _brands

    private val db = Firebase.firestore
    fun createBrand(
        brand: BrandEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = hashMapOf(
            Brand.NAME.property to brand.name,
            Brand.DES.property to if (brand.des == "") "Không có!" else brand.des,
            Brand.PIC.property to brand.pic
        )
        db.collection(Collection.BRAND.collectionName).add(data)
            .addOnSuccessListener {
                handleSuccess.invoke()
            }
            .addOnFailureListener { e ->
                handleFail.invoke(e.message.toString())
            }
    }

    fun updateBrand(
        brand: BrandEntity,
        idDocument: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = hashMapOf(
            Brand.NAME.property to brand.name,
            Brand.DES.property to if (brand.des == "") "Không có!" else brand.des,
            Brand.PIC.property to brand.pic
        )

        db.collection(Collection.BRAND.collectionName).document(idDocument)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { handleSuccess() }
            .addOnFailureListener { e -> handleFail(e.message.toString()) }
    }

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
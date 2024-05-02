package com.example.balo.ui.admin.adminbrand

import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.CategoryEntity
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Category
import com.example.balo.data.model.enum.Collection
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AdminBrandVM : ViewModel() {
    private val db = Firebase.firestore
    fun createBrand(
        brand: BrandEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = hashMapOf(
            Brand.NAME.property to brand.name,
            Brand.DES.property to brand.des,
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
}
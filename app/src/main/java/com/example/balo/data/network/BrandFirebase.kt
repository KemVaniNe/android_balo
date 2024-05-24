package com.example.balo.data.network

import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class BrandFirebase {
    private val db = Firebase.firestore

    fun getAllBrands(handleSuccess: (List<BrandEntity>) -> Unit, handleFail: (String) -> Unit) {
        val data = mutableListOf<BrandEntity>()
        db.collection(Collection.BRAND.collectionName)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    data.add(Utils.convertDocToBrand(document))
                }
                handleSuccess.invoke(data)
            }
            .addOnFailureListener { exception -> handleFail(exception.message.toString()) }
    }

    fun getBrandsBaseId(
        idBrand: String,
        handleSuccess: (BrandEntity) -> Unit,
        handleNotExits: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.BRAND.collectionName).document(idBrand)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    handleSuccess.invoke(Utils.convertDocToBrand(document))
                } else {
                    handleNotExits.invoke()
                }
            }.addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }
}
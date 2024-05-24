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
}
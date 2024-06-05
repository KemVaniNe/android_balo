package com.example.balo.data.network

import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
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
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun getBrandsBaseId(
        idBrand: String,
        handleSuccess: (BrandEntity) -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.BRAND.collectionName).document(idBrand)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    handleSuccess.invoke(Utils.convertDocToBrand(document))
                } else {
                    handleFail.invoke("Không tìm thấy thương hiệu này!")
                }
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun createBrand(
        brand: BrandEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.brandToMap(brand)
        db.collection(Collection.BRAND.collectionName)
            .add(data)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun updateBrand(
        brand: BrandEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.brandToMap(brand)
        db.collection(Collection.BRAND.collectionName)
            .document(brand.id)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { handleSuccess() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun deleteBrands(ids: List<String>, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        val batch = db.batch()
        for (id in ids) {
            val docRef = db.collection(Collection.BRAND.collectionName).document(id)
            batch.delete(docRef)
        }
        batch.commit()
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun deleteBrand(
        documentId: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.BRAND.collectionName)
            .document(documentId)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handleSuccess.invoke()
                } else {
                    handleFail.invoke("ERROR: ${task.exception?.message ?: "Unknown error occurred"}")
                }
            }
    }
}
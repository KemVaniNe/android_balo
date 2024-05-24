package com.example.balo.data.network

import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ProductFirebase {
    private val db = Firebase.firestore

    fun getProductBaseId(
        idProduct: String,
        handleSuccess: (BaloEntity) -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.BALO.collectionName)
            .document(idProduct)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    handleSuccess.invoke(Utils.convertDocToBProduct(document))
                } else {
                    handleFail.invoke("Không tìm thấy sản phẩm này!")
                }
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun getProductBaseBrand(
        idBrand: String,
        handleSuccess: (List<BaloEntity>) -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = mutableListOf<BaloEntity>()
        db.collection(Collection.BALO.collectionName)
            .whereEqualTo(Balo.ID_BRAND.property, idBrand)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    data.add(Utils.convertDocToBProduct(document))
                }
                handleSuccess.invoke(data)
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun getProducts(handleSuccess: (List<BaloEntity>) -> Unit, handleFail: (String) -> Unit) {
        val data = mutableListOf<BaloEntity>()
        db.collection(Collection.BALO.collectionName)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    data.add(Utils.convertDocToBProduct(document))
                }
                handleSuccess.invoke(data)
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }
}
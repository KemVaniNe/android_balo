package com.example.balo.data.network

import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ProductFirebase {
    private val db = Firebase.firestore

    fun updateProduct(
        balo: BaloEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.productToMap(balo)
        db.collection(Collection.BALO.collectionName).document(balo.id)
            .update(data)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }

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

    fun updateSellProduct(
        updateSell: Map<String, Any>,
        idProduct: String,
        handleFail: (String) -> Unit
    ): Task<Void> {
        return db.collection(Collection.BALO.collectionName)
            .document(idProduct)
            .update(updateSell)
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun getProductNoneSell(
        handleSuccess: (List<BaloEntity>) -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = mutableListOf<BaloEntity>()
        db.collection(Collection.BALO.collectionName)
            .whereEqualTo(Balo.SELL.property, "0")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    data.add(Utils.convertDocToBProduct(document))
                }
                handleSuccess.invoke(data)
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }
}
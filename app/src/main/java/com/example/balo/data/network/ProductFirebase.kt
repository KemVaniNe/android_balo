package com.example.balo.data.network

import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class ProductFirebase {
    private val db = Firebase.firestore

    fun updateProduct(
        balo: BaloEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.productToMap(balo)
        db.collection(Collection.BALO.collectionName)
            .document(balo.id)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { handleSuccess() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
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
            .whereEqualTo(Balo.ISSELL.property, true)
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

    fun deleteProducts(
        products: List<BaloEntity>,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val batch = db.batch()
        for (product in products) {
            val docRef = db.collection(Collection.BALO.collectionName).document(product.id)
            product.isSell = false
            batch.update(docRef, Utils.productToMap(product))
        }

        batch.commit()
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun createProduct(
        product: BaloEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.productToMap(product)
        db.collection(Collection.BALO.collectionName)
            .add(data)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun deleteProduct(
        documentId: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.BALO.collectionName)
            .document(documentId).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handleSuccess.invoke()
                } else {
                    handleFail.invoke("ERROR: ${task.exception?.message ?: "Unknown error occurred"}")
                }
            }
    }
}
package com.example.balo.admin.managerproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ManagerProductVM : ViewModel() {
    private val db = Firebase.firestore

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val products = _products

    fun getAllProducts(handleFail: (String) -> Unit) {
        val data = mutableListOf<BaloEntity>()
        db.collection(Collection.BALO.collectionName).get().addOnSuccessListener { result ->
            for (document in result) {
                data.add(Utils.convertDocToBProduct(document))
            }
            _products.postValue(data)
        }.addOnFailureListener { exception ->
            handleFail(exception.message.toString())
        }
    }

    fun deleteProducts(ids: List<String>, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        val batch = db.batch()
        for (id in ids) {
            val docRef = db.collection(Collection.BALO.collectionName).document(id)
            batch.delete(docRef)
        }
        batch.commit().addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke(e.message ?: "Unknown error occurred") }
    }

}
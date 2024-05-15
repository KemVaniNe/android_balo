package com.example.balo.admin.managerproduct.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class AdminDetailProductVM : ViewModel() {

    private val db = Firebase.firestore

    var currentProduct: BaloEntity? = null

    var brandCurrent: BrandEntity? = null

    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading
    fun getBaloById(id: String, handleFail: (String) -> Unit) {
        _isLoading.postValue(true)
        db.collection(Collection.BALO.collectionName).document(id).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    currentProduct = Utils.convertDocToBProduct(document)
                    getBrandById(currentProduct!!.id)
                } else {
                    handleFail.invoke("Document with ID $id does not exist")
                }
            }.addOnFailureListener { exception ->
                handleFail.invoke(exception.message ?: "Unknown error occurred")
            }
    }

    private fun getBrandById(brandId: String) {
        if (brandId == Constants.ID_BRAND_OTHER) {
            brandCurrent = Utils.otherBrand("")
            _isLoading.postValue(false)
        } else {
            db.collection(Collection.BRAND.collectionName).document(brandId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        brandCurrent = Utils.convertDocToBrand(document)
                        _isLoading.postValue(false)
                    } else {
                        brandCurrent = Utils.otherBrand("")
                        _isLoading.postValue(false)
                    }
                }.addOnFailureListener {
                    brandCurrent = Utils.otherBrand("")
                    _isLoading.postValue(false)
                }
        }
    }

    fun createProduct(
        product: BaloEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit
    ) {
        val data = Utils.productToMap(product)
        db.collection(Collection.BALO.collectionName).add(data).addOnSuccessListener {
            handleSuccess.invoke()
        }.addOnFailureListener { e ->
            handleFail.invoke(e.message.toString())
        }
    }

    fun updateProduct(
        product: BaloEntity,
        idDocument: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.productToMap(product)
        db.collection(Collection.BALO.collectionName).document(idDocument)
            .set(data, SetOptions.merge()).addOnSuccessListener { handleSuccess() }
            .addOnFailureListener { e -> handleFail(e.message.toString()) }
    }

    fun deleteProduct(
        documentId: String, handleSuccess: () -> Unit, handleFail: (String) -> Unit
    ) {
        db.collection(Collection.BALO.collectionName).document(documentId).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handleSuccess.invoke()
                } else {
                    handleFail.invoke(task.exception?.message ?: "Unknown error occurred")
                }
            }
    }

    fun resetCurrentProduct() {
        currentProduct = null
    }
}
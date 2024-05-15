package com.example.balo.client.clientdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ClientDetailVM : ViewModel() {

    private val _productCurrent = MutableLiveData<BaloEntity?>(null)
    val productCurrent = _productCurrent

    private val db = Firebase.firestore

    fun getProducts(idBalo: String, handleFail: (String) -> Unit) {
        db.collection(Collection.BALO.collectionName).document(idBalo)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    _productCurrent.postValue(Utils.convertDocToBProduct(document))
                } else {
                    handleFail("EROR: Product not exists")
                }
            }.addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }
}
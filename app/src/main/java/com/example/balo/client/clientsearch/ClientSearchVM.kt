package com.example.balo.client.clientsearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ClientSearchVM : ViewModel() {

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val product = _products

    private val db = Firebase.firestore

    private var listCurrent = listOf<BaloEntity>()

    fun getProducts(idBrand: String, handleFail: (String) -> Unit) {
        db.collection(Collection.BRAND.collectionName).document(idBrand)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    getProductBaseBrand(idBrand, handleFail)
                } else {
                    getAllBrand(handleFail)
                }
            }.addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }

    fun searchProduct(name: String) {
        val data = mutableListOf<BaloEntity>()
        listCurrent.forEach {
            if (it.name.lowercase().contains(name.lowercase())) {
                data.add(it)
            }
        }
        _products.postValue(data)
    }

    private fun getProductBaseBrand(idBrand: String, handleFail: (String) -> Unit) {
        val data = mutableListOf<BaloEntity>()
        db.collection(Collection.BALO.collectionName).whereEqualTo(Balo.ID_BRAND.property, idBrand)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    data.add(Utils.convertDocToBProduct(document))
                }
                _products.postValue(data)
                listCurrent = data
            }.addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }

    fun getAllBrand(handleFail: (String) -> Unit) {
        val data = mutableListOf<BaloEntity>()
        db.collection(Collection.BALO.collectionName)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    data.add(Utils.convertDocToBProduct(document))
                }
                _products.postValue(data)
                listCurrent = data
            }.addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }
}
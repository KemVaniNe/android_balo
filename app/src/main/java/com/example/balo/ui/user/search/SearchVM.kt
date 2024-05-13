package com.example.balo.ui.user.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.User
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore

class SearchVM : ViewModel() {

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val product = _products

    private val db = Firebase.firestore

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

    private fun getProductBaseBrand(idBrand: String, handleFail: (String) -> Unit) {
        val data = mutableListOf<BaloEntity>()
        db.collection(Collection.BALO.collectionName).whereEqualTo(Balo.ID_BRAND.property, idBrand)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    data.add(convertDocToBProduct(document))
                }
                _products.postValue(data)
            }.addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }

    private fun getAllBrand(handleFail: (String) -> Unit) {
        val data = mutableListOf<BaloEntity>()
        db.collection(Collection.BALO.collectionName)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    data.add(convertDocToBProduct(document))
                }
                _products.postValue(data)
            }.addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }

    private fun convertDocToBProduct(document: QueryDocumentSnapshot): BaloEntity {
        return BaloEntity(
            id = document.id,
            name = document.getString(Balo.NAME.property) ?: "",
            idBrand = document.getString(Balo.ID_BRAND.property) ?: "",
            priceSell = document.getString(Balo.PRICESELL.property) ?: "",
            priceImport = document.getString(Balo.PRICEINPUT.property) ?: "",
            des = document.getString(Balo.DES.property) ?: "",
            pic = document.getString(Balo.PIC.property) ?: "",
            sell = document.getString(Balo.SELL.property) ?: "",
            quantitiy = document.getString(Balo.QUANTITY.property) ?: "",
        )
    }
}
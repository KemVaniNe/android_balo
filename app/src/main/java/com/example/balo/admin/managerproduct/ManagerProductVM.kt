package com.example.balo.admin.managerproduct

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

class ManagerProductVM : ViewModel() {
    private val db = Firebase.firestore

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val products = _products

    fun getAllProducts(handleFail: (String) -> Unit) {
        val data = mutableListOf<BaloEntity>()
        db.collection(Collection.BALO.collectionName).get().addOnSuccessListener { result ->
            for (document in result) {
                val product = BaloEntity(
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
                data.add(product)
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
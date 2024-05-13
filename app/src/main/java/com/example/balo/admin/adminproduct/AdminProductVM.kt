package com.example.balo.admin.adminproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Collection
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class AdminProductVM : ViewModel() {

    private val _brands = MutableLiveData<List<BrandEntity>?>(null)
    val brands = _brands

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val products = _products

    private val db = Firebase.firestore

    fun getAllBrands(handleFail: (String) -> Unit) {
        val data = mutableListOf<BrandEntity>()
        db.collection(Collection.BRAND.collectionName)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val brand = BrandEntity(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        des = document.getString("des") ?: "",
                        pic = document.getString("pic") ?: ""
                    )
                    data.add(brand)
                }
                _brands.postValue(data)
            }
            .addOnFailureListener { exception ->
                handleFail(exception.message.toString())
            }
    }

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
}
package com.example.balo.ui.admin.managerproduct.detail

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
                    val product = BaloEntity(
                        id = document.id,
                        name = document.getString(Balo.NAME.property) ?: "",
                        idBrand = document.getString(Balo.ID_BRAND.property)
                            ?: Constants.ID_BRAND_OTHER,
                        priceSell = document.getString(Balo.PRICESELL.property) ?: "",
                        priceImport = document.getString(Balo.PRICEINPUT.property) ?: "",
                        des = document.getString(Balo.DES.property) ?: "",
                        pic = document.getString(Balo.PIC.property) ?: "",
                        sell = document.getString(Balo.SELL.property) ?: "",
                        quantitiy = document.getString(Balo.QUANTITY.property) ?: "",
                    )
                    currentProduct = product
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
                        val brand = BrandEntity(
                            id = document.id,
                            name = document.getString(Brand.NAME.property) ?: "",
                            des = document.getString(Brand.DES.property) ?: "",
                            pic = document.getString(Brand.PIC.property) ?: ""
                        )
                        brandCurrent = brand
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
        val data = hashMapOf(
            Balo.NAME.property to product.name,
            Balo.ID_BRAND.property to product.idBrand,
            Balo.PRICESELL.property to product.priceSell,
            Balo.DES.property to if (product.des == "") "Không có!" else product.des,
            Balo.PIC.property to product.pic,
            Balo.PRICEINPUT.property to product.priceImport,
            Balo.QUANTITY.property to product.quantitiy,
            Balo.SELL.property to "0",
        )
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
        val data = hashMapOf(
            Balo.NAME.property to product.name,
            Balo.ID_BRAND.property to product.idBrand,
            Balo.PRICESELL.property to product.priceSell,
            Balo.DES.property to if (product.des == "") "Không có!" else product.des,
            Balo.PIC.property to product.pic,
            Balo.PRICEINPUT.property to product.priceImport,
            Balo.QUANTITY.property to product.quantitiy,
        )

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
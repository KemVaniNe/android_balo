package com.example.balo.ui.admin.adminbrand

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Collection
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class AdminBrandVM : ViewModel() {

    private val _brands = MutableLiveData<List<BrandEntity>?>(null)
    val brands = _brands

    private val _currentBrand = MutableLiveData<BrandEntity?>(null)
    val currentBrand = _currentBrand

    private val db = Firebase.firestore
    fun createBrand(
        brand: BrandEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = hashMapOf(
            Brand.NAME.property to brand.name,
            Brand.DES.property to if (brand.des == "") "Không có!" else brand.des,
            Brand.PIC.property to brand.pic
        )
        db.collection(Collection.BRAND.collectionName).add(data)
            .addOnSuccessListener {
                handleSuccess.invoke()
            }
            .addOnFailureListener { e ->
                handleFail.invoke(e.message.toString())
            }
    }

    fun updateBrand(
        brand: BrandEntity,
        idDocument: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = hashMapOf(
            Brand.NAME.property to brand.name,
            Brand.DES.property to if (brand.des == "") "Không có!" else brand.des,
            Brand.PIC.property to brand.pic
        )

        db.collection(Collection.BRAND.collectionName).document(idDocument)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { handleSuccess() }
            .addOnFailureListener { e -> handleFail(e.message.toString()) }
    }

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

    fun deleteBrand(
        documentId: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.BRAND.collectionName).document(documentId)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handleSuccess.invoke()
                } else {
                    handleFail.invoke(task.exception?.message ?: "Unknown error occurred")
                }
            }
    }

    fun getBrandById(brandId: String, handleFail: (String) -> Unit) {
        db.collection(Collection.BRAND.collectionName)
            .document(brandId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val brand = BrandEntity(
                        id = documentSnapshot.id,
                        name = documentSnapshot.getString("name") ?: "",
                        des = documentSnapshot.getString("des") ?: "",
                        pic = documentSnapshot.getString("pic") ?: ""
                    )
                    currentBrand.postValue(brand)
                } else {
                    handleFail.invoke("Document with ID $brandId does not exist")
                }
            }
            .addOnFailureListener { exception ->
                handleFail.invoke(exception.message ?: "Unknown error occurred")
            }
    }

    fun deleteBrands(ids: List<String>, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        val batch = db.batch()
        for (id in ids) {
            val docRef = db.collection(Collection.BRAND.collectionName).document(id)
            batch.delete(docRef)
        }
        batch.commit()
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke(e.message ?: "Unknown error occurred") }
    }


    fun resetCurrentBrand() {
        _currentBrand.postValue(null)
    }

}
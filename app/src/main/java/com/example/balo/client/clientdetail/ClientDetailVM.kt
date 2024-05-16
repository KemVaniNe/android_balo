package com.example.balo.client.clientdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.enum.Cart
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

    private fun createNewCart(
        cart: CartEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.cartToMap(cart)
        db.collection(Collection.CART.collectionName).add(data)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }

    fun createCart(
        cart: CartEntity,
        handleExits: () -> Unit,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.CART.collectionName)
            .whereEqualTo(Cart.ID_USER.property, cart.idUser)
            .whereEqualTo(Cart.ID_BALO.property, cart.idBalo)
            .get()
            .addOnSuccessListener { document ->
                if(document.size() > 0) {
                    handleExits.invoke()
                } else {
                    createNewCart(cart, handleSuccess, handleFail)
                }
            }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }
}
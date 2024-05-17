package com.example.balo.client.clientcart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.enum.Cart
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

class ClientCartVM : ViewModel() {

    private val _carts = MutableLiveData<List<CartEntity>?>(null)
    val carts = _carts

    private val db = Firebase.firestore

    fun getCart(idUser: String, handleFail: (String) -> Unit) {
        db.collection(Collection.CART.collectionName)
            .whereEqualTo(Cart.ID_USER.property, idUser)
            .get()
            .addOnSuccessListener { documents ->
                val data = mutableListOf<CartEntity>()
                val tasks = mutableListOf<Task<DocumentSnapshot>>()

                for (doc in documents) {
                    val cart = Utils.convertDocToCart(doc)
                    val task = db.collection(Collection.BALO.collectionName)
                        .document(cart.idBalo)
                        .get()
                        .addOnSuccessListener { docBalo ->
                            val balo = Utils.convertDocToBProduct(docBalo)
                            cart.max = Utils.getAvailableProduct(balo.quantitiy, balo.sell)
                            cart.nameBalo = balo.name
                            cart.price = balo.priceSell
                            cart.pic = balo.pic
                            data.add(cart)
                        }
                        .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }

                    tasks.add(task)
                }

                Tasks.whenAllComplete(tasks).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _carts.postValue(data)
                    } else {
                        handleFail.invoke("Failed to fetch some documents")
                    }
                }
            }.addOnFailureListener { exception -> handleFail.invoke(exception.message.toString()) }
    }
}
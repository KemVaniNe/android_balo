package com.example.balo.data.network

import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.enum.Cart
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class CartFirebase {
    private val db = Firebase.firestore

    private val productFirebase = ProductFirebase()

    fun isCartProductExits(
        cart: CartEntity,
        handleExits: () -> Unit,
        handleNotExit: () -> Unit,
        handleFail: (String) -> Unit,
        handleFull: () -> Unit,
    ) {
        db.collection(Collection.CART.collectionName)
            .whereEqualTo(Cart.ID_USER.property, cart.idUser)
            .get()
            .addOnSuccessListener { document ->
                if (document.size() > Constants.MAX_CART) {
                    handleFull.invoke()
                } else {
                    for (doc in document) {
                        if ((doc.getString(Cart.ID_BALO.property) ?: "") == cart.idBalo) {
                            handleExits.invoke()
                            return@addOnSuccessListener
                        }
                    }
                    handleNotExit.invoke()
                }
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun createNewCart(
        cart: CartEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.cartToMap(cart)
        db.collection(Collection.CART.collectionName).add(data)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun getCartsBaseUser(
        idUser: String,
        handleSuccess: (List<CartEntity>) -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.CART.collectionName)
            .whereEqualTo(Cart.ID_USER.property, idUser)
            .get()
            .addOnSuccessListener { documents ->
                val tasks = documents.map { doc ->
                    handleCartDocument(doc, handleFail)
                }
                Tasks.whenAllComplete(tasks).addOnCompleteListener {
                    if (it.isSuccessful) {
                        handleSuccess(tasks.mapNotNull { task -> task.result })
                    } else {
                        handleFail("Failed to fetch some documents")
                    }
                }
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    private fun handleCartDocument(
        doc: QueryDocumentSnapshot,
        handleFail: (String) -> Unit
    ): Task<CartEntity> {
        val cart = Utils.convertDocToCart(doc)
        val taskCompletionSource = TaskCompletionSource<CartEntity>()

        productFirebase.getProductBaseId(
            idProduct = cart.idBalo,
            handleSuccess = { balo ->
                cart.max = Utils.calculate(balo.quantitiy, balo.sell)
                cart.nameBalo = balo.name
                cart.price = balo.priceSell
                cart.pic = balo.pic
                taskCompletionSource.setResult(cart)
            },
            handleFail = { error ->
                handleFail.invoke(error)
                taskCompletionSource.setException(Exception(error))
            }
        )
        return taskCompletionSource.task
    }

    fun deleteCardsBaseId(
        ids: List<CartEntity>,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val batch = db.batch()
        for (id in ids) {
            val docRef = db.collection(Collection.CART.collectionName).document(id.idCart)
            batch.delete(docRef)
        }
        batch.commit()
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun updateCart(
        cart: CartEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.cartToMap(cart)
        db.collection(Collection.CART.collectionName).document(cart.idCart)
            .set(data, SetOptions.merge()).addOnSuccessListener { handleSuccess() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }
}
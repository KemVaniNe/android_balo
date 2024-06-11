package com.example.balo.data.network

import com.example.balo.data.model.BillEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.Order
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class BillFirebase {
    private val db = Firebase.firestore

    fun getBillBaseUser(
        id: String,
        handleSuccess: (Pair<List<BillEntity>, String>) -> Unit,
        handleFail: (String) -> Unit
    ) {
        val list = mutableListOf<BillEntity>()
        var total = 0.0
        db.collection(Collection.ORDER.collectionName)
            .whereEqualTo(Order.ID_USER.property, id)
            .get()
            .addOnSuccessListener { document ->
                document.forEach {
                    val bill = Utils.convertDocToBill(it)
                    list.add(bill)
                    total += bill.totalPrice
                }
                handleSuccess.invoke(Pair(list, total.toString()))
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun getBills(
        handleSuccess: (List<BillEntity>) -> Unit,
        handleFail: (String) -> Unit
    ) {
        val list = mutableListOf<BillEntity>()
        db.collection(Collection.ORDER.collectionName)
            .get()
            .addOnSuccessListener { document ->
                document.forEach {
                    list.add(Utils.convertDocToBill(it))
                }
                handleSuccess.invoke(list)
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }
}
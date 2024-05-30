package com.example.balo.admin.managerorder.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AdminDetailOrderVM : ViewModel() {
    private val _detail = MutableLiveData<OrderEntity?>(null)
    val detail = _detail

    private val db = Firebase.firestore

    fun getDetail(id: String, handleFail: (String) -> Unit) {
        db.collection(Collection.ORDER.collectionName)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                _detail.postValue(Utils.convertDocToOrder(document))
            }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }

    fun updateOrder(order: OrderEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        val data = Utils.orderToMap(order)
        db.collection(Collection.ORDER.collectionName).document(order.id)
            .update(data)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }

}
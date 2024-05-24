package com.example.balo.admin.managerorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.Order
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ManagerOrderVM : ViewModel() {

    private val _orders = MutableLiveData<List<OrderEntity>>()
    val orders = _orders

    private val db = Firebase.firestore

    fun getOrders(handleFail: (String) -> Unit) {
        val list = mutableListOf<OrderEntity>()
        db.collection(Collection.ORDER.collectionName)
            .get()
            .addOnSuccessListener { document ->
                document.forEach {
                    list.add(Utils.convertDocToOrder(it))
                }
                _orders.postValue(list)
            }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }
}
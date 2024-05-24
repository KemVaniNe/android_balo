package com.example.balo.client.clientorderstatus.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ClientOrderDetailVM : ViewModel() {
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

    fun cancelOrder(id: String, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        db.collection(Collection.ORDER.collectionName)
            .document(id)
            .update(Utils.statusCancelToMap())
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }

    fun updateProduct(order: OrderEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        val tasks = mutableListOf<Task<Void>>()
        order.detail.forEach { detail ->
            val task = db.collection(Collection.BALO.collectionName).document(detail.idBalo)
                .get()
                .continueWithTask { task ->
                    if (task.isSuccessful) {
                        val doc = task.result
                        val product = Utils.convertDocToBProduct(doc)
                        val newSellCount =
                            Utils.stringToInt(product.sell) - Utils.stringToInt(detail.quantity)
                        db.collection(Collection.BALO.collectionName).document(detail.idBalo)
                            .update(Utils.sellToMap(newSellCount.toString()))
                    } else {
                        Tasks.forException(task.exception ?: Exception("Unknown error"))
                    }
                }
                .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }

            tasks.add(task)
        }
        Tasks.whenAllComplete(tasks).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                handleSuccess.invoke()
            } else {
                handleFail.invoke("Failed to update some products")
            }
        }
    }

    fun updateRate(
        order: OrderEntity,
        orderDetailChoose: OrderDetailEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.orderToMap(order)
        db.collection(Collection.ORDER.collectionName).document(order.id)
            .update(data)
            .addOnSuccessListener {
                val commentNew = "${orderDetailChoose.rate}${orderDetailChoose.comment}"
                getBalo(orderDetailChoose.idBalo, commentNew, handleSuccess, handleFail)
            }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }

    private fun getBalo(
        id: String,
        commentNew: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        db.collection(Collection.BALO.collectionName).document(id)
            .get()
            .addOnSuccessListener { doc ->
                val balo = Utils.convertDocToBProduct(doc)
                val oldRate = Utils.stringToInt(balo.rate).toFloat()
                val numOldRate = balo.comment.size
                val rateCurrent = Utils.stringToInt(commentNew.take(1)).toFloat()
                val newRateBalo = (oldRate + rateCurrent) / (numOldRate + 1)
                val newList = mutableListOf<String>()
                newList.addAll(balo.comment)
                newList.add(commentNew)
                balo.comment = newList
                balo.rate = newRateBalo.toString()
                updateBalo(balo, handleSuccess, handleFail)
            }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }

    private fun updateBalo(
        balo: BaloEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = Utils.productToMap(balo)
        db.collection(Collection.BALO.collectionName).document(balo.id)
            .update(data)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke(e.message.toString()) }
    }

}
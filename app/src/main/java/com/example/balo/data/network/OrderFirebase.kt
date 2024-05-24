package com.example.balo.data.network

import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class OrderFirebase {
    private val db = Firebase.firestore

    private val productFirebase = ProductFirebase()

    fun createOrder(order: OrderEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        val data = Utils.orderToMap(order)
        val tasks = mutableListOf<Task<Void>>()
        db.collection(Collection.ORDER.collectionName).add(data)
            .addOnSuccessListener {
                order.detail.forEach { detail ->
                    val newSell =
                        Utils.stringToInt(detail.quantity) + Utils.stringToInt(detail.sell)
                    val updateSell = Utils.sellToMap(newSell.toString())
                    val task = productFirebase.updateSellProduct(
                        updateSell = updateSell,
                        idProduct = detail.idBalo,
                        handleFail = { handleFail.invoke(it) }
                    )
                    tasks.add(task)
                }

                Tasks.whenAllComplete(tasks).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        handleSuccess.invoke()
                    } else {
                        handleFail.invoke("Failed to update some documents")
                    }
                }
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun getOrderDetails(
        orders: List<OrderDetailEntity>,
        handleSuccess: (List<OrderDetailEntity>) -> Unit,
        handleFail: (String) -> Unit
    ) {
        val tasks = orders.map { order ->
            processOrderDetail(order, handleFail)
        }
        Tasks.whenAllComplete(tasks).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val newList = tasks.mapNotNull { it.result }
                handleSuccess(newList)
            } else {
                handleFail("Failed to fetch some documents")
            }
        }
    }

    private fun processOrderDetail(
        order: OrderDetailEntity,
        handleFail: (String) -> Unit
    ): Task<OrderDetailEntity> {
        val taskCompletionSource = TaskCompletionSource<OrderDetailEntity>()
        productFirebase.getProductBaseId(
            idProduct = order.idBalo,
            handleSuccess = { balo ->
                val updatedOrder = OrderDetailEntity(
                    idBalo = order.idBalo,
                    nameBalo = order.nameBalo,
                    quantity = order.quantity,
                    price = order.price,
                    picProduct = balo.pic,
                    sell = balo.sell,
                )
                taskCompletionSource.setResult(updatedOrder)
            },
            handleFail = { error ->
                handleFail(error)
                taskCompletionSource.setException(Exception(error))
            }
        )
        return taskCompletionSource.task
    }
}
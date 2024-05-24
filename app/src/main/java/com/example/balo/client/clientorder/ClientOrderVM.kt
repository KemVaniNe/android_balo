package com.example.balo.client.clientorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.example.balo.utils.Utils.stringToInt
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

class ClientOrderVM : ViewModel() {
    private val _account = MutableLiveData<UserEntity?>(null)
    val account = _account

    private val _orderDetailEntity = MutableLiveData<List<OrderDetailEntity>>(emptyList())
    val orderDetail = _orderDetailEntity

    private val db = Firebase.firestore

    fun loadData(order: List<OrderDetailEntity>, handleFail: (String) -> Unit) {
        loadUser(handleFail)
        getCart(order, handleFail)
    }

    fun createOrder(order: OrderEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        val data = Utils.orderToMap(order)
        val tasks = mutableListOf<Task<Void>>()

        db.collection(Collection.ORDER.collectionName).add(data)
            .addOnSuccessListener {
                order.detail.forEach { detail ->
                    val newSell = stringToInt(detail.quantity) + stringToInt(detail.sell)
                    val updateSell = Utils.sellToMap(newSell.toString())

                    val task = db.collection(Collection.BALO.collectionName)
                        .document(detail.idBalo)
                        .update(updateSell)
                        .addOnFailureListener { e ->
                            handleFail.invoke(e.message.toString())
                        }
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
            .addOnFailureListener { e ->
                handleFail.invoke(e.message.toString())
            }
    }

    fun deleteCards(
        ids: List<String>,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val batch = db.batch()
        for (id in ids) {
            val docRef = db.collection(Collection.CART.collectionName).document(id)
            batch.delete(docRef)
        }
        batch.commit().addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e ->
                handleFail.invoke(
                    e.message ?: "Unknown error occurred"
                )
            }
    }

    fun updateProduct() {

    }

//    fun createOrder(
//        order: OrderEntity,
//        orderDetails: List<OrderDetailEntity>,
//        handleSuccess: () -> Unit,
//        handleFail: (String) -> Unit
//    ) {
//        val tasks = mutableListOf<Task<Void>>()
//        val data = Utils.orderToMap(order)
//        db.collection(Collection.ORDER.collectionName).add(data).addOnSuccessListener { orderRef ->
//            orderDetails.forEach { detail ->
//                val detailData = Utils.orderDetailToMap(detail)
//                val task = db.collection(Collection.ORDER_DETAIL.collectionName).add(detailData)
//                    .continueWithTask { task ->
//                        if (task.isSuccessful) {
//                            Tasks.forResult(null)
//                        } else {
//                            Tasks.forException<Void>(task.exception ?: Exception("Unknown error"))
//                        }
//                    }
//
//                task.addOnFailureListener { e ->
//                    handleFail.invoke(e.message.toString())
//                }
//                tasks.add(task)
//            }
//            Tasks.whenAllComplete(tasks).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    handleSuccess.invoke()
//                } else {
//                    handleFail.invoke("Failed to fetch some documents")
//                }
//            }
//        }.addOnFailureListener { e ->
//            handleFail.invoke(e.message.toString())
//        }
//    }

    private fun loadUser(handleFail: (String) -> Unit) {
        if (Pref.idUser != Constants.ID_GUEST) {
            db.collection(Collection.USER.collectionName).document(Pref.idUser)
                .get()
                .addOnSuccessListener { document ->
                    _account.postValue(Utils.convertDocToUser(document))
                }.addOnFailureListener { exception ->
                    handleFail.invoke(exception.message.toString())
                }
        }
    }

    private fun getCart(orders: List<OrderDetailEntity>, handleFail: (String) -> Unit) {
        val tasks = mutableListOf<Task<DocumentSnapshot>>()
        val newList = mutableListOf<OrderDetailEntity>()
        orders.forEach { order ->
            val task =
                db.collection(Collection.BALO.collectionName).document(order.idBalo).get()
                    .addOnSuccessListener { docBalo ->
                        newList.add(
                            OrderDetailEntity(
                                idBalo = order.idBalo,
                                nameBalo = order.nameBalo,
                                quantity = order.quantity,
                                price = order.price,
                                picProduct = Utils.convertDocToBProduct(docBalo).pic,
                                sell = Utils.convertDocToBProduct(docBalo).sell,
                            )
                        )
                    }
                    .addOnFailureListener {
                        handleFail.invoke(it.message.toString())
                    }
            tasks.add(task)
        }
        Tasks.whenAllComplete(tasks).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                orderDetail.postValue(newList)
            } else {
                handleFail.invoke("Failed to fetch some documents")
            }
        }
    }
}
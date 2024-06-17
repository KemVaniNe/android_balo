package com.example.balo.client.clientnotification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.NotificationEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.network.NotificationFirebase
import com.example.balo.data.network.OrderFirebase
import java.text.SimpleDateFormat
import java.util.Locale

class ClientNotificationVM : ViewModel() {
    private val _notification = MutableLiveData<List<NotificationEntity>>(emptyList())
    val notification = _notification

    private val _order = MutableLiveData<OrderEntity?>(null)
    val order = _order

    private val notificationFirebase = NotificationFirebase()

    private val orderFirebase = OrderFirebase()

    fun getNotificationBaseUser(handleFail: (String) -> Unit) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        notificationFirebase.getNotificationByUser(
            handleSuccess = { list ->
                _notification.postValue(list.sortedByDescending { dateFormat.parse(it.datatime) })
            },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getOrderById(id: String, handleFail: (String) -> Unit) {
        orderFirebase.getOrderBaseId(
            id = id,
            handleSuccess = { _order.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun updateStatusNotification(notification: NotificationEntity, handleFail: (String) -> Unit) {
        notification.isSeen = true
        notificationFirebase.updateNotification(
            notification = notification,
            handleSuccess = {},
            handleFail = { handleFail.invoke(it) }
        )

    }
}
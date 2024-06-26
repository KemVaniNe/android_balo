package com.example.balo.admin.managerorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balo.data.model.NotificationEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.enum.NotificationFromAdmin
import com.example.balo.data.network.NotificationFirebase
import com.example.balo.data.network.OrderFirebase
import com.example.balo.payment.data.ZLRefundResponse
import com.example.balo.payment.networks.ZLPayService
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils
import kotlinx.coroutines.launch

class ManagerOrderVM : ViewModel() {

    private val _orders = MutableLiveData<List<OrderEntity>>()
    val orders = _orders

    private val _detail = MutableLiveData<OrderEntity?>(null)
    val detail = _detail

    private val _responseRefund = MutableLiveData<ZLRefundResponse>(null)
    val responseRefund = _responseRefund

    private val notificationFirebase = NotificationFirebase()

    private val orderFirebase = OrderFirebase()

    private val zlPayService = ZLPayService()

    fun getOrders(handleFail: (String) -> Unit) {
        orderFirebase.getAllOrders(
            handleSuccess = { _orders.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getDetail(id: String, handleFail: (String) -> Unit) {
        orderFirebase.getOrderBaseId(
            id = id,
            handleSuccess = { _detail.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun updateOrder(order: OrderEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        orderFirebase.updateOrder(
            order = order,
            handleSuccess = {
                val notification = when (order.statusOrder) {
                    Constants.ORDER_CONFIRM -> NotificationFromAdmin.accept.value
                    Constants.ORDER_WAIT_SHIP -> NotificationFromAdmin.pack.value
                    Constants.ORDER_SHIP -> NotificationFromAdmin.ship.value
                    Constants.ORDER_COMPLETE -> NotificationFromAdmin.success.value
                    Constants.ORDER_CANCEL -> NotificationFromAdmin.cancel.value
                    else -> ""
                }
                createNotification(
                    notification = NotificationEntity(
                        idUser = order.iduser,
                        idOrder = order.id,
                        notification = notification,
                        datatime = Utils.getCurrentDateTime(),
                        isSeen = false,
                        roleUser = false,
                    ),
                    handleSuccess = { handleSuccess.invoke() },
                    handleFail = { handleFail.invoke(it) }
                )
            },
            handleFail = { handleFail.invoke(it) }
        )
    }

    private fun createNotification(
        notification: NotificationEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        notificationFirebase.createNotification(
            notification = notification,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun refund(
        zpTransId: String,
        amount: Long,
        handleFail: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = zlPayService.getRequestRefund(zpTransId, amount)
                val response = zlPayService.getZLPayApi().refund(request)
                responseRefund.postValue(response)
            } catch (e: Exception) {
                handleFail.invoke("ERROR VM ${e.message}")
            }
        }
    }

    fun cancelOrder(id: String, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        orderFirebase.cancelOrder(
            id = id,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun cancelOrderByUser(
        order: OrderEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        orderFirebase.cancelOrderByUser(
            order = order,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
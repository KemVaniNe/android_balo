package com.example.balo.client.clientorderstatus.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.payment.data.ZLRefundResponse
import com.example.balo.data.network.OrderFirebase
import com.example.balo.data.network.ProductFirebase
import com.example.balo.payment.networks.ZLPayService
import com.example.balo.utils.Utils
import kotlinx.coroutines.launch

class ClientOrderDetailVM : ViewModel() {
    private val _detail = MutableLiveData<OrderEntity?>(null)
    val detail = _detail

    private val _responseRefund = MutableLiveData<ZLRefundResponse>(null)
    val responseRefund = _responseRefund

    private val productFirebase = ProductFirebase()

    private val orderFirebase = OrderFirebase()

    private val zlPayService = ZLPayService()

    fun getDetail(id: String, handleFail: (String) -> Unit) {
        orderFirebase.getOrderBaseId(
            id = id,
            handleSuccess = { _detail.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
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

    fun updateRate(
        order: OrderEntity,
        orderDetailChoose: OrderDetailEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        orderFirebase.updateOrder(
            order = order,
            handleSuccess = {
                val commentNew = "${orderDetailChoose.rate}${orderDetailChoose.comment}"
                getBalo(orderDetailChoose.idBalo, commentNew, handleSuccess, handleFail)
            },
            handleFail = { handleFail.invoke(it) }
        )
    }

    private fun getBalo(
        id: String,
        commentNew: String,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        productFirebase.getProductBaseId(
            idProduct = id,
            handleSuccess = {
                val oldRate = it.rate
                val numOldRate = it.comment.size
                val rateCurrent = Utils.stringToInt(commentNew.take(1)).toFloat()
                val newRateBalo = (oldRate + rateCurrent) / (numOldRate + 1)
                val newList = mutableListOf<String>()
                newList.addAll(it.comment)
                newList.add(commentNew)
                it.comment = newList
                it.rate = newRateBalo
                updateBalo(it, handleSuccess, handleFail)
            },
            handleFail = { handleFail.invoke(it) }
        )
    }

    private fun updateBalo(
        balo: BaloEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        productFirebase.updateProduct(
            balo = balo,
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
}
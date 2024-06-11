package com.example.balo.admin.adminhome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.network.OrderFirebase
import com.example.balo.data.network.ProductFirebase
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils
import com.github.mikephil.charting.data.BarEntry

class AdminHomeVM : ViewModel() {

    private val _entriesBills = MutableLiveData<Pair<List<BarEntry>, List<String>>>()

    val entriesBills = _entriesBills

    private val _products = MutableLiveData<List<BaloEntity>>()
    val products = _products

    private val orderFirebase = OrderFirebase()

    private val productFirebase = ProductFirebase()

    fun getBills(handleFail: (String) -> Unit) {
        orderFirebase.getAllOrders(
            handleSuccess = {
                convertBillToEntries(it)
            },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun getProducts(handleFail: (String) -> Unit) {
        productFirebase.getProducts(
            handleSuccess = { list ->
                _products.postValue(list.sortedByDescending { it.totalSell })
            },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun filter(type: Int) {
        val newList = mutableListOf<BaloEntity>()
        when (type) {
            Constants.TYPE_SELL -> {
                _products.value?.let { list ->
                    newList.addAll(list.sortedByDescending { it.sell })
                }
            }

            Constants.TYPE_PROFIT -> {
                _products.value?.let { list ->
                    newList.addAll(list.sortedByDescending { Utils.getProfit(it) })
                }
            }

            Constants.TYPE_REVENUE -> {
                _products.value?.let { list ->
                    newList.addAll(list.sortedByDescending { it.totalSell })
                }
            }
        }
        _products.postValue(newList)
    }

    private fun convertBillToEntries(bills: List<OrderEntity>) {
        val mapDateToTotalPrice = mutableMapOf<String, Float>()

        bills.forEach {
            if (it.statusOrder != Constants.ORDER_CANCEL) {
                val totalPrice = it.totalPrice.toFloat()
                val date = it.date
                if (mapDateToTotalPrice.containsKey(date)) {
                    mapDateToTotalPrice[date] = mapDateToTotalPrice[date]!! + totalPrice
                } else {
                    mapDateToTotalPrice[date] = totalPrice
                }
            }
        }

        val entries = mutableListOf<BarEntry>()
        val dates = mutableListOf<String>()
        for ((date, totalPrice) in mapDateToTotalPrice) {
            dates.add(date)
            val entry = BarEntry(dates.size - 1.toFloat(), totalPrice)
            entries.add(entry)
        }
        _entriesBills.postValue(Pair(entries, dates))
    }
}
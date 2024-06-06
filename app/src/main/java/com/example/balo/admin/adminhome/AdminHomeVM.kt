package com.example.balo.admin.adminhome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.network.OrderFirebase
import com.example.balo.data.network.ProductFirebase
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils
import com.github.mikephil.charting.data.Entry

class AdminHomeVM : ViewModel() {

    private val _entriesBills = MutableLiveData<Pair<List<Entry>, List<String>>>()
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
                _products.postValue(list.sortedByDescending { Utils.stringToInt(it.totalSell) })
            },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun filter(type: Int) {
        val newList = mutableListOf<BaloEntity>()
        when (type) {
            Constants.TYPE_SELL -> {
                _products.value?.let { list ->
                    newList.addAll(list.sortedByDescending { Utils.stringToInt(it.sell) })
                }
            }

            Constants.TYPE_PROFIT -> {
                _products.value?.let { list ->
                    newList.addAll(list.sortedByDescending { Utils.getProfit(it) })
                }
            }

            Constants.TYPE_REVENUE -> {
                _products.value?.let { list ->
                    newList.addAll(list.sortedByDescending { Utils.stringToInt(it.totalSell) })
                }
            }
        }
        _products.postValue(newList)
    }

    private fun convertBillToEntries(bills: List<OrderEntity>) {
        val mapDateToTotalPrice = mutableMapOf<String, Float>()

        bills.forEach {
            if (it.statusOrder != Constants.ORDER_CANCEL) {
                val totalPrice = Utils.stringToInt(it.totalPrice).toFloat()
                val date = it.date
                if (mapDateToTotalPrice.containsKey(date)) {
                    mapDateToTotalPrice[date] = mapDateToTotalPrice[date]!! + totalPrice
                } else {
                    mapDateToTotalPrice[date] = totalPrice
                }
            }
        }

        val entries = mutableListOf<Entry>()
        val dates = mutableListOf<String>()
        for ((date, totalPrice) in mapDateToTotalPrice) {
            dates.add(date)
            val entry = Entry(dates.size - 1.toFloat(), totalPrice)
            entries.add(entry)
        }
        _entriesBills.postValue(Pair(entries, dates))
    }


    private fun convertProducts(orders: List<OrderEntity>) {
        val productSales = mutableMapOf<String, BaloEntity>()

        orders.forEach { order ->
            order.detail.forEach { detail ->
                val id = detail.idBalo
                val price = Utils.stringToInt(detail.quantity) * Utils.stringToInt(detail.price)

                if (productSales.containsKey(id)) {
                    val balo = productSales[id]!!
                    val totalPrice = Utils.stringToInt(balo.priceSell) + price
                    val quantity = Utils.stringToInt(balo.sell) + Utils.stringToInt(detail.quantity)

                    productSales[id] = BaloEntity(
                        id = balo.id,
                        name = balo.name,
                        priceSell = totalPrice.toString(),
                        sell = quantity.toString()
                    )
                } else {
                    productSales[id] = BaloEntity(
                        id = id,
                        name = detail.nameBalo,
                        priceSell = detail.price,
                        sell = detail.quantity
                    )
                }
            }
        }

        val product =
            productSales.values.toList().sortedByDescending { Utils.stringToInt(it.sell) }

        _products.postValue(product)
    }

}
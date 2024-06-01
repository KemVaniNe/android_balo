package com.example.balo.admin.adminhome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BillEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.network.BillFirebase
import com.example.balo.data.network.BrandFirebase
import com.example.balo.data.network.ProductFirebase
import com.example.balo.utils.Utils
import com.github.mikephil.charting.data.Entry

class AdminHomeVM : ViewModel() {

    private val _entriesBills = MutableLiveData<Pair<List<Entry>, List<String>>>()
    val entriesBills = _entriesBills

    private val _products = MutableLiveData<List<BaloEntity>?>(null)
    val products = _products

    private val _bills = MutableLiveData<List<BillEntity>>(emptyList())
    val bills = _bills

    private val billFirebase = BillFirebase()

    fun getBills(handleFail: (String) -> Unit) {
        billFirebase.getBills(
            handleSuccess = { convertBillToEntries(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    private fun convertBillToEntries(bills: List<BillEntity>) {
        val entries = mutableListOf<Entry>()
        val dates = mutableListOf<String>()
        for (i in bills.indices) {
            val totalPrice = Utils.stringToInt(bills[i].totalPrice)
            val date = bills[i].date
            entries.add(Entry(i.toFloat(), totalPrice.toFloat()))
            dates.add(date)
        }
        _entriesBills.postValue(Pair(entries, dates))
    }
}
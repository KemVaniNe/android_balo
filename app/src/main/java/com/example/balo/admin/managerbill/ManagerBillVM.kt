package com.example.balo.admin.managerbill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BillEntity
import com.example.balo.data.network.BillFirebase
import com.example.balo.utils.Constants

class ManagerBillVM : ViewModel() {
    private val _bills = MutableLiveData<List<BillEntity>>(emptyList())
    val bills = _bills

    private val _billsSearch = MutableLiveData<List<BillEntity>>(emptyList())
    val billsSearch = _billsSearch

    private val billFirebase = BillFirebase()
    fun getBills(handleFail: (String) -> Unit) {
        billFirebase.getBills(
            handleSuccess = { _bills.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }

    fun search(date: String) {
        val searchData = mutableListOf<BillEntity>()
        if (date == Constants.ALL_DATE) {
            _bills.value?.let { searchData.addAll(it) }
        } else {
            _bills.value?.forEach {
                if (it.date == date) {
                    searchData.add(it)
                }
            }
        }
        _billsSearch.postValue(searchData)
    }
}
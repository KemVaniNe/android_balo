package com.example.balo.admin.adminbill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.BillEntity
import com.example.balo.data.network.BillFirebase

class AdminBillVM: ViewModel() {
    private val _bills = MutableLiveData<List<BillEntity>>(emptyList())
    val bills = _bills
    private val billFirebase = BillFirebase()
    fun getBills( handleFail: (String) -> Unit) {
        billFirebase.getBills(
            handleSuccess = { _bills.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
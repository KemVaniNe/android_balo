package com.example.balo.ui

import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.google.gson.Gson

class ShareViewModel : ViewModel() {
    var account: UserEntity? = null

    fun updateAccount(data: String) {
        account = Gson().fromJson(data, UserEntity::class.java)
    }

}
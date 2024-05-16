package com.example.balo.shareview

import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.google.gson.Gson

class ShareViewModel : ViewModel() {
    var account: UserEntity? = null

    fun updateAccount(data: String) {
        account = if (data == "") {
            Pref.idUser = Constants.ID_GUEST
            null
        } else {
            Pref.idUser = Gson().fromJson(data, UserEntity::class.java).id
            Gson().fromJson(data, UserEntity::class.java)
        }
    }
}
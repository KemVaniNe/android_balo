package com.example.balo.client.clientaddress

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.R
import com.example.balo.data.model.AddressEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.network.AccountFirebase
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class ClientAddressVM : ViewModel() {

    private val accountFirebase = AccountFirebase()

    private val _account = MutableLiveData<UserEntity?>(null)
    val account = _account

    fun getAddressFromJson(context: Context): AddressEntity? {
        return try {
            val inputStream = context.resources.openRawResource(R.raw.address)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            Gson().fromJson(jsonString, object : TypeToken<AddressEntity>() {}.type)
        } catch (e: Exception) {
            null
        }
    }

    fun updateInfo(
        user: UserEntity,
        handleSuccess: () -> Unit,
        handleError: (String) -> Unit
    ) {
        accountFirebase.updateUser(
            user = user,
            handleSuccess = { handleSuccess.invoke() },
            handleFail = { handleError.invoke(it) }
        )
    }

    fun getUser(handleFail: (String) -> Unit) {
        accountFirebase.getUserBaseId(
            handleSuccess = { _account.postValue(it) },
            handleFail = { handleFail.invoke(it) }
        )
    }
}
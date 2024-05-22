package com.example.balo.client.clientAddress.newAddress

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.balo.R
import com.example.balo.data.model.AddressEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Utils
import com.google.common.reflect.TypeToken
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson

class ClientAddressVM : ViewModel() {

    private val db = Firebase.firestore

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
        val updateData = Utils.userToMap(user)
        db.collection(Collection.USER.collectionName).document(user.id)
            .update(updateData)
            .addOnSuccessListener {
                handleSuccess.invoke()
            }
            .addOnFailureListener { e -> handleError.invoke(e.message.toString()) }
    }
}
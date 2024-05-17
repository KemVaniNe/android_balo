package com.example.balo.client.clientaccout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.User
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ClientAccountVM : ViewModel() {
    private val _account = MutableLiveData<UserEntity?>(null)
    val account = _account

    private val db = Firebase.firestore

    fun updateAccount(handleFail: (String) -> Unit) {
        if (Pref.idUser != Constants.ID_GUEST) {
            db.collection(Collection.USER.collectionName).document(Pref.idUser)
                .get()
                .addOnSuccessListener { document ->
                    _account.postValue(Utils.convertDocToUser(document))
                }.addOnFailureListener { exception ->
                    handleFail.invoke(exception.message.toString())
                }
        }
    }

    fun updatePassword(
        user: UserEntity,
        handleSuccess: () -> Unit,
        handleError: (String) -> Unit
    ) {
        val updateData = Utils.userToMap(user)
        db.collection(Collection.USER.collectionName).document(user.id)
            .update(updateData)
            .addOnSuccessListener {
                _account.postValue(user)
                handleSuccess.invoke()
            }
            .addOnFailureListener { e -> handleError.invoke(e.message.toString()) }
    }
}
package com.example.balo.client.clientorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ClientOrderVM : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading = _isLoading
    var account: UserEntity? = null

    private val db = Firebase.firestore

    fun loadUser(handleFail: (String) -> Unit) {
        _isLoading.postValue(true)
        if (Pref.idUser != Constants.ID_GUEST) {
            db.collection(Collection.USER.collectionName).document(Pref.idUser)
                .get()
                .addOnSuccessListener { document ->
                    account = Utils.convertDocToUser(document)
                    _isLoading.postValue(true)
                }.addOnFailureListener { exception ->
                    handleFail.invoke(exception.message.toString())
                    _isLoading.postValue(true)
                }
        }
    }
}
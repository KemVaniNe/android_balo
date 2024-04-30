package com.example.balo.ui.admin.admincaterogy

import androidx.lifecycle.ViewModel
import com.example.balo.data.model.CategoryEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Category
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.User
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AdminCategoryVM : ViewModel() {
    private val db = Firebase.firestore
    fun createCategory(
        category: CategoryEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = hashMapOf(
            Category.NAME.property to category.name,
            Category.PIC.property to category.pic
        )
        db.collection(Collection.CATEGORY.collectionName).add(data)
            .addOnSuccessListener {
                handleSuccess.invoke()
            }
            .addOnFailureListener { e ->
                handleFail.invoke(e.message.toString())
            }
    }

}
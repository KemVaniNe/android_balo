package com.example.balo.data.network

import com.example.balo.data.model.NotificationEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.Notification
import com.example.balo.data.model.enum.Order
import com.example.balo.utils.Constants
import com.example.balo.utils.DocumentUtil
import com.example.balo.utils.MapObjectUtil
import com.example.balo.utils.Pref
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class NotificationFirebase {
    private val db = Firebase.firestore

    fun getNotificationByUser(
        handleSuccess: (List<NotificationEntity>) -> Unit,
        handleFail: (String) -> Unit
    ) {
        if (Pref.idUser == Constants.ID_GUEST) {
            handleSuccess.invoke(emptyList())
        } else {
            val list = mutableListOf<NotificationEntity>()
            db.collection(Collection.NOTIFICATION.collectionName)
                .whereEqualTo(Notification.idUser.property, Pref.idUser)
                .get()
                .addOnSuccessListener { document ->
                    document.forEach {
                        list.add(DocumentUtil.convertDocToNotification(it))
                    }
                    handleSuccess.invoke(list)
                }
                .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
        }
    }

    fun getNotificationAdmin(
        handleSuccess: (List<NotificationEntity>) -> Unit,
        handleFail: (String) -> Unit
    ) {
        val list = mutableListOf<NotificationEntity>()
        db.collection(Collection.NOTIFICATION.collectionName)
            .whereEqualTo(Notification.role.property, true)
            .get()
            .addOnSuccessListener { document ->
                document.forEach {
                    list.add(DocumentUtil.convertDocToNotification(it))
                }
                handleSuccess.invoke(list)
            }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun updateNotification(
        notification: NotificationEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = MapObjectUtil.notificationToMap(notification = notification)
        db.collection(Collection.NOTIFICATION.collectionName)
            .document(notification.id)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { handleSuccess() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun deleteNotifications(
        notifications: List<NotificationEntity>,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val batch = db.batch()
        notifications.forEach {
            val docRef = db.collection(Collection.NOTIFICATION.collectionName).document(it.id)
            batch.delete(docRef)
        }

        batch.commit()
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }

    fun createNotification(
        notification: NotificationEntity,
        handleSuccess: () -> Unit,
        handleFail: (String) -> Unit
    ) {
        val data = MapObjectUtil.notificationToMap(notification)
        db.collection(Collection.NOTIFICATION.collectionName)
            .add(data)
            .addOnSuccessListener { handleSuccess.invoke() }
            .addOnFailureListener { e -> handleFail.invoke("ERROR: ${e.message ?: "Unknown error occurred"}") }
    }
}
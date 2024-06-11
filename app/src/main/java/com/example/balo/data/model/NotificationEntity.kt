package com.example.balo.data.model

data class NotificationEntity(
    var id: String = "",
    var idUser: String = "",
    var notification: String = "",
    var idOrder: String = "",
    var datatime: String = "",
    var isSeen: Boolean = false,
    var roleUser: Boolean = false
)
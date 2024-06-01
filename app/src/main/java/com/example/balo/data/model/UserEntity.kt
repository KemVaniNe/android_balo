package com.example.balo.data.model

data class UserEntity(
    var id: String = "",
    var username: String = "",
    var pic: String = "",
    var phone: String = "",
    var password: String = "",
    var role: Boolean = false,
    var address: List<String> = emptyList()
)
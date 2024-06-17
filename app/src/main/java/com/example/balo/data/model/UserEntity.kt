package com.example.balo.data.model

data class UserEntity(
    var id: String = "",
    var username: String = "",
    var email: String = "",
    var phone: String = "",
    var password: String = "",
    var role: Boolean = false,
    var authcode: String = "",
    var address: List<String> = emptyList()
)
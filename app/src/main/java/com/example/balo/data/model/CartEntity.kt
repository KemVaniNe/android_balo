package com.example.balo.data.model

data class CartEntity(
    var idCart: String = "",
    var idUser: String = "",
    var idBalo: String = "",
    var quantity: Double = 0.0,
    var nameBalo: String = "",
    var price: Double = 0.0,
    var totalPriceSell: Double = 0.0,
    var pic: String = "",
    var max: Double = 0.0,
    var isSelect: Boolean = false
)
package com.example.balo.data.model

data class CartEntity(
    var idCart: String = "",
    var idUser: String = "",
    var idBalo: String = "",
    var quantity: String = "0",
    var nameBalo: String = "",
    var price: String = "0",
    var totalPriceSell: String = "0",
    var pic: String = "",
    var max: String = "0",
    var isSelect: Boolean = false
)
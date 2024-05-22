package com.example.balo.data.model

data class OrderEntity(
    var id: String = "",
    var iduser: String = "",
    var date: String = "",
    var totalPrice: String = "0",
    var statusOrder: String = "",
    var priceShip: String = "0",
    var address: String = ""
)

data class OrderDetail(
    var idDetail: String = "",
    var idOrder: String = "",
    var idBalo: String = "",
    var quantity: String = "0",
    var price: String = "0",
    var rate: String = "0",
    var comment: String = ""
)
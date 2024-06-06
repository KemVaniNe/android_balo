package com.example.balo.data.model

data class OrderEntity(
    var id: String = "",
    var iduser: String = "",
    var date: String = "",
    var totalPrice: String = "0",
    var statusOrder: String = "",
    var priceShip: String = "0",
    var address: String = "",
    var detail: List<OrderDetailEntity> = emptyList()
)

data class OrderDetailEntity(
    var idBalo: String = "",
    var nameBalo: String = "",
    var quantity: String = "0",
    var price: String = "0",
    var totalPriceSellCurrent: String = "0",
    var picProduct: String = "",
    var rate: String = "0",
    var comment: String = "",
    var sell: String = "0"
)
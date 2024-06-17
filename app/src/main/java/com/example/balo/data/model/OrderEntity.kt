package com.example.balo.data.model

data class OrderEntity(
    var id: String = "",
    var iduser: String = "",
    var date: String = "",
    var totalPrice: Double = 0.0,
    var statusOrder: String = "",
    var priceShip: Double = 0.0,
    var address: String = "",
    var idpay: String = "",
    var detail: List<OrderDetailEntity> = emptyList()
)

data class OrderDetailEntity(
    var idBalo: String = "",
    var nameBalo: String = "",
    var quantity: Double = 0.0,
    var price: Double = 0.0,
    var totalPriceSellCurrent: Double = 0.0,
    var picProduct: String = "",
    var rate: Double = 0.0,
    var comment: String = "",
    var sell: Double = 0.0
)
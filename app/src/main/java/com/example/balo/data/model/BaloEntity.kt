package com.example.balo.data.model

data class BaloEntity(
    var id: String,
    var name: String,
    var idBrand: String,
    var idCategory: String,
    var priceSell: Int,
    var priceImport: Int,
    var des: String,
    var pic: String,
    var collor: List<String>,
    var sell: Int,
    var available: Int,
)
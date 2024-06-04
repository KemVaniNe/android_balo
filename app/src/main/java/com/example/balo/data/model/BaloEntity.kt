package com.example.balo.data.model

data class BaloEntity(
    var id: String = "",
    var name: String = "",
    var idBrand: String = "",
    var priceSell: String = "0",
    var priceImport: String = "0",
    var des: String = "",
    var pic: String = "",
    var sell: String = "0",
    var quantitiy: String = "0",
    var rate: String = "0",
    var totalImport: String = "0",
    var totalSell: String = "0",
    var isSell: Boolean = true,
    var comment: List<String> = emptyList()
)
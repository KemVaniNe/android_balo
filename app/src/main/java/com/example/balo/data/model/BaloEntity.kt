package com.example.balo.data.model

data class BaloEntity(
    var id: String = "",
    var name: String = "",
    var idBrand: String = "",
    var priceSell: Double = 0.0,
    var priceImport: Double = 0.0,
    var des: String = "",
    var pic: String = "",
    var sell: Double = 0.0,
    var quantitiy: Double = 0.0,
    var rate: Double = 0.0,
    var totalImport: Double = 0.0,
    var totalSell: Double = 0.0,
    var isSell: Boolean = true,
    var comment: List<String> = emptyList(),
    var isSelected: Boolean = false
)
package com.example.balo.data.model

import com.google.gson.annotations.SerializedName

data class AddressEntity(
    @SerializedName("Data")
    var data: List<TinhThanhPhoEntity> = emptyList()
)

data class TinhThanhPhoEntity(
    @SerializedName("TinhThanhPho")
    var tinhThanhPho: String = "",
    @SerializedName("QuanHuyenDS")
    var quanHuyenDS: List<QuanHuyenEntity>
)

data class QuanHuyenEntity(
    @SerializedName("QuanHuyen")
    var quanHuyen: String = "",
    @SerializedName("PhuongXaDS")
    var phuongXaDS: List<String> = emptyList()
)

data class AddressSelect(
    var address: String = "",
    var isSelected: Boolean = false
)
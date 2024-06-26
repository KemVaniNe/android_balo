package com.example.balo.payment.data

data class ZLRefundResponse(
    val return_code: Int,
    val return_message: String,
    val sub_return_code: Int,
    val sub_return_message: String
)

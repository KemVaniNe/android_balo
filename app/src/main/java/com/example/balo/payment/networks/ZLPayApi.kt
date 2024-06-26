package com.example.balo.payment.networks

import com.example.balo.payment.data.ZLRefundResponse
import com.example.balo.payment.utils.ZLConfig
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ZLPayApi {
    @POST(ZLConfig.POST_REFUND)
    suspend fun refund(@Body request: RequestBody): ZLRefundResponse
}
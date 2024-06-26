package com.example.balo.payment.networks

import android.annotation.SuppressLint
import com.example.balo.payment.utils.ZLConfig.APP_ID
import com.example.balo.payment.utils.ZLConfig.MAC_KEY
import com.example.balo.payment.utils.ZLConfig.SERVER_ZALOPAY
import com.example.balo.zalopay.Helper.Helpers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import okhttp3.FormBody
import okhttp3.RequestBody

class ZLPayService {
    fun getZLPayApi(): ZLPayApi {
        return Retrofit.Builder()
            .baseUrl(SERVER_ZALOPAY)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZLPayApi::class.java)
    }

    fun getRequestRefund(zpTransId: String, amount: Long): RequestBody {
        val timestamp = System.currentTimeMillis()
        val uid = "$timestamp${(111..999).random()}"
        val data = "${APP_ID}|$zpTransId|$amount|refund|$timestamp"
        val mac = Helpers.getMac(MAC_KEY, data)

        return FormBody.Builder()
            .add("app_id", APP_ID.toString())
            .add("zp_trans_id", zpTransId)
            .add("m_refund_id", "${getCurrentTimeString()}_${APP_ID}_$uid")
            .add("amount", amount.toString())
            .add("timestamp", timestamp.toString())
            .add("description", "refund")
            .add("mac", mac)
            .build()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTimeString(): String {
        val cal = GregorianCalendar(TimeZone.getTimeZone("GMT+7"))
        val fmt = SimpleDateFormat("yyMMdd")
        fmt.calendar = cal
        return fmt.format(cal.time)
    }
}
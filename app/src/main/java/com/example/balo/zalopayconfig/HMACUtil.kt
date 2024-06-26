package com.example.balo.zalopayconfig

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.balo.zalopay.Helper.HMac.HexStringUtil
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.LinkedList
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HMACUtil {

    val HMACMD5 = "HmacMD5"
    val HMACSHA1 = "HmacSHA1"
    val HMACSHA256 = "HmacSHA256"
    val HMACSHA512 = "HmacSHA512"
    val UTF8CHARSET = StandardCharsets.UTF_8

    val HMACS = LinkedList(
        mutableListOf(
            "UnSupport",
            "HmacSHA256",
            "HmacMD5",
            "HmacSHA384",
            "HMacSHA1",
            "HmacSHA512"
        )
    )

    private fun HMacEncode(algorithm: String, key: String, data: String): ByteArray? {
        var macGenerator: Mac? = null
        try {
            macGenerator = Mac.getInstance(algorithm)
            val signingKey = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), algorithm)
            macGenerator.init(signingKey)
        } catch (ex: Exception) {
        }
        if (macGenerator == null) {
            return null
        }
        var dataByte: ByteArray? = null
        try {
            dataByte = data.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
        }
        return macGenerator.doFinal(dataByte)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun HMacBase64Encode(algorithm: String, key: String, data: String): String? {
        val hmacEncodeBytes = HMacEncode(algorithm, key, data) ?: return null
        return Base64.getEncoder().encodeToString(hmacEncodeBytes)
    }

    fun HMacHexStringEncode(algorithm: String, key: String, data: String): String? {
        val hmacEncodeBytes = HMacEncode(algorithm, key, data) ?: return null
        return HexStringUtil.byteArrayToHexString(hmacEncodeBytes)
    }
}
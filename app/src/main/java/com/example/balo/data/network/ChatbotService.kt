package com.example.balo.data.network

import com.example.balo.data.model.ChatbotRequest
import com.example.balo.data.model.ChatbotResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ChatbotApi {
    @POST("/chatbot")
    suspend fun sendMessage(@Body request: ChatbotRequest): ChatbotResponse
}

class ChatbotService {

    fun getChatbotAPI(): ChatbotApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://chatbotdeploy-1.onrender.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatbotApi::class.java)
    }
}

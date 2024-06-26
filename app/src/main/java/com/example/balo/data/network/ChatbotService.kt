package com.example.balo.data.network

import com.example.balo.data.model.ChatbotRequest
import com.example.balo.data.model.ChatbotResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatbotApi {
    @POST("/chatbot")
    suspend fun sendMessage(@Body request: ChatbotRequest): ChatbotResponse
}

class ChatbotService {

    fun getChatbotAPI(): ChatbotApi {
        return Retrofit.Builder()
            .baseUrl("https://balo-chatbot.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatbotApi::class.java)
    }
}

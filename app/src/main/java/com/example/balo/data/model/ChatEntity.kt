package com.example.balo.data.model

data class ChatEntity(
    var isUser: Boolean = true,
    var mess: String = ""
)

data class ChatbotRequest(
    val message: String
)

data class ChatbotResponse(
    val response: String
)
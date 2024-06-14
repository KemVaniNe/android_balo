package com.example.balo.client.chatbot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.ChatEntity
import com.example.balo.data.network.ChatbotService

class ChatbotVM : ViewModel() {
    private val _mess = MutableLiveData(
        listOf(
            ChatEntity(
                false,
                "Xin chào, Kem có thể giúp gì cho bạn?"
            )
        )
    )
    val mess = _mess

    private val chatbotService = ChatbotService()

    fun postMess(mess: String, handleFail: (String) -> Unit) {
        val list = mutableListOf<ChatEntity>()
        _mess.value?.let { list.addAll(it) }
        list.add(
            ChatEntity(
                true,
                mess
            )
        )
        _mess.postValue(list)
    }
}
package com.example.balo.client.chatbot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balo.data.model.ChatEntity
import com.example.balo.data.model.ChatbotRequest
import com.example.balo.data.network.ChatbotService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatbotVM : ViewModel() {

    private val _mess = MutableLiveData(
        listOf(ChatEntity(false, "Xin chào, Kem có thể giúp gì cho bạn?"))
    )
    val mess = _mess

    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading


    private val chatbotService = ChatbotService()

    private val chatbotAPI = chatbotService.getChatbotAPI()

    fun postMess(message: String, handleFail: (String) -> Unit) {
        val list = mutableListOf<ChatEntity>()
        _mess.value?.let { list.addAll(it) }
        list.add(ChatEntity(true, message))
        _mess.postValue(list)
        postValue(list, message, handleFail)
    }

    private fun postValue(
        list: MutableList<ChatEntity>,
        message: String,
        handleFail: (String) -> Unit
    ) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val request = ChatbotRequest(message)
                val response = withContext(Dispatchers.IO) {
                    chatbotAPI.sendMessage(request)
                }
                list.add(ChatEntity(false, response.response))
                _isLoading.postValue(false)
                _mess.postValue(list)
            } catch (e: Exception) {
                _isLoading.postValue(false)
                handleFail.invoke("ERROR ${e.message}")
            }
        }
    }
}
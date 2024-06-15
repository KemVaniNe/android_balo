package com.example.balo.client.chatbot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balo.data.model.ChatEntity
import com.example.balo.data.model.ChatbotRequest
import com.example.balo.data.network.ChatbotService
import com.example.balo.data.network.ProductFirebase
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

    private val productFirebase = ProductFirebase()

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

                if (extractVariable(response.response) == "SANPHAM") {
                    getProducts(list, response.response, handleFail)
                } else {
                    list.add(ChatEntity(false, response.response))
                    _isLoading.postValue(false)
                    _mess.postValue(list)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                handleFail.invoke("ERROR ${e.message}")
            }
        }
    }

    private fun getProducts(
        list: MutableList<ChatEntity>,
        response: String,
        handleFail: (String) -> Unit
    ) {
        productFirebase.getProducts(
            handleSuccess = { products ->
                val topSell = products.maxByOrNull { it.sell }
                val newMess = if (topSell != null) {
                    response.replace("1${extractVariable(response)}1_", "") + topSell.name
                } else response
                list.add(ChatEntity(false, newMess))
                _mess.postValue(list)
                _isLoading.postValue(false)
            },
            handleFail = {
                _isLoading.postValue(false)
                handleFail.invoke(it)
            }
        )
    }

    private fun extractVariable(input: String): String {
        val startMarker = "1"
        val endMarker = "1"
        val startIndex = input.indexOf(startMarker)
        val endIndex = input.indexOf(endMarker, startIndex + startMarker.length)

        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            input.substring(startIndex + 1, endIndex)
        } else {
            ""
        }
    }
}
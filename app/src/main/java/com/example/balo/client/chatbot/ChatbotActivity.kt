package com.example.balo.client.chatbot

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.ChatAdapter
import com.example.balo.data.model.ChatEntity
import com.example.balo.databinding.ActivityChatbotBinding
import com.example.balo.shareview.base.BaseActivity

class ChatbotActivity : BaseActivity<ActivityChatbotBinding>() {

    private lateinit var viewModel: ChatbotVM

    private var mess = mutableListOf<ChatEntity>()

    private val messAdapter by lazy { ChatAdapter(mess) }

    override fun viewBinding(inflate: LayoutInflater): ActivityChatbotBinding =
        ActivityChatbotBinding.inflate(inflate)

    override fun initView() {
        binding.rvMess.run {
            layoutManager = LinearLayoutManager(this@ChatbotActivity)
            adapter = messAdapter
        }
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ChatbotVM::class.java]
        listenVM()
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        btnSend.setOnClickListener { handleSend() }
    }

    private fun handleSend() = binding.run {
        if (edtMess.text.toString().trim() == "") {
            toast("Bạn chưa nhập giá trị gì")
        } else {
            postMess()
        }
        edtMess.setText("")
    }

    private fun postMess() {
        viewModel.postMess(binding.edtMess.text.toString()) { toast("FAIL") }
    }

    private fun listenVM() {
        viewModel.mess.observe(this) {
            mess.run {
                clear()
                addAll(it)
            }
            messAdapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(this) {
            binding.clLoading.visibility = if(it) View.VISIBLE else View.GONE
        }
    }
}
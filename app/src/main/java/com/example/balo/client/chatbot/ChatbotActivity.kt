package com.example.balo.client.chatbot

import android.view.LayoutInflater
import com.example.balo.databinding.ActivityChatbotBinding
import com.example.balo.shareview.base.BaseActivity

class ChatbotActivity : BaseActivity<ActivityChatbotBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityChatbotBinding =
        ActivityChatbotBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.tvTitle.setOnClickListener { finish() }
    }
}
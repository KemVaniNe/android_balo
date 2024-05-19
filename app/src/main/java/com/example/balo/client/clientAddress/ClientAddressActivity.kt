package com.example.balo.client.clientAddress

import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.example.balo.databinding.ActivityClientAddressBinding
import com.example.balo.shareview.base.BaseActivity

class ClientAddressActivity : BaseActivity<ActivityClientAddressBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityClientAddressBinding =
        ActivityClientAddressBinding.inflate(inflate)

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() = binding.run {}

    override fun initData() {
    }

    override fun initListener() {
    }
}
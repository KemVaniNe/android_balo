package com.example.balo.client.clientnotification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.balo.databinding.FragmentClientNotificationBinding
import com.example.balo.shareview.base.BaseFragment

class ClientNotificationFragment : BaseFragment<FragmentClientNotificationBinding>() {

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentClientNotificationBinding = FragmentClientNotificationBinding.inflate(inflater)
}
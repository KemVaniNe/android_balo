package com.example.balo.ui.user.account

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.balo.databinding.FragmentAccountBinding
import com.example.balo.ui.base.BaseFragment


class AccountFragment : BaseFragment<FragmentAccountBinding>() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        tvAddress.setOnClickListener {
            //TODO
        }
        tvCart.setOnClickListener {
            //TODO
        }
        tvContact.setOnClickListener {
            //TODO
        }
        tvOrder.setOnClickListener {
            //TODO
        }
        tvInfo.setOnClickListener {
            //TODO
        }
        tvUpdatePass.setOnClickListener {
            //TODO
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAccountBinding = FragmentAccountBinding.inflate(inflater)

}
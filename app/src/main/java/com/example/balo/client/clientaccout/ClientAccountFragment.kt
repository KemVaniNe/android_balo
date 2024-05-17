package com.example.balo.client.clientaccout

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.balo.client.clientcart.ClientCartActivity
import com.example.balo.client.clientmain.ClientMainActivity
import com.example.balo.databinding.FragmentAccountBinding
import com.example.balo.shareview.base.BaseFragment
import com.example.balo.shareview.login.LoginActivity

class ClientAccountFragment : BaseFragment<FragmentAccountBinding>() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        tvAddress.setOnClickListener {
            //TODO
        }
        tvCart.setOnClickListener {
            context?.let { startActivity(Intent(it, ClientCartActivity::class.java)) }
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
        tvLogOut.setOnClickListener {
            context?.let { startActivity(Intent(it, LoginActivity::class.java)) }
            (context as ClientMainActivity).finishAct()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAccountBinding = FragmentAccountBinding.inflate(inflater)

}
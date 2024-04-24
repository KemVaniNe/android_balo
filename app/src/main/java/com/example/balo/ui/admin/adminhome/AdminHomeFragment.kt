package com.example.balo.ui.admin.adminhome

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.balo.databinding.FragmentAdminHomeBinding
import com.example.balo.ui.base.BaseFragment

class AdminHomeFragment : BaseFragment<FragmentAdminHomeBinding>() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminHomeBinding = FragmentAdminHomeBinding.inflate(inflater)

}
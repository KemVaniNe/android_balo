package com.example.balo.ui.user.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.balo.databinding.FragmentHomeBinding
import com.example.balo.ui.base.BaseFragment
import com.example.balo.ui.user.search.SearchActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        tvSearch.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater)

}
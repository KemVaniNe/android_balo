package com.example.balo.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.balo.databinding.FragmentFavoriteBinding
import com.example.balo.ui.base.BaseFragment

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater)

}
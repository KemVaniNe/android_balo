package com.example.balo.client.clientcart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.FavoriteAdapter
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.FragmentFavoriteBinding
import com.example.balo.shareview.base.BaseFragment

class ClientCartFragment : BaseFragment<FragmentFavoriteBinding>() {

    private var favorite = mutableListOf<BaloEntity>()

    private val favoriteAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FavoriteAdapter(favorite) {
            //TODO
        }
    }

    override fun initView() {
        binding.rvFavorite.layoutManager = LinearLayoutManager(context)
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
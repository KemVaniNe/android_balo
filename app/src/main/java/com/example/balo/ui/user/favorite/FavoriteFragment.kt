package com.example.balo.ui.user.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.FragmentFavoriteBinding
import com.example.balo.ui.base.BaseFragment

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

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
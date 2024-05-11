package com.example.balo.ui.user.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.balo.databinding.FragmentHomeBinding
import com.example.balo.ui.base.BaseFragment
import com.example.balo.adapter.BannerViewPagerAdapter
import com.example.balo.ui.user.search.SearchActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val banners = mutableListOf<Int>()
    private val viewPagerAdapter by lazy { BannerViewPagerAdapter(banners) }
    private lateinit var viewModel: HomeViewModel
    override fun initView() = binding.run {
        viewPager.adapter = viewPagerAdapter
        dotsIndicator.setViewPager(viewPager)
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        listenVM()
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

    private fun listenVM() {
        viewModel.banner.onEach {
            banners.run {
                clear()
                addAll(it)
            }
            viewPagerAdapter.notifyDataSetChanged()
        }.launchIn(scope = lifecycleScope)
    }

}
package com.example.balo.ui.user.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.databinding.FragmentHomeBinding
import com.example.balo.ui.base.BaseFragment
import com.example.balo.adapter.BannerViewPagerAdapter
import com.example.balo.adapter.brand.BrandAdapter
import com.example.balo.data.model.BrandEntity
import com.example.balo.ui.user.search.SearchActivity
import com.example.balo.utils.Utils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val banners = mutableListOf<Int>()
    private val viewPagerAdapter by lazy { BannerViewPagerAdapter(banners) }
    private lateinit var viewModel: HomeViewModel
    private lateinit var dialog: AlertDialog
    private val brands = mutableListOf<BrandEntity>()
    private val brandAdapter by lazy {
        BrandAdapter(brands) { pos ->
            //TODO
        }
    }

    override fun initView() = binding.run {
        viewPager.adapter = viewPagerAdapter
        dotsIndicator.setViewPager(viewPager)
        rvBrand.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvBrand.adapter = brandAdapter
    }

    override fun initData() {
        context?.let {
            dialog = Utils.showProgressDialog(it)
        }
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        getBrands()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.banner.onEach {
            banners.run {
                clear()
                addAll(it)
            }
            viewPagerAdapter.notifyDataSetChanged()
        }.launchIn(scope = lifecycleScope)

        viewModel.brands.observe(this) {
            if(it != null) {
                if(dialog.isShowing) dialog.dismiss()
                brands.run {
                    clear()
                    addAll(it)
                }
                brandAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getBrands() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getAllBrands { error ->
            if (dialog.isShowing) dialog.dismiss()
            toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
        }
    }
}
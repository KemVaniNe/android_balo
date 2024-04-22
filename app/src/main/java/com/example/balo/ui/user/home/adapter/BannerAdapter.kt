package com.example.balo.ui.user.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.balo.databinding.LayoutBannerBinding
import com.example.balo.utils.Utils

class BannerAdapter(private val banners: List<String>) : PagerAdapter() {

    private val list = banners
    override fun getCount(): Int = banners.size
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(viewGroup: ViewGroup, i: Int): Any {
        val binding = LayoutBannerBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        Utils.displayBase64Image(list[i], binding.imgBanner)
        viewGroup.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(viewGroup: ViewGroup, i: Int, obj: Any) {
        viewGroup.removeView(obj as ConstraintLayout)
    }
}
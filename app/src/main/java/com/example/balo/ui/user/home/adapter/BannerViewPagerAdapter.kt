package com.example.balo.ui.user.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.balo.databinding.LayoutBannerBinding

class BannerViewPagerAdapter(private val onBoards: List<Int>) : PagerAdapter() {

    private val list = onBoards
    override fun getCount(): Int = onBoards.size
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(viewGroup: ViewGroup, i: Int): Any {
        val binding =
            LayoutBannerBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        Glide.with(viewGroup.context).load(list[i]).into(binding.imgBanner)
        viewGroup.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(viewGroup: ViewGroup, i: Int, obj: Any) {
        viewGroup.removeView(obj as ConstraintLayout)
    }
}
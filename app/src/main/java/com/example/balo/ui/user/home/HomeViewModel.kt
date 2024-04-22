package com.example.balo.ui.user.home

import androidx.lifecycle.ViewModel
import com.example.balo.utils.Banner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _banners = MutableStateFlow<List<Int>>(emptyList())
    val banner = _banners.asStateFlow()

    init {
        _banners.tryEmit(Banner.banners)
    }
}
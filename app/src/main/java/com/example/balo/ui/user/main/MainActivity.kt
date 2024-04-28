package com.example.balo.ui.user.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.databinding.ActivityMainBinding
import com.example.balo.ui.ShareViewModel
import com.example.balo.ui.base.BaseActivity
import com.example.balo.utils.Constants

class MainActivity : BaseActivity<ActivityMainBinding>() {

    lateinit var shareViewModel: ShareViewModel
    companion object {

        const val KEY_USER = "user_account"
        const val EMPTY_ACCOUNT= ""

        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(KEY_USER, response)
            }
        }
    }

    private val viewPagerAdapter by lazy { MainViewPagerAdapter(supportFragmentManager) }

    override fun viewBinding(inflate: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflate)

    override fun initView() {
        if(shareViewModel.account == null) {
            viewGuest()
        } else {
            viewUser()
        }
        binding.viewPager.run {
            offscreenPageLimit = 2
            adapter = viewPagerAdapter
        }
    }

    override fun initData() {
        shareViewModel = ViewModelProvider(this)[ShareViewModel::class.java]
        val intent = intent
        if (intent.hasExtra(KEY_USER) && intent.getStringExtra(KEY_USER) != null) {
            shareViewModel.updateAccount(intent.getStringExtra(KEY_USER)!!)
        } else {
            finish()
        }

    }

    override fun initListener() {
        binding.run {
            bottomNav.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_home -> {
                        changeCurrentFragment(Constants.USER_HOME)
                    }

                    R.id.menu_favorite -> {
                        changeCurrentFragment(Constants.USER_FAVORITE)
                    }

                    R.id.menu_account -> {
                        changeCurrentFragment(Constants.USER_ACCOUNT)
                    }

                    else -> false
                }
            }
        }
    }

    private fun changeCurrentFragment(type: Int): Boolean {
        binding.viewPager.setCurrentItem(type, true)
        return true
    }

    private fun viewGuest() {
        //TODO
    }

    private fun viewUser() {
        //TODO
    }

}
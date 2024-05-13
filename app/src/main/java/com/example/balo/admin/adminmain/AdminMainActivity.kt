package com.example.balo.admin.adminmain

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.adapter.AdminViewPagerAdapter
import com.example.balo.databinding.ActivityAdminBinding
import com.example.balo.shareview.ShareViewModel
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants

class AdminMainActivity : BaseActivity<ActivityAdminBinding>() {

    lateinit var shareViewModel: ShareViewModel
    companion object {

        const val KEY_ADMIN = "admin_account"

        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, AdminMainActivity::class.java).apply {
                putExtra(KEY_ADMIN, response)
            }
        }
    }


    private val viewPagerAdapter by lazy { AdminViewPagerAdapter(supportFragmentManager) }
    override fun viewBinding(inflate: LayoutInflater): ActivityAdminBinding =
        ActivityAdminBinding.inflate(inflate)

    override fun initView() {
        binding.viewPager.run {
            offscreenPageLimit = 2
            adapter = viewPagerAdapter
        }
    }

    override fun initData() {
        shareViewModel = ViewModelProvider(this)[ShareViewModel::class.java]
        val intent = intent
        if (intent.hasExtra(KEY_ADMIN) && intent.getStringExtra(KEY_ADMIN) != null) {
            shareViewModel.updateAccount(intent.getStringExtra(KEY_ADMIN)!!)
        } else {
            finish()
        }
    }

    override fun initListener() {
        binding.run {
            bottomNav.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.admin_home -> {
                        changeCurrentFragment(Constants.ADMIN_HOME)
                    }

                    R.id.admin_order -> {
                        changeCurrentFragment(Constants.ADMIN_ORDER)
                    }

                    R.id.admin_cart -> {
                        changeCurrentFragment(Constants.ADMIN_PRODUCT)
                    }

                    R.id.admin_account -> {
                        changeCurrentFragment(Constants.ADMIN_ACCOUNT)
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

}
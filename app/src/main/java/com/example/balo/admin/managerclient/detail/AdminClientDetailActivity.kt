package com.example.balo.admin.managerclient.detail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.balo.admin.managerclient.ManagerClientVM
import com.example.balo.databinding.ActivityAdminClientDetailBinding
import com.example.balo.shareview.base.BaseActivity

class AdminClientDetailActivity : BaseActivity<ActivityAdminClientDetailBinding>() {

    private lateinit var viewModel: ManagerClientVM

    private var id = ""
    private var name = ""

    companion object {
        const val KEY_ID = "detail"
        const val KEY_NAME = "name"
        fun newIntent(context: Context, id: String, name: String): Intent {
            return Intent(context, AdminClientDetailActivity::class.java).apply {
                putExtra(KEY_ID, id)
                putExtra(KEY_NAME, name)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminClientDetailBinding =
        ActivityAdminClientDetailBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ManagerClientVM::class.java]
        listenVM()
        val intent = intent
        val nameReceive = intent.getStringExtra(KEY_NAME)
        val idReceive = intent.getStringExtra(KEY_ID)
        if (intent.hasExtra(KEY_NAME) && intent.hasExtra(KEY_ID) && nameReceive != null && idReceive != null) {
            id = idReceive
            name = nameReceive
            getBill()
        } else {
            finish()
        }
    }

    override fun initListener() {
    }

    private fun getBill() {

    }

    private fun listenVM() {

    }
}
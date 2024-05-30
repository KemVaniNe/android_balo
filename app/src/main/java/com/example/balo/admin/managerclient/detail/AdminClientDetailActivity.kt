package com.example.balo.admin.managerclient.detail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.AdminBillAdapter
import com.example.balo.admin.managerclient.ManagerClientVM
import com.example.balo.admin.managerorder.detail.AdminDetailOrderActivity
import com.example.balo.data.model.BillEntity
import com.example.balo.databinding.ActivityAdminClientDetailBinding
import com.example.balo.shareview.base.BaseActivity

class AdminClientDetailActivity : BaseActivity<ActivityAdminClientDetailBinding>() {

    private lateinit var viewModel: ManagerClientVM

    private val bills = mutableListOf<BillEntity>()

    private val billAdapter by lazy {
        AdminBillAdapter(bills) { pos ->
            startActivity(AdminDetailOrderActivity.newIntent(this, bills[pos].idOrder))
        }
    }

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

    override fun initView() = binding.run {
        rvBill.layoutManager = LinearLayoutManager(this@AdminClientDetailActivity)
        rvBill.adapter = billAdapter
        tvTitle.text = name
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
            listenVM()
            getBill()
        } else {
            finish()
        }
    }

    override fun initListener() {
        binding.tvTitle.setOnClickListener { finish() }
    }

    private fun getBill() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getBillsBaseUser(id) {
            if (dialog.isShowing) dialog.dismiss()
            toast(it)
        }
    }

    private fun listenVM() {
        viewModel.bills.observe(this) {
            if (dialog.isShowing) dialog.dismiss()
            binding.run {
                if (it.isEmpty()) {
                    llNone.visibility = View.VISIBLE
                    rvBill.visibility = View.GONE
                } else {
                    llNone.visibility = View.GONE
                    rvBill.visibility = View.VISIBLE
                    bills.run {
                        clear()
                        addAll(it)
                    }
                    val price = "Total: ${viewModel.totalPrice}"
                    tvPrice.text = price
                }
            }

        }
    }
}
package com.example.balo.admin.managerbill

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.AdminBillAdapter
import com.example.balo.admin.managerorder.detail.AdminDetailOrderActivity
import com.example.balo.data.model.BillEntity
import com.example.balo.databinding.ActivityManagerBillBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.DialogUtil

class ManagerBillActivity : BaseActivity<ActivityManagerBillBinding>() {

    private lateinit var viewModel: ManagerBillVM

    private val bills = mutableListOf<BillEntity>()

    private val billAdapter by lazy {
        AdminBillAdapter(bills) { pos ->
            startActivity(AdminDetailOrderActivity.newIntent(this, bills[pos].idOrder))
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityManagerBillBinding =
        ActivityManagerBillBinding.inflate(inflate)

    override fun initView() = binding.run {
        rvBill.layoutManager = LinearLayoutManager(this@ManagerBillActivity)
        rvBill.adapter = billAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ManagerBillVM::class.java]
        listenVM()
        getBill()
    }

    override fun initListener() = binding.run {
        tvTime.setOnClickListener { handleTime() }
        tvTitle.setOnClickListener { finish() }
    }

    private fun handleTime() = binding.run {
        if (clLoading.visibility == View.GONE) {
            DialogUtil.showTimeDialog(this@ManagerBillActivity) {
                tvTime.text = it
                clLoading.visibility = View.VISIBLE
                viewModel.search(it)
            }
        }
    }

    private fun getBill() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getBills {
            binding.clLoading.visibility = View.GONE
            toast(it)
        }
    }

    private fun listenVM() {
        viewModel.bills.observe(this) {
            updateView(it)
        }

        viewModel.billsSearch.observe(this) {
            updateView(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateView(list: List<BillEntity>) = binding.run {
        clLoading.visibility = View.GONE
        val total = "${list.size} hóa đơn"
        tvTotal.text = total
        if (list.isNotEmpty()) {
            bills.run {
                clear()
                addAll(list)
            }
            billAdapter.notifyDataSetChanged()
            rvBill.visibility = View.VISIBLE
            llNone.visibility = View.GONE
        } else {
            rvBill.visibility = View.GONE
            llNone.visibility = View.VISIBLE
        }
    }
}
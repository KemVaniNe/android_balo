package com.example.balo.admin.adminbill

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.AdminBillAdapter
import com.example.balo.admin.managerorder.detail.AdminDetailOrderActivity
import com.example.balo.data.model.BillEntity
import com.example.balo.databinding.FragmentAdminOrderBinding
import com.example.balo.shareview.base.BaseFragment
import com.example.balo.utils.DialogUtil

class AdminBillFragment : BaseFragment<FragmentAdminOrderBinding>() {

    private lateinit var viewModel: AdminBillVM

    private val bills = mutableListOf<BillEntity>()

    private val billAdapter by lazy {
        AdminBillAdapter(bills) { pos ->
            context?.let {
                startActivity(AdminDetailOrderActivity.newIntent(it, bills[pos].idOrder))
            }
        }
    }

    override fun initView() = binding.run {
        rvBill.layoutManager = LinearLayoutManager(context)
        rvBill.adapter = billAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminBillVM::class.java]
        listenVM()
        getBill()
    }

    override fun initListener() = binding.run {
        tvTime.setOnClickListener { handleTime() }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminOrderBinding = FragmentAdminOrderBinding.inflate(inflater)

    private fun handleTime() = binding.run {
        if(clLoading.visibility == View.GONE) {
            context?.let { context ->
                DialogUtil.showTimeDialog(context) {
                    tvTime.text = it
                    clLoading.visibility = View.VISIBLE
                    viewModel.search(it)
                }
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
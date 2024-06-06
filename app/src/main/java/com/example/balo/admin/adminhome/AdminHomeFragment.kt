package com.example.balo.admin.adminhome

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.product.AdminMapsProductAdapter
import com.example.balo.admin.managerbrand.detail.AdminBrandDetailActivity
import com.example.balo.admin.managerproduct.detailproduct.AdminProductDetailActivity
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.FragmentAdminHomeBinding
import com.example.balo.shareview.base.BaseFragment
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class AdminHomeFragment : BaseFragment<FragmentAdminHomeBinding>() {

    private val products = mutableListOf<BaloEntity>()

    private var currentTypeFilter = Constants.TYPE_REVENUE

    companion object {
        const val REQUEST_CODE_CHANGE = 123
    }

    private val productAdapter by lazy {
        AdminMapsProductAdapter(products) {
            goToDetail(products[it].id)
        }
    }

    private lateinit var viewModel: AdminHomeVM
    override fun initView() = binding.run {
        rvProduct.layoutManager = LinearLayoutManager(context)
        rvProduct.adapter = productAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminHomeVM::class.java]
        listenBill()
        getBills()
        getProducts()
    }

    override fun initListener() {
        binding.tvFilter.setOnClickListener { handleFilter() }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminHomeBinding = FragmentAdminHomeBinding.inflate(inflater)

    private fun goToDetail(id: String) {
        context?.let {
            startActivityForResult(
                AdminProductDetailActivity.newIntent(it, id),
                REQUEST_CODE_CHANGE
            )
        }
    }

    private fun handleFilter() = binding.run {
        context?.let {
            Utils.showRevenueOption(it, currentTypeFilter) { type ->
                currentTypeFilter = type
                filter(type)
                when (type) {
                    Constants.TYPE_SELL -> {
                        tvFilter.text = getString(R.string.product_sell)
                    }

                    Constants.TYPE_PROFIT -> {
                        tvFilter.text = getString(R.string.profit)
                    }

                    Constants.TYPE_REVENUE -> {
                        tvFilter.text = getString(R.string.revenue)
                    }
                }
            }
        }
    }

    private fun filter(type: Int) {
        viewModel.filter(type)
    }

    private fun getBills() = binding.run {
        clLoading.visibility = View.VISIBLE
        viewModel.getBills {
            clLoading.visibility = View.GONE
            toast(it)
        }
    }

    private fun getProducts() = binding.run {
        clLoadingInventory.visibility = View.VISIBLE
        viewModel.getProducts {
            clLoadingInventory.visibility = View.GONE
            toast(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenBill() {
        viewModel.entriesBills.observe(this) {
            binding.run {
                clLoading.visibility = View.GONE
                charLine.visibility = View.VISIBLE
                drawChart(charLine, it)
            }
        }

        viewModel.products.observe(this) {
            binding.run {
                clLoadingInventory.visibility = View.GONE
                products.run {
                    clear()
                    addAll(it)
                }
                productAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun drawChart(charLine: LineChart, dataBills: Pair<List<Entry>, List<String>>) {
        val dates = dataBills.second
        val entries = dataBills.first
        val xAxisValueFormatter = IndexAxisValueFormatter(dates)
        charLine.xAxis.apply {
            valueFormatter = xAxisValueFormatter
            labelRotationAngle = -75f
            position = XAxis.XAxisPosition.BOTTOM
            setLabelCount(dates.size, false)
            textSize = 8f
            setDrawGridLines(false)
            textColor = ContextCompat.getColor(requireContext(), R.color.color_text)
        }

        charLine.axisLeft.apply {
            setDrawGridLines(false)
            textColor = ContextCompat.getColor(requireContext(), R.color.color_text)

        }

        charLine.axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            textColor = ContextCompat.getColor(requireContext(), R.color.color_text)
        }

        val dataSet = LineDataSet(entries, "Price").apply {
            lineWidth = 1f
            setDrawCircles(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            color = ContextCompat.getColor(requireContext(), R.color.color_button)
            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_fill)
        }

        charLine.apply {
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = entries.size.toFloat()
            data = LineData(dataSet)
            invalidate()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHANGE && resultCode == Activity.RESULT_OK) {
            getBills()
            getProducts()
        }
    }
}
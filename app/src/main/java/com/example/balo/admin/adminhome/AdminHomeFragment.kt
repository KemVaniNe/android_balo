package com.example.balo.admin.adminhome

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.product.AdminMapsProductAdapter
import com.example.balo.admin.managerproduct.detail.AdminDetailProductActivity
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.FragmentAdminHomeBinding
import com.example.balo.shareview.base.BaseFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class AdminHomeFragment : BaseFragment<FragmentAdminHomeBinding>() {

    private val products = mutableListOf<BaloEntity>()

    private val noneSells = mutableListOf<BaloEntity>()

    private val productAdapter by lazy {
        AdminMapsProductAdapter(products) {
            goToDetail(products[it].id)
        }
    }

    private val noneAdapter by lazy {
        AdminMapsProductAdapter(noneSells) {
            goToDetail(noneSells[it].id)
        }
    }

    private lateinit var viewModel: AdminHomeVM
    override fun initView() = binding.run {
        rvProduct.layoutManager = LinearLayoutManager(context)
        rvProduct.adapter = productAdapter
        rvNoneSell.layoutManager = LinearLayoutManager(context)
        rvNoneSell.adapter = noneAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminHomeVM::class.java]
        listenBill()
        getBills()
        getNoneSell()
    }

    override fun initListener() {
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminHomeBinding = FragmentAdminHomeBinding.inflate(inflater)

    private fun goToDetail(id: String) {
        context?.let { startActivity(AdminDetailProductActivity.newIntent(it, id)) }
    }

    private fun getBills() = binding.run {
        clLoading.visibility = View.VISIBLE
        clLoadingInventory.visibility = View.VISIBLE
        viewModel.getBills {
            clLoading.visibility = View.GONE
            clLoadingInventory.visibility = View.GONE
            toast(it)
        }
    }

    private fun getNoneSell() = binding.run {
        clLoadingNoneSell.visibility = View.VISIBLE
        viewModel.getNoneSell {
            clLoadingNoneSell.visibility = View.GONE
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

        viewModel.noneSell.observe(this) {
            binding.run {
                clLoadingNoneSell.visibility = View.GONE
                noneSells.run {
                    clear()
                    addAll(it)
                }
                noneAdapter.notifyDataSetChanged()
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
}
package com.example.balo.admin.managerproduct.detailproduct

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.CommentAdapter
import com.example.balo.admin.managerproduct.ManagerProductVM
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.ActivityAdminProductManagerBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Utils

class AdminProductDetailActivity : BaseActivity<ActivityAdminProductManagerBinding>() {

    private lateinit var viewModel: ManagerProductVM

    private var currentProduct: BaloEntity? = null

    private val comment = mutableListOf<String>()

    private var id = ""

    private val commentAdapter by lazy { CommentAdapter(comment) }

    companion object {

        const val ID = "id"
        const val REQUEST_CODE_CHANGE = 123
        fun newIntent(context: Context, id: String): Intent {
            return Intent(context, AdminProductDetailActivity::class.java).apply {
                putExtra(ID, id)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminProductManagerBinding =
        ActivityAdminProductManagerBinding.inflate(inflate)

    override fun initView() = binding.rvRate.run {
        layoutManager = LinearLayoutManager(this@AdminProductDetailActivity)
        adapter = commentAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ManagerProductVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(ID) && intent.getStringExtra(ID) != null) {
            id = intent.getStringExtra(ID)!!
            getProduct()
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnEdit.setOnClickListener { handleEdit() }
    }

    private fun handleEdit() {
        startActivityForResult(
            AdminProductEditActivity.newIntent(this, id), REQUEST_CODE_CHANGE
        )
    }

    private fun getProduct() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getProducts(id) {
            toastDialog(it)
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.productCurrent.observe(this) {
            if (it != null) {
                binding.clLoading.visibility = View.GONE
                currentProduct = it
                comment.run {
                    clear()
                    addAll(it.comment)
                }
                setView(it)
                commentAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setView(product: BaloEntity) = binding.run {
        Utils.displayBase64Image(product.pic, imgPic)
        tvName.text = product.name
        tvPrice.text = product.priceSell
        if (product.comment.size > 0) {
            tvRate.text = product.rate
            tvCountRate.text = "${product.comment.size} người đánh giá"
        } else {
            tvRate.text = "Chưa có đánh giá"
            tvCountRate.text = "Chưa có đánh giá"
            imgStar.visibility = View.GONE
        }
        tvSell.text = product.sell
        if ((product.quantitiy.toInt() - product.sell.toInt()) < 1) {
            llButton.visibility = View.GONE
            tvSoldOut.visibility = View.VISIBLE
        } else {
            llButton.visibility = View.VISIBLE
            tvSoldOut.visibility = View.GONE
        }
        tvValueDes.text = product.des
    }

    private fun toastDialog(notification: String) {
        toast(notification)
        binding.clLoading.visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHANGE && resultCode == RESULT_OK) {
            getProduct()
            setResult(RESULT_OK)
        }
    }
}
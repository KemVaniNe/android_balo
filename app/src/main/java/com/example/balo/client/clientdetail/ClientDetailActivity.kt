package com.example.balo.client.clientdetail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.CommentAdapter
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.ActivityClientDetailBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Utils

class ClientDetailActivity : BaseActivity<ActivityClientDetailBinding>() {

    private lateinit var viewModel: ClientDetailVM

    private lateinit var dialog: AlertDialog

    private var currentProduct: BaloEntity? = null

    private val comment = mutableListOf<String>()

    private val commentAdapter by lazy { CommentAdapter(comment) }

    companion object {

        const val KEY_DETAIL = "admin_brand"
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, ClientDetailActivity::class.java).apply {
                putExtra(KEY_DETAIL, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientDetailBinding =
        ActivityClientDetailBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        dialog = Utils.showProgressDialog(this)
        viewModel = ViewModelProvider(this)[ClientDetailVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_DETAIL) && intent.getStringExtra(KEY_DETAIL) != null) {
            getProduct(intent.getStringExtra(KEY_DETAIL)!!)
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnAdd.setOnClickListener {
            //TODO
        }
        btnCard.setOnClickListener {
            //TODO
        }
    }

    private fun getProduct(id: String) {
        if (!dialog.isShowing) dialog.show()
        viewModel.getProducts(id) {
            if (dialog.isShowing) dialog.dismiss()
            toast(it)
            finish()
        }
    }

    private fun listenVM() {
        viewModel.productCurrent.observe(this) {
            if (it != null) {
                if (dialog.isShowing) dialog.dismiss()
                currentProduct = it
                binding.run {
                    Utils.displayBase64Image(it.pic, imgPic)
                    tvName.text = it.name
                    tvPrice.text = it.priceSell
                    if (it.comment.size > 0) {
                        tvRate.text = it.rate
                        tvCountRate.text = "${it.comment.size} người đánh giá"
                    } else {
                        tvRate.text = "Chưa có đánh giá"
                        tvCountRate.text = "Chưa có đánh giá"
                        imgStar.visibility = View.GONE
                    }
                    tvSell.text = it.sell
                    if ((it.quantitiy.toInt() - it.sell.toInt()) < 1) {
                        llButton.visibility = View.GONE
                        tvSoldOut.visibility = View.VISIBLE
                    } else {
                        llButton.visibility = View.VISIBLE
                        tvSoldOut.visibility = View.GONE
                    }
                    comment.run {
                        clear()
                        addAll(it.comment)
                    }
                    tvValueDes.text = it.des
                    rvRate.layoutManager = LinearLayoutManager(this@ClientDetailActivity)
                    rvRate.adapter = commentAdapter
                }
            }
        }
    }
}
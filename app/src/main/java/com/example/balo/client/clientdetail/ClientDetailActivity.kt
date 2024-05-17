package com.example.balo.client.clientdetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.CommentAdapter
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.CartEntity
import com.example.balo.databinding.ActivityClientDetailBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils

class ClientDetailActivity : BaseActivity<ActivityClientDetailBinding>() {

    private lateinit var viewModel: ClientDetailVM

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

    override fun initView() = binding.rvRate.run {
        layoutManager = LinearLayoutManager(this@ClientDetailActivity)
        adapter = commentAdapter
    }

    override fun initData() {
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
        btnCard.setOnClickListener { handleCart() }
    }

    private fun getProduct(id: String) {
        if (!dialog.isShowing) dialog.show()
        viewModel.getProducts(id) {
            toastDialog(it)
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.productCurrent.observe(this) {
            if (it != null) {
                if (dialog.isShowing) dialog.dismiss()
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

    private fun handleCart() {
        if (Pref.idUser == Constants.ID_GUEST) {
            toast(getString(R.string.you_need_login))
        } else {
            Utils.showQuantityChoose(this, currentProduct!!) { quantity ->
                val cartEntity = CartEntity(
                    idUser = Pref.idUser,
                    idBalo = currentProduct!!.id,
                    quantity = quantity
                )
                if (!dialog.isShowing) dialog.show()
                createCart(cartEntity)
            }
        }
    }

    private fun createCart(cartEntity: CartEntity) {
        viewModel.createCart(cartEntity,
            handleExits = { toastDialog(getString(R.string.cart_exits)) },
            handleSuccess = { toastDialog(getString(R.string.add_success_cart)) },
            handleFail = { error -> toastDialog("ERROR: $error") },
            handleFull = { toastDialog(getString(R.string.cart_full)) })
    }

    private fun toastDialog(notification: String) {
        toast(notification)
        if (dialog.isShowing) dialog.dismiss()
    }
}
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
import com.example.balo.client.clientorder.ClientOrderActivity
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.databinding.ActivityClientDetailBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.google.gson.Gson

class ClientDetailActivity : BaseActivity<ActivityClientDetailBinding>() {

    private lateinit var viewModel: ClientDetailVM

    private var currentProduct: BaloEntity? = null

    private val comment = mutableListOf<String>()

    private var id = ""

    private val commentAdapter by lazy { CommentAdapter(comment) }

    companion object {

        const val KEY_DETAIL = "id"
        const val REQUEST_CODE_ORDER = 123
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, ClientDetailActivity::class.java).apply {
                putExtra(KEY_DETAIL, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientDetailBinding =
        ActivityClientDetailBinding.inflate(inflate)

    override fun initView() = binding.run {
        rvRate.layoutManager = LinearLayoutManager(this@ClientDetailActivity)
        rvRate.adapter = commentAdapter
        if (Pref.idUser == Constants.ID_GUEST) {
            llButton.visibility = View.GONE
        }
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientDetailVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_DETAIL) && intent.getStringExtra(KEY_DETAIL) != null) {
            id = intent.getStringExtra(KEY_DETAIL)!!
            getProduct()
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnAdd.setOnClickListener { handleAdd() }
        btnCard.setOnClickListener { handleCart() }
    }

    private fun handleCart() {
        if(binding.clLoading.visibility == View.GONE) {
            if (Pref.idUser == Constants.ID_GUEST) {
                toast(getString(R.string.you_need_login))
            } else {
                Utils.showQuantityChoose(this, currentProduct!!) { quantity ->
                    val cartEntity = CartEntity(
                        idUser = Pref.idUser,
                        idBalo = currentProduct!!.id,
                        quantity = Utils.stringToDouble(quantity),
                    )
                    binding.clLoading.visibility = View.VISIBLE
                    createCart(cartEntity)
                }
            }
        }
    }

    private fun handleAdd() {
        if(binding.clLoading.visibility == View.GONE) {
            if (Pref.idUser == Constants.ID_GUEST) {
                toast(getString(R.string.you_need_login))
            } else {
                Utils.showQuantityChoose(this, currentProduct!!) { quantity ->
                    val orderDetailEntity = OrderDetailEntity(
                        idBalo = currentProduct!!.id,
                        nameBalo = currentProduct!!.name,
                        quantity = Utils.stringToDouble(quantity),
                        price = currentProduct!!.priceSell,
                        totalPriceSellCurrent = currentProduct!!.totalSell
                    )
                    goToOrder(orderDetailEntity)
                }
            }
        }
    }

    private fun goToOrder(order: OrderDetailEntity) {
        startActivityForResult(
            ClientOrderActivity.newIntent(this, listOf(Gson().toJson(order)), emptyList()),
            REQUEST_CODE_ORDER
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
        tvPrice.text = product.priceSell.toString()
        if (product.comment.size > 0) {
            tvRate.text = product.rate.toString()
            tvCountRate.text = "${product.comment.size} người đánh giá"
        } else {
            tvRate.text = "Chưa có đánh giá"
            tvCountRate.text = "Chưa có đánh giá"
            imgStar.visibility = View.GONE
        }
        tvSell.text = product.sell.toString()
        if ((product.quantitiy.toInt() - product.sell.toInt()) < 1) {
            llButton.visibility = View.GONE
            tvSoldOut.visibility = View.VISIBLE
        } else {
            if (Pref.idUser == Constants.ID_GUEST) {
                llButton.visibility = View.GONE
            } else {
                llButton.visibility = View.VISIBLE
            }
            tvSoldOut.visibility = View.GONE
        }
        tvValueDes.text = product.des
    }

    private fun createCart(cartEntity: CartEntity) {
        viewModel.createCart(cartEntity,
            handleExits = { toastDialog(getString(R.string.cart_exits)) },
            handleSuccess = { toastDialog(getString(R.string.add_success_cart)) },
            handleFail = { toastDialog(it) },
            handleFull = { toastDialog(getString(R.string.cart_full)) })
    }

    private fun toastDialog(notification: String) {
        toast(notification)
        binding.clLoading.visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ORDER && resultCode == RESULT_OK) {
            getProduct()
        }
    }
}
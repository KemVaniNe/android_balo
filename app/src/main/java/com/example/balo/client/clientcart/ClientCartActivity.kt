package com.example.balo.client.clientcart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.ClientCartAdapter
import com.example.balo.data.model.CartEntity
import com.example.balo.databinding.ActivityClientCartBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils

class ClientCartActivity : BaseActivity<ActivityClientCartBinding>() {

    private var carts = mutableListOf<CartEntity>()

    private lateinit var viewModel: ClientCartVM

    private lateinit var dialog: AlertDialog

    private val cartAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ClientCartAdapter(carts, {
            //TODO
        }, {

        })
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientCartBinding =
        ActivityClientCartBinding.inflate(inflate)

    override fun initView() = binding.rvFavorite.run {
        layoutManager = LinearLayoutManager(context)
        adapter = cartAdapter
    }

    override fun initData() {
        dialog = Utils.showProgressDialog(this)
        viewModel = ViewModelProvider(this)[ClientCartVM::class.java]
        listenVM()
        updateCart()
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.carts.observe(this) {
            if (it != null) {
                carts.run {
                    clear()
                    addAll(it)
                }
                cartAdapter.notifyDataSetChanged()
                if(dialog.isShowing) dialog.dismiss()
            }
        }
    }

    private fun updateCart() {
        if (Pref.idUser != Constants.ID_GUEST) {
            if(!dialog.isShowing) dialog.show()
            viewModel.getCart(Pref.idUser) { error -> toast("ERROR: $error") }
        }
    }
}
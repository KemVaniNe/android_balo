package com.example.balo.admin.managerclient

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.AdminClientAdapter
import com.example.balo.admin.managerclient.detail.AdminClientDetailActivity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityManagerClientBinding
import com.example.balo.shareview.base.BaseActivity

class ManagerClientActivity : BaseActivity<ActivityManagerClientBinding>() {

    private val clients = mutableListOf<UserEntity>()

    private lateinit var viewModel: ManagerClientVM

    private val clientsAdapter by lazy {
        AdminClientAdapter(clients) { pos ->
            goToDetail(clients[pos])
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityManagerClientBinding =
        ActivityManagerClientBinding.inflate(inflate)

    override fun initView() = binding.rvUser.run {
        layoutManager = LinearLayoutManager(this@ManagerClientActivity)
        adapter = clientsAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ManagerClientVM::class.java]
        listenVM()
        getClient()
    }

    override fun initListener() {
        binding.tvTitle.setOnClickListener { finish() }
    }

    private fun getClient() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getClients {
            if (dialog.isShowing) dialog.dismiss()
            toast(it)
        }
    }

    private fun goToDetail(client: UserEntity) {
        startActivity(
            Intent(
                AdminClientDetailActivity.newIntent(
                    context = this, name = client.username, id = client.id
                )
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.clients.observe(this) {
            if (dialog.isShowing) dialog.dismiss()
            clients.run {
                clear()
                addAll(it)
            }
            clientsAdapter.notifyDataSetChanged()
        }
    }
}
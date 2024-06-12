package com.example.balo.admin.adminnotification

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.notification.NotificationAdapter
import com.example.balo.admin.adminnotification.detail.AdminNotificationDetailActivity
import com.example.balo.data.model.NotificationEntity
import com.example.balo.databinding.FragmentAdminNotificationBinding
import com.example.balo.shareview.base.BaseFragment
import com.google.gson.Gson

class AdminNotificationFragment : BaseFragment<FragmentAdminNotificationBinding>() {

    companion object {

        const val REQUEST_CODE_DETAIL = 123
    }

    private lateinit var viewModel: AdminNotificationVM

    private val notifications = mutableListOf<NotificationEntity>()

    private val notificationAdapter by lazy {
        NotificationAdapter(notifications) { pos ->
            goToDetail(notifications[pos])
        }
    }

    override fun initView() = binding.run {
        rvNotification.layoutManager = LinearLayoutManager(context)
        rvNotification.adapter = notificationAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminNotificationVM::class.java]
        listenVM()
        getNotification()
    }

    override fun initListener() {
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminNotificationBinding = FragmentAdminNotificationBinding.inflate(inflater)

    private fun getNotification() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getNotificationAdmin {
            binding.clLoading.visibility = View.GONE
            toast(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.notification.observe(this) {
            notifications.run {
                clear()
                addAll(it)
            }
            notificationAdapter.notifyDataSetChanged()
            binding.clLoading.visibility = View.GONE
        }
    }

    private fun goToDetail(item: NotificationEntity) {
        context?.let {
            startActivityForResult(
                AdminNotificationDetailActivity.newIntent(it, Gson().toJson(item)),
                REQUEST_CODE_DETAIL
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAIL) {
            getNotification()
        }
    }
}
package com.example.balo.adapter.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.R
import com.example.balo.data.model.NotificationEntity
import com.example.balo.databinding.ItemNotificationBinding

class NotificationAdapter(
    private var list: List<NotificationEntity>,
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.VH>() {
    inner class VH(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun onBind(item: NotificationEntity) {
            binding.run {
                tvDate.text = item.datatime
                tvDes.text = item.notification
                val txtOrder = "MÃ ĐƠN HÀNG: ${item.idOrder}"
                tvOrder.text = txtOrder
                if (item.isSeen) {
                    root.background = null
                } else {
                    root.setBackgroundResource(R.drawable.bg_option)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    ).apply {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.invoke(adapterPosition)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }
}
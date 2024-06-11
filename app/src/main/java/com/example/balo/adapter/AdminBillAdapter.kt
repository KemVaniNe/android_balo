package com.example.balo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.BillEntity
import com.example.balo.databinding.ItemBillBinding

class AdminBillAdapter(
    private var list: List<BillEntity>,
    private val listener: (Int) -> Unit,
) : RecyclerView.Adapter<AdminBillAdapter.VH>() {
    inner class VH(val binding: ItemBillBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun onBind(item: BillEntity) {
            binding.run {
                tvDate.text = item.date
                tvPrice.text = item.totalPrice.toString()
                tvStatus.text = item.statusOrder
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemBillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    ).apply {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.invoke(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }
}
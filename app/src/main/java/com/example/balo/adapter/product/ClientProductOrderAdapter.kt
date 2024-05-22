package com.example.balo.adapter.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.OrderDetail
import com.example.balo.databinding.ItemProductAdminBinding
import com.example.balo.utils.Utils

class ClientProductOrderAdapter(
    private var list: List<OrderDetail>
) : RecyclerView.Adapter<ClientProductOrderAdapter.VH>() {
    inner class VH(val binding: ItemProductAdminBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: OrderDetail) {
            binding.run {
                Utils.displayBase64Image(item.picProduct, imgPic)
                tvName.text = item.nameBalo
                tvPrice.text = item.price
                tvQuantity.text = item.quantity
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemProductAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }
}
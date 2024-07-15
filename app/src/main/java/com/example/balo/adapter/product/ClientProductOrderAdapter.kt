package com.example.balo.adapter.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.databinding.ItemProductAdminBinding
import com.example.balo.utils.Utils

class ClientProductOrderAdapter(
    private var list: List<OrderDetailEntity>
) : RecyclerView.Adapter<ClientProductOrderAdapter.VH>() {
    inner class VH(val binding: ItemProductAdminBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: OrderDetailEntity) {
            binding.run {
                Utils.displayBase64Image(item.picProduct, imgPic)
                tvName.text = item.nameBalo
                tvPrice.text = item.price.toString()
                tvQuantity.text = item.quantity.toString()
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
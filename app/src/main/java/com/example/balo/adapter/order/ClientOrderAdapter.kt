package com.example.balo.adapter.order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.OrderEntity
import com.example.balo.databinding.ItemOrderBinding
import com.example.balo.utils.Utils

class ClientOrderAdapter(
    private var list: List<OrderEntity>,
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<ClientOrderAdapter.VH>() {
    inner class VH(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun onBind(item: OrderEntity) {
            binding.run {
                val detail = item.detail.firstOrNull()
                if (detail != null) {
                    tvName.text = detail.nameBalo
                    tvPrice.text = detail.price.toString()
                    Utils.displayBase64Image(detail.picProduct, imgPic)
                    tvQuantity.text = detail.quantity.toString()
                }
                val total = "${item.detail.size} sản phẩm"
                tvTotalProduct.text = total
                tvStatus.text = item.statusOrder
                val price = item.totalPrice + item.priceShip
                tvTotalPrice.text = price.toString()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
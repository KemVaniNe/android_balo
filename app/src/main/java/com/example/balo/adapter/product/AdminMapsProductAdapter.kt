package com.example.balo.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.ItemProductPriceBinding
import com.example.balo.utils.Utils

class AdminMapsProductAdapter(
    private var list: List<BaloEntity>,
    private var listener: (Int) -> Unit
) : RecyclerView.Adapter<AdminMapsProductAdapter.VH>() {
    inner class VH(val binding: ItemProductPriceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: BaloEntity) {
            binding.run {
                tvName.text = item.name
                val priceSell = "Doanh thu: ${item.totalSell}"
                tvPrice.text = priceSell
                val sell = "Đã bán: ${item.sell}"
                tvSell.text = sell
                val profit = Utils.getProfit(item)
                val profitText = "Lợi nhuận: $profit"
                tvProfit.text = profitText
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemProductPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
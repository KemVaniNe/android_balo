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
                val totalImport = Utils.stringToInt(item.totalImport).toFloat()
                val quantity = Utils.stringToInt(item.quantitiy).toFloat()
                val totalPrice = Utils.stringToInt(item.totalSell).toFloat()
                tvName.text = item.name
                val priceSell = "Doanh thu: ${totalPrice}"
                tvPrice.text = priceSell
                val sell = "Đã bán: ${item.sell}"
                tvSell.text = sell
                val priceImportProduct = totalImport / (if (quantity == 0f) 1f else quantity)
                val profit = totalPrice - priceImportProduct * Utils.stringToInt(item.sell)
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
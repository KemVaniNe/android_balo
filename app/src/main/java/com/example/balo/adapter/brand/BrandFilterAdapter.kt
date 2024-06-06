package com.example.balo.adapter.brand

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.R
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ItemBrandFilterBinding
import com.example.balo.databinding.ItemTypeBinding
import com.example.balo.utils.Utils

class BrandFilterAdapter(
    private var list: List<BrandEntity>,
    private val listener: (Int) -> Unit,
) : RecyclerView.Adapter<BrandFilterAdapter.VH>() {
    inner class VH(val binding: ItemBrandFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: BrandEntity) {
            binding.run {
                tvNameBrand.text = item.name
                root.setBackgroundResource(if (item.isSelected) R.drawable.bg_btn else R.drawable.bg_option)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemBrandFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
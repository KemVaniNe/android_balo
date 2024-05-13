package com.example.balo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.ItemProductBinding

class FavoriteAdapter(
    private var list: List<BaloEntity>,
    private val listener: (Int) -> Unit,
) : RecyclerView.Adapter<FavoriteAdapter.VH>() {
    inner class VH(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: BaloEntity) {
            binding.run {
                tvName.text = item.name
                tvPrice.text = item.priceSell
                clNum.visibility = View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
package com.example.balo.ui.user.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ItemTypeBinding
import com.example.balo.utils.Utils

class BrandAdapter(
    private var list: List<BrandEntity>,
    private val listener: (Int) -> Unit,
) : RecyclerView.Adapter<BrandAdapter.VH>() {
    inner class VH(val binding: ItemTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: BrandEntity) {
            binding.run {
                Utils.displayBase64Image(item.pic, imgPic)
                tvDes.text = item.name
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
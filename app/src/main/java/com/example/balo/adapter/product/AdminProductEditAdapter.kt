package com.example.balo.adapter.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.ItemProductEditBinding
import com.example.balo.utils.Utils

class AdminProductEditAdapter(
    private var list: List<BaloEntity>,
    private val listener: (Int) -> Unit,
    private val onCheckBox: (Pair<Boolean, Int>) -> Unit,
) : RecyclerView.Adapter<AdminProductEditAdapter.VH>() {
    inner class VH(val binding: ItemProductEditBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: BaloEntity) {
            binding.run {
                Utils.displayBase64Image(item.pic, imgPic)
                tvName.text = item.name
                tvPrice.text = item.priceSell.toString()
                val available = item.quantitiy - item.sell
                tvQuantity.text = available.toString()
                cbDelete.isChecked = item.isSelected
                cbDelete.setOnClickListener {
                    onCheckBox.invoke(Pair(cbDelete.isChecked, adapterPosition))
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemProductEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
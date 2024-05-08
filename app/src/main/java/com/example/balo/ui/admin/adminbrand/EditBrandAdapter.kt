package com.example.balo.ui.admin.adminbrand

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ItemBrandEditBinding
import com.example.balo.utils.Utils

class EditBrandAdapter(
    private var list: List<BrandEntity>,
    private val listener: (Int) -> Unit,
    private val onCheckBox: (Pair<Boolean, String>) -> Unit,
) : RecyclerView.Adapter<EditBrandAdapter.VH>() {
    inner class VH(val binding: ItemBrandEditBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: BrandEntity) {
            binding.run {
                Utils.displayBase64Image(item.pic, imgPic)
                tvDes.text = item.name
                cbDelete.setOnClickListener {
                    onCheckBox.invoke(Pair(cbDelete.isChecked, item.id))
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemBrandEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
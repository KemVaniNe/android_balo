package com.example.balo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ItemClientBinding
import com.example.balo.utils.Utils

class AdminClientAdapter(
    private var list: List<UserEntity>,
    private val listener: (Int) -> Unit,
) : RecyclerView.Adapter<AdminClientAdapter.VH>() {
    inner class VH(val binding: ItemClientBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun onBind(item: UserEntity) {
            binding.run {
                tvName.text = item.username
                tvPhone.text = item.phone
                Utils.displayUserAvatar(item.pic, imgAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
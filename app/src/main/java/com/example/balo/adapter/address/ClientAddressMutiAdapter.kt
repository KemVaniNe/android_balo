package com.example.balo.adapter.address

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.R
import com.example.balo.data.model.AddressSelect
import com.example.balo.databinding.ItemAddressChooseBinding
import com.example.balo.utils.Pref

class ClientAddressMutiAdapter(
    private var list: List<AddressSelect>,
    private val listener: (Int) -> Unit,
) : RecyclerView.Adapter<ClientAddressMutiAdapter.VH>() {
    inner class VH(val binding: ItemAddressChooseBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun onBind(item: AddressSelect) {
            binding.run {
                root.setBackgroundResource(if (item.isSelected) R.drawable.bg_btn else R.drawable.bg_option)
                tvAddress.text = item.address
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemAddressChooseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
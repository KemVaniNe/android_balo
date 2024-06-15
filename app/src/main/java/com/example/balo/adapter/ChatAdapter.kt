package com.example.balo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.ChatEntity
import com.example.balo.databinding.ItemMessBinding

class ChatAdapter(
    private var list: List<ChatEntity>,
) : RecyclerView.Adapter<ChatAdapter.VH>() {
    inner class VH(val binding: ItemMessBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun onBind(item: ChatEntity) {
            binding.run {
                if (item.isUser) {
                    llMessUser.visibility = View.VISIBLE
                    llMessBot.visibility = View.GONE
                    tvMessUser.text = item.mess
                } else {
                    llMessUser.visibility = View.GONE
                    llMessBot.visibility = View.VISIBLE
                    tvMessBot.text = item.mess

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemMessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }
}
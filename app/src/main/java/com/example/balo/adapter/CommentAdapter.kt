package com.example.balo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.databinding.ItemCommentBinding

class CommentAdapter(
    private var list: List<String>,
) : RecyclerView.Adapter<CommentAdapter.VH>() {
    inner class VH(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: String) {
            binding.run {
                val rate = item.first().digitToIntOrNull() ?: 5
                val comment = item.drop(1).trim()
                if (comment == "") {
                    tvComment.visibility = View.GONE
                } else {
                    tvComment.text = comment
                }
                when (rate) {
                    1 -> {
                        img2.visibility = View.GONE
                        img3.visibility = View.GONE
                        img4.visibility = View.GONE
                        img5.visibility = View.GONE
                    }

                    2 -> {
                        img3.visibility = View.GONE
                        img4.visibility = View.GONE
                        img5.visibility = View.GONE
                    }

                    3 -> {
                        img4.visibility = View.GONE
                        img5.visibility = View.GONE
                    }

                    4 -> {
                        img5.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }
}
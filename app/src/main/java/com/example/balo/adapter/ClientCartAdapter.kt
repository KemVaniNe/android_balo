package com.example.balo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.CartEntity
import com.example.balo.databinding.ItemProductBinding
import com.example.balo.utils.Utils

class ClientCartAdapter(
    private var list: List<CartEntity>,
    private val listener: (Int) -> Unit,
    private val listenerDel: () -> Unit
) : RecyclerView.Adapter<ClientCartAdapter.VH>() {
    inner class VH(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: CartEntity) {
            binding.run {
                tvName.text = item.nameBalo
                tvPrice.text = item.price
                Utils.displayBase64Image(item.pic, imgPic)
                val max = item.max.toIntOrNull() ?: 0
                tvNum.text = item.quantity
                if (max == 0) {
                    tvSoldOut.visibility = View.VISIBLE
                    clNum.visibility = View.GONE
                } else if ((item.quantity.toIntOrNull() ?: 0) > max) {
                    tvNum.text = max.toString()
                    imgAdd.visibility = View.GONE
                } else {
                    tvNum.text = item.quantity
                }
                imgAdd.setOnClickListener {
                    imgMinus.visibility = View.VISIBLE
                    val nextValue = tvNum.text.toString().toInt() + 1
                    tvNum.text = nextValue.toString()
                    if (nextValue == max) imgAdd.visibility = View.INVISIBLE
                }
                imgMinus.setOnClickListener {
                    if (tvNum.text.toString().toInt() == 1) {
                        listenerDel.invoke()
                    } else {
                        imgAdd.visibility = View.VISIBLE
                        val nextValue = tvNum.text.toString().toInt() - 1
                        tvNum.text = nextValue.toString()
                    }
                }
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
package com.example.balo.adapter.order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.databinding.ItemOrderDetailBinding
import com.example.balo.utils.Utils

class ShareOrderDetailAdapter(
    private var list: List<OrderDetailEntity>,
    private var isShowRate: Boolean,
    private var isUser: Boolean,
    private val listener: (Int) -> Unit,
    private var listenerRate: (Int) -> Unit
) : RecyclerView.Adapter<ShareOrderDetailAdapter.VH>() {
    inner class VH(val binding: ItemOrderDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun onBind(item: OrderDetailEntity) {
            binding.run {
                tvDetail.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listenerRate.invoke(adapterPosition)
                    }
                }
                clTop.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.invoke(adapterPosition)
                    }
                }
                clRate.visibility = if (isShowRate && isUser) View.VISIBLE else View.GONE
                tvName.text = item.nameBalo
                tvPrice.text = item.price
                Utils.displayBase64Image(item.picProduct, imgPic)
                tvQuantity.text = item.quantity
                if (item.rate == "0") {
                    tvDetail.visibility = View.VISIBLE
                    llDes.visibility = View.INVISIBLE
                } else {
                    tvDetail.visibility = View.GONE
                    llDes.visibility = View.VISIBLE
                    if (item.comment != "") {
                        tvComment.text = item.comment
                    } else {
                        tvComment.visibility = View.GONE
                    }
                    when (Utils.stringToInt(item.rate)) {
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

                        else -> {
                            llDes.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRate(isRate: Boolean) {
        isShowRate = isRate
        notifyDataSetChanged()
    }
}
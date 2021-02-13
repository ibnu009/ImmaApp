package com.pjb.immaapp.ui.purchaseorder.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.po.ItemPurchaseOrder
import com.pjb.immaapp.databinding.DetailItemBinding
import com.pjb.immaapp.utils.ConverterHelper

class DataItemPoPagedListAdapter :
    PagedListAdapter<ItemPurchaseOrder, DataItemPoPagedListAdapter.DataItemPoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataItemPoViewHolder {
        val binding = DetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataItemPoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataItemPoViewHolder, position: Int) {
        getItem(position)?.let { po ->
            holder.bind(po)
        }
    }

    inner class DataItemPoViewHolder(private val binding: DetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(po: ItemPurchaseOrder) {
            with(binding) {
                binding.txJobTitle.text = po.namaMaterial
                val quantity = po.quantity.toString()
                binding.txQty.text = binding.root.context.getString(R.string.x1, quantity)
                val total = (po.quantity * po.unitCost)
                val formattedTotal = ConverterHelper().convertAnggaranFormat(total)
                binding.txTotalAnggaranDetaiItem.text = binding.root.context.getString(R.string.anggaran_po, formattedTotal)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ItemPurchaseOrder> = object :
            DiffUtil.ItemCallback<ItemPurchaseOrder>() {
            override fun areItemsTheSame(oldItem: ItemPurchaseOrder, newItem: ItemPurchaseOrder): Boolean {
                return oldItem.namaMaterial == newItem.namaMaterial && oldItem.namaMaterial == newItem.namaMaterial
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ItemPurchaseOrder,
                newItem: ItemPurchaseOrder
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}
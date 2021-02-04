package com.pjb.immaapp.ui.purchaseorder.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.po.ItemPurchaseOrder
import kotlinx.android.synthetic.main.detail_item.view.*
import kotlinx.android.synthetic.main.po_item.view.*
import kotlinx.android.synthetic.main.po_item.view.tx_job_title

class DataItemPoPagedListAdapter() :
    PagedListAdapter<ItemPurchaseOrder, DataItemPoPagedListAdapter.DataItemPoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataItemPoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false)
        return DataItemPoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataItemPoViewHolder, position: Int) {
        getItem(position)?.let { po ->
            holder.bind(po)
        }
    }

    inner class DataItemPoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(po: ItemPurchaseOrder) {
            with(itemView) {
                tx_job_title.text = po.namaMaterial
                val quantity = po.quantity.toString()
                tx_qty.text = context.getString(R.string.x1, quantity)

                tx_total_anggaran_detai_item.text = po.unitCost
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
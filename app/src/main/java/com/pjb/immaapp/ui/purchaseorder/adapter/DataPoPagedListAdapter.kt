package com.pjb.immaapp.ui.purchaseorder.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.PurchaseOrder
import kotlinx.android.synthetic.main.po_item.view.*

class DataPoPagedListAdapter() :
    PagedListAdapter<PurchaseOrder, DataPoPagedListAdapter.DataPoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataPoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.po_item, parent, false)
        return DataPoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataPoViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bind(movie)
//            holder.itemView.setOnClickListener {
//                onClickedAction.onClicked(movie.movieId)
//            }
        }
    }

    inner class DataPoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(po: PurchaseOrder) {
            with(itemView) {
                tx_job_title.text = po.jobTitle
                tx_tanggal_permintaan.text = po.orderDate
                tx_nomor_po.text = po.ponum
                tx_total_anggaran.text = po.anggaran.toString()

                img_icon_item_po.background =
                    AppCompatResources.getDrawable(itemView.context, R.drawable.bg_icon_green)
                img_icon_item_po.setImageResource(R.drawable.ic_time)
            }
        }
    }


    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<PurchaseOrder> = object :
            DiffUtil.ItemCallback<PurchaseOrder>() {
            override fun areItemsTheSame(oldItem: PurchaseOrder, newItem: PurchaseOrder): Boolean {
                return oldItem.ponum == newItem.ponum && oldItem.ponum == newItem.ponum
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: PurchaseOrder,
                newItem: PurchaseOrder
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}
package com.pjb.immaapp.ui.usulanpermintaanbarang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.upb.ItemPermintaanBarang
import kotlinx.android.synthetic.main.detail_item.view.*

class DataItemUpbPagedListAdapter:
    PagedListAdapter<ItemPermintaanBarang, DataItemUpbPagedListAdapter.DataItemUpbViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataItemUpbPagedListAdapter.DataItemUpbViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false)

        return DataItemUpbViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DataItemUpbPagedListAdapter.DataItemUpbViewHolder,
        position: Int
    ) {
        getItem(position)?.let { upb ->
            holder.bind(upb)
        }
    }

    inner class DataItemUpbViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(upb : ItemPermintaanBarang){
            with(itemView){
                tx_job_title.text = upb.barang
                tx_qty.text = context.getString(R.string.x1, upb.quantity.toString())
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ItemPermintaanBarang> = object :
        DiffUtil.ItemCallback<ItemPermintaanBarang>() {
            override fun areItemsTheSame(
                oldItem: ItemPermintaanBarang,
                newItem: ItemPermintaanBarang
            ): Boolean {
                return oldItem.barang == newItem.barang && oldItem.barang == newItem.barang
            }

            override fun areContentsTheSame(
                oldItem: ItemPermintaanBarang,
                newItem: ItemPermintaanBarang
            ): Boolean {
                return oldItem.barang == newItem.barang
            }

        }
    }

}
package com.pjb.immaapp.ui.usulanpermintaanbarang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.PermintaanBarang
import kotlinx.android.synthetic.main.usulan_item.view.*

class DataUpbPagedListAdapter() :
    PagedListAdapter<PermintaanBarang, DataUpbPagedListAdapter.DataUpbViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataUpbPagedListAdapter.DataUpbViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usulan_item, parent, false)

        return DataUpbViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DataUpbPagedListAdapter.DataUpbViewHolder,
        position: Int
    ) {
        getItem(position)?.let { upb ->
            holder.bind(upb)
        }
    }

    inner class DataUpbViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(upb : PermintaanBarang){
            with(itemView){
                tx_no_permintaan.text = upb.noPermintaan
                tx_tanggal_permintaan.text = upb.tanggalPermohonan
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK : DiffUtil.ItemCallback<PermintaanBarang> = object :
        DiffUtil.ItemCallback<PermintaanBarang>(){
            override fun areItemsTheSame(
                oldItem: PermintaanBarang,
                newItem: PermintaanBarang
            ): Boolean {
                return oldItem.no == newItem.no && oldItem.no == newItem.no
            }

            override fun areContentsTheSame(
                oldItem: PermintaanBarang,
                newItem: PermintaanBarang
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}
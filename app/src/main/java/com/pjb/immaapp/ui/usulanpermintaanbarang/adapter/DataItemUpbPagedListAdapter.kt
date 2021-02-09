package com.pjb.immaapp.ui.usulanpermintaanbarang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.upb.ItemPermintaanBarang
import com.pjb.immaapp.databinding.DetailItemBinding

class DataItemUpbPagedListAdapter:
    PagedListAdapter<ItemPermintaanBarang, DataItemUpbPagedListAdapter.DataItemUpbViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataItemUpbPagedListAdapter.DataItemUpbViewHolder {
        val binding = DetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataItemUpbViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DataItemUpbPagedListAdapter.DataItemUpbViewHolder,
        position: Int
    ) {
        getItem(position)?.let { upb ->
            holder.bind(upb)
        }
    }

    inner class DataItemUpbViewHolder(private val binding: DetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(upb : ItemPermintaanBarang){
            with(binding){
                binding.txJobTitle.text = upb.barang
                binding.txQty.text = binding.root.context.getString(R.string.x1, upb.quantity.toString())
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
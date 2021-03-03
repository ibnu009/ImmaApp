package com.pjb.immaapp.ui.usulanpermintaanbarang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.data.entity.upb.PermintaanBarang
import com.pjb.immaapp.databinding.UsulanItemBinding
import com.pjb.immaapp.handler.OnClickedActionDataUpb

class   DataUpbPagedListAdapter(private val onClickedAction : OnClickedActionDataUpb) :
    PagedListAdapter<PermintaanBarang, DataUpbPagedListAdapter.DataUpbViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataUpbPagedListAdapter.DataUpbViewHolder {
        val binding = UsulanItemBinding.inflate(LayoutInflater.from(parent.context.applicationContext), parent, false)
        return DataUpbViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataUpbViewHolder, position: Int) {
        getItem(position)?.let { upb ->
            holder.bind(upb)
            holder.itemView.setOnClickListener{
                onClickedAction.onClicked(upb.idPermintaan)
            }
        }
    }

    inner class DataUpbViewHolder(private val binding: UsulanItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(upb : PermintaanBarang){
            with(binding){
                binding.txTanggalPermintaan.text = upb.tanggalPermohonan
                binding.txNoPermintaan.text = upb.noPermintaan
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK : DiffUtil.ItemCallback<PermintaanBarang> = object :
            DiffUtil.ItemCallback<PermintaanBarang>() {
            override fun areItemsTheSame(oldItem: PermintaanBarang, newItem: PermintaanBarang): Boolean {
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
package com.pjb.immaapp.ui.usulanpermintaanbarang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.data.entity.local.supplier.Suppliers
import com.pjb.immaapp.data.entity.upb.Supplier
import com.pjb.immaapp.databinding.SupplierListItemBinding
import com.pjb.immaapp.handler.OnClickedActionDataSupplier
import timber.log.Timber

class SupplierPagedListAdapter(private val onClickedActionDataSupplier: OnClickedActionDataSupplier) :
    PagingDataAdapter<Suppliers.SupplierEntity, SupplierPagedListAdapter.DataSupplierViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: DataSupplierViewHolder, position: Int) {
        getItem(position)?.let { supplier ->
            holder.bind(supplier)
            holder.itemView.setOnClickListener{
                onClickedActionDataSupplier.onClicked(supplier.id, supplier.nama)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataSupplierViewHolder {
        val binding =
            SupplierListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataSupplierViewHolder(binding)
    }

    inner class DataSupplierViewHolder(private val binding: SupplierListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(supplier: Suppliers.SupplierEntity) {
            Timber.d("check data ${supplier.id_supplier}")
            with(binding) {
                binding.txNamaSupplier.text = supplier.nama
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Suppliers.SupplierEntity> =
            object : DiffUtil.ItemCallback<Suppliers.SupplierEntity>() {
                override fun areItemsTheSame(oldItem: Suppliers.SupplierEntity, newItem: Suppliers.SupplierEntity): Boolean {
                    return oldItem.id == newItem.id && oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Suppliers.SupplierEntity, newItem: Suppliers.SupplierEntity): Boolean {
                    return oldItem == newItem
                }

            }
    }

}
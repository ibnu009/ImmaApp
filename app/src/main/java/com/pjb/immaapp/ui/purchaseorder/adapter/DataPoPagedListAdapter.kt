package com.pjb.immaapp.ui.purchaseorder.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders
import com.pjb.immaapp.data.entity.po.PurchaseOrder
import com.pjb.immaapp.databinding.PoItemBinding
import com.pjb.immaapp.handler.OnClickedActionDataPo
import com.pjb.immaapp.utils.ConverterHelper
import java.text.SimpleDateFormat
import java.util.*

class DataPoPagedListAdapter(private val onClickedAction: OnClickedActionDataPo) :
    PagingDataAdapter<PurchaseOrders.PurchaseOrderEntity, DataPoPagedListAdapter.DataPoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataPoViewHolder {
        val binding = PoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataPoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataPoViewHolder, position: Int) {
        getItem(position)?.let { po ->
            holder.bind(po)
            holder.itemView.setOnClickListener {
                onClickedAction.onClicked(po.encodePonum)
            }
        }
    }

    inner class DataPoViewHolder(private val binding: PoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(po: PurchaseOrders.PurchaseOrderEntity) {
            with(binding) {
                binding.txJobTitle.text = po.jobTitle
                binding.txTanggalPermintaan.text = po.orderDate
                binding.txNomorPo.text = po.ponum

                val anggaranFix = ConverterHelper().convertAnggaranFormat(po.anggaran)
                binding.txTotalAnggaran.text = binding.root.context.getString(R.string.anggaran_po, anggaranFix)
                dueDateChecker(po.orderDate, binding)
            }
        }
    }

    private fun dueDateChecker(dueDate: String, binding: PoItemBinding) {
        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(calendar.time)

        if (currentDate > dueDate) {
            binding.imgIconItemPo.background =
                AppCompatResources.getDrawable(binding.root.context, R.drawable.bg_icon_red)
            binding.imgIconItemPo.setImageResource(R.drawable.ic_time)
        } else {
            binding.imgIconItemPo.background =
                AppCompatResources.getDrawable(binding.root.context, R.drawable.bg_icon_green)
            binding.imgIconItemPo.setImageResource(R.drawable.ic_check)
        }
    }


    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<PurchaseOrders.PurchaseOrderEntity> = object :
            DiffUtil.ItemCallback<PurchaseOrders.PurchaseOrderEntity>() {
            override fun areItemsTheSame(oldItem: PurchaseOrders.PurchaseOrderEntity, newItem: PurchaseOrders.PurchaseOrderEntity): Boolean {
                return oldItem.idPo == newItem.idPo && oldItem.idPo == newItem.idPo
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: PurchaseOrders.PurchaseOrderEntity,
                newItem: PurchaseOrders.PurchaseOrderEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}
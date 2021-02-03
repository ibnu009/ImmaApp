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
import com.pjb.immaapp.data.entity.po.PurchaseOrder
import com.pjb.immaapp.handler.OnClickedActionDataPo
import kotlinx.android.synthetic.main.po_item.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DataPoPagedListAdapter(private val onClickedAction: OnClickedActionDataPo) :
    PagedListAdapter<PurchaseOrder, DataPoPagedListAdapter.DataPoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataPoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.po_item, parent, false)
        return DataPoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataPoViewHolder, position: Int) {
        getItem(position)?.let { po ->
            holder.bind(po)
            holder.itemView.setOnClickListener {
                onClickedAction.onClicked(po.encodePonum)
            }
        }
    }

    inner class DataPoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(po: PurchaseOrder) {
            with(itemView) {
                tx_job_title.text = po.jobTitle
                tx_tanggal_permintaan.text = po.orderDate
                tx_nomor_po.text = po.ponum

                val decimalFormat: DecimalFormat = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
                decimalFormat.applyPattern("#,###,###,###")
                val anggaranFix = decimalFormat.format(po.anggaran)

                tx_total_anggaran.text = context.getString(R.string.anggaran_po, anggaranFix)

                dueDateChecker(po.orderDate, itemView)
            }
        }
    }

    private fun dueDateChecker(dueDate: String, itemView: View) {
        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(calendar.time)

        if (currentDate > dueDate) {
            itemView.img_icon_item_po.background =
                AppCompatResources.getDrawable(itemView.context, R.drawable.bg_icon_red)
            itemView.img_icon_item_po.setImageResource(R.drawable.ic_time)
        } else {
            itemView.img_icon_item_po.background =
                AppCompatResources.getDrawable(itemView.context, R.drawable.bg_icon_green)
            itemView.img_icon_item_po.setImageResource(R.drawable.ic_check)
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
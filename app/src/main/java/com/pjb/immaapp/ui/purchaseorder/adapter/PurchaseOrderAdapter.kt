package com.pjb.immaapp.ui.purchaseorder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.PurchaseOrder
import kotlinx.android.synthetic.main.po_item.view.*

class PurchaseOrderAdapter : RecyclerView.Adapter<PurchaseOrderAdapter.ViewHolder>(){

    private val listPurchaseOrder = ArrayList<PurchaseOrder>()

    fun setList(list: List<PurchaseOrder>){
        listPurchaseOrder.addAll(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseOrderAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.po_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchaseOrderAdapter.ViewHolder, position: Int) {
        val list = listPurchaseOrder[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int = listPurchaseOrder.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(purchaseOrder: PurchaseOrder){
            with(itemView){
                tx_job_title.text = purchaseOrder.jobTitle
                tx_tanggal_permintaan.text = purchaseOrder.orderDate
                tx_nomor_po.text = purchaseOrder.ponum
                tx_total_anggaran.text = purchaseOrder.anggaran.toString()
            }
        }
    }

}
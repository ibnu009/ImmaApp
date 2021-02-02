package com.pjb.immaapp.ui.gudangpermintaanbarang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.GudangPermintaanBarang
import kotlinx.android.synthetic.main.gudang_item.view.*

class GudangPermintaanAdapter : RecyclerView.Adapter<GudangPermintaanAdapter.ViewHolder>() {

    val listPermintaanBarang = ArrayList<GudangPermintaanBarang>()

    fun setList(list: List<GudangPermintaanBarang>){
        listPermintaanBarang.addAll(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GudangPermintaanAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gudang_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GudangPermintaanAdapter.ViewHolder, position: Int) {
        val list = listPermintaanBarang[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int = listPermintaanBarang.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(gudangPermintaanBarang: GudangPermintaanBarang){
            with(itemView){
                tx_no_gudang.text = gudangPermintaanBarang.nomorPO
            }
        }
    }

}
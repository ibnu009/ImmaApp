package com.pjb.immaapp.ui.usulanpermintaanbarang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.upb.PermintaanBarang
import kotlinx.android.synthetic.main.usulan_item.view.*

class UsulanPermintaanAdapter : RecyclerView.Adapter<UsulanPermintaanAdapter.UsulanViewHolder>() {

    private val listUsulan  = ArrayList<PermintaanBarang>()

    fun setList(list: List<PermintaanBarang>){
        listUsulan.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsulanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usulan_item, parent, false)

        return UsulanViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsulanViewHolder, position: Int) {
        val list = listUsulan[position]
        holder.bind(list)
    }

    override fun getItemCount() = listUsulan.size

    inner class UsulanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(permintaanBarang: PermintaanBarang){
            with(itemView){
                tx_no_permintaan.text = permintaanBarang.noPermintaan
                tx_tanggal_permintaan.text = permintaanBarang.tanggalPermohonan
            }
        }
    }
}
package com.pjb.immaapp.ui.gudangpermintaanbarang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.GudangPermintaanBarang
import com.pjb.immaapp.databinding.GudangItemBinding

class GudangPermintaanAdapter : RecyclerView.Adapter<GudangPermintaanAdapter.ViewHolder>() {

    val listPermintaanBarang = ArrayList<GudangPermintaanBarang>()

    fun setList(list: List<GudangPermintaanBarang>){
        listPermintaanBarang.addAll(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GudangPermintaanAdapter.ViewHolder {
        val binding = GudangItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GudangPermintaanAdapter.ViewHolder, position: Int) {
        val list = listPermintaanBarang[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int = listPermintaanBarang.size

    inner class ViewHolder(private val binding: GudangItemBinding) : RecyclerView.ViewHolder(binding?.root){
        fun bind(gudangPermintaanBarang: GudangPermintaanBarang){
            with(itemView){
                binding.txNoGudang.text = gudangPermintaanBarang.nomorPO
            }
        }
    }

}
package com.pjb.immaapp.ui.usulanpermintaanbarang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.upb.Company
import com.pjb.immaapp.databinding.CompanyItemBinding
import com.pjb.immaapp.utils.ConverterHelper

class CompanyListAdapter() : RecyclerView.Adapter<CompanyListAdapter.CompanyViewHolder>() {

    private val listCompany = ArrayList<Company>()

    fun setCompanyList(list: List<Company>) {
        listCompany.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val binding = CompanyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompanyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val company = listCompany[position]
        holder.bind(company)
    }

    override fun getItemCount(): Int = listCompany.size

    inner class CompanyViewHolder(private val binding: CompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(company: Company) {
            with(binding){
                txCompanyName.text = company.name
                val price = ConverterHelper().convertAnggaranFormat(company.harga)
                txPrice.text = binding.root.context.getString(R.string.anggaran_po, price)
                txCompanyCode.text = company.kode
            }
        }
    }
}

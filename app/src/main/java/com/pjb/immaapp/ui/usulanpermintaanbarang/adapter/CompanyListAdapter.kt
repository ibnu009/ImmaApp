package com.pjb.immaapp.ui.usulanpermintaanbarang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.upb.Company
import com.pjb.immaapp.databinding.CompanyItemBinding
import com.pjb.immaapp.ui.usulanpermintaanbarang.handler.OnItemCompanyClick
import com.pjb.immaapp.utils.ConverterHelper
import timber.log.Timber

class CompanyListAdapter(private val onItemCompanyClick: OnItemCompanyClick) : RecyclerView.Adapter<CompanyListAdapter.CompanyViewHolder>() {

    private val listCompany = ArrayList<Company>()
    private var selectedItem = RecyclerView.NO_POSITION

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
        holder.itemView.isSelected = selectedItem == position
        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedItem)
            selectedItem = position
            notifyItemChanged(selectedItem)
            if (selectedItem == position) {
                Timber.d("wah sudah cocok")
            }
            onItemCompanyClick.onCLicked(
                company.id
            )
        }

    }

    override fun getItemCount(): Int = listCompany.size

    inner class CompanyViewHolder(private val binding: CompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(company: Company) {
            with(binding){
                if (selectedItem == bindingAdapterPosition){
                    txStatus.visibility = View.VISIBLE
                } else {
                    txStatus.visibility = View.GONE
                }

                txCompanyName.text = company.name
                val price = ConverterHelper().convertAnggaranFormat(company.harga)
                txPrice.text = binding.root.context.getString(R.string.anggaran_po, price)
                txCompanyCode.text = company.kode
            }
        }
    }
}

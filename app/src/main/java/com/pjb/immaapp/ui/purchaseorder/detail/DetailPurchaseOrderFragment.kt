package com.pjb.immaapp.ui.purchaseorder.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.ui.purchaseorder.PurchaseOrderViewModel
import com.pjb.immaapp.ui.purchaseorder.adapter.DataItemPoPagedListAdapter
import com.pjb.immaapp.ui.purchaseorder.adapter.DataPoPagedListAdapter
import com.pjb.immaapp.utils.ConverterHelper
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail_po.*
import timber.log.Timber

class DetailPurchaseOrderFragment : Fragment() {

    companion object {
        const val EXTRA_PO_ENCODE = "EXTRA_PO"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var itemPagedListAdapter: DataItemPoPagedListAdapter


    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory)[PurchaseOrderViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_po, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemPagedListAdapter = DataItemPoPagedListAdapter()
        with(rv_item_data_po) {
            adapter = itemPagedListAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

        val safeArgs = arguments?.let { DetailPurchaseOrderFragmentArgs.fromBundle(it) }
        val codePo = safeArgs?.passEncodePo

        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        val token =
            sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
                ?: "Shared Preference Not Found"

        initiateDetail(token, codePo!!)
        initiateItemPo(token, codePo)
    }

    private fun initiateDetail(token: String, codePo: String) {
        viewModel.getDetailDataPo("12345", token, codePo).observe(viewLifecycleOwner, Observer {
            Timber.d("check vendor name ${it.vendor}")
            tx_tanggal_order.text = it.tanggalOrder
            tx_judul_pekerjaan.text = it.jobTitle
            tx_nama_vendor.text = it.vendor
            tx_levering.text = it.levering

            val anggaranFix = ConverterHelper().convertAnggaranFormat(it.anggaran)
            tx_total_anggaran.text = context?.getString(R.string.anggaran_po, anggaranFix)
        })

        viewModel.networkStateDetail.observe(viewLifecycleOwner, Observer {
            if (viewModel.listItemIsEmty(token, codePo) && it == NetworkState.LOADING) {
                shimmer_view_container_detail_po.startShimmer()
            } else{
                shimmer_view_container_detail_po.stopShimmer()
                shimmer_view_container_detail_po.visibility = View.GONE
            }
        })
    }

    private fun initiateItemPo(token: String, codePo: String) {
        viewModel.getListItemPo(token, codePo).observe(viewLifecycleOwner, Observer {
            itemPagedListAdapter.submitList(it)
        })

        viewModel.networkStateDetail.observe(viewLifecycleOwner, Observer {
            if (viewModel.listItemIsEmty(token, codePo) && it == NetworkState.LOADING) {
                shimmer_view_container_detail_po_rv.visibility = View.VISIBLE
                shimmer_view_container_detail_po_rv.startShimmer()
            } else {
                shimmer_view_container_detail_po_rv.visibility = View.GONE
                shimmer_view_container_detail_po_rv.stopShimmer()
            }
        })
    }

    private fun viewStateCheck(status: Boolean) {
        if (status) {
            tx_title_tanggal_order.visibility = View.GONE
        }else {

        }
    }
}
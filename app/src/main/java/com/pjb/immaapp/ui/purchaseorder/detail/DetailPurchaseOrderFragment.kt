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
import com.pjb.immaapp.databinding.FragmentDetailPoBinding
import com.pjb.immaapp.ui.purchaseorder.PurchaseOrderViewModel
import com.pjb.immaapp.ui.purchaseorder.adapter.DataItemPoPagedListAdapter
import com.pjb.immaapp.utils.ConverterHelper
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber

class DetailPurchaseOrderFragment : Fragment() {

    companion object {
        const val EXTRA_PO_ENCODE = "EXTRA_PO"
    }
    private lateinit var token: String

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var itemPagedListAdapter: DataItemPoPagedListAdapter


    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it) }?.get(PurchaseOrderViewModel::class.java)
    }

    private var _bindingFragmentDetailPo : FragmentDetailPoBinding? = null
    private val binding get() = _bindingFragmentDetailPo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentDetailPo = FragmentDetailPoBinding.inflate(inflater, container, false)
        return _bindingFragmentDetailPo?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemPagedListAdapter = DataItemPoPagedListAdapter()
        with(binding?.rvItemDataPo) {
            this?.adapter = itemPagedListAdapter
            this?.layoutManager = LinearLayoutManager(this?.context, LinearLayoutManager.VERTICAL, false)
        }

        binding?.shimmerViewContainerDetailPo?.visibility = View.VISIBLE
        binding?.shimmerViewContainerDetailPoRv?.visibility = View.VISIBLE

        val safeArgs = arguments?.let { DetailPurchaseOrderFragmentArgs.fromBundle(it) }
        val codePo = safeArgs?.passEncodePo

        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        token =
            sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
                ?: "Shared Preference Not Found"

        initiateDetail(token, codePo!!)
        initiateItemPo(token, codePo)
    }

    private fun initiateDetail(token: String, codePo: String) {
        viewModel?.getDetailDataPo("12345", token, codePo)?.observe(viewLifecycleOwner, Observer {
            Timber.d("check Data $it")
            binding?.txTanggalOrder?.text = it.tanggalOrder
            binding?.txJudulPekerjaan?.text = it.jobTitle
            binding?.txNamaVendor?.text = it.vendor
            binding?.txLevering?.text = it.levering

            val anggaranFix = ConverterHelper().convertAnggaranFormat(it.anggaran)
            binding?.txTotalAnggaran?.text = context?.getString(R.string.anggaran_po, anggaranFix)
        })

        viewModel?.networkStateDetail?.observe(viewLifecycleOwner, Observer {
            if (it == NetworkState.LOADING) {
                binding?.shimmerViewContainerDetailPo?.startShimmer()
            } else{
                binding?.shimmerViewContainerDetailPo?.stopShimmer()
                binding?.shimmerViewContainerDetailPo?.visibility = View.GONE
                binding?.layoutKeterangan?.visibility = View.VISIBLE
                binding?.txDetailPo?.visibility = View.VISIBLE
            }
        })
    }

    private fun initiateItemPo(token: String, codePo: String) {
        viewModel?.getListItemPo(token, codePo)?.observe(viewLifecycleOwner, Observer {
            itemPagedListAdapter.submitList(it)
            Timber.d("Received data is $it")
        })

        viewModel?.networkStateDetail?.observe(viewLifecycleOwner, Observer {
            when {
                it == NetworkState.LOADING -> {
                    binding?.shimmerViewContainerDetailPoRv?.startShimmer()
                }
                it == NetworkState.LOADED -> {
                    binding?.shimmerViewContainerDetailPoRv?.stopShimmer()
                    binding?.shimmerViewContainerDetailPoRv?.visibility = View.GONE
                }
                it == NetworkState.ERROR -> {
                    Timber.e("Error")
                }
                viewModel?.listItemIsEmty(token, codePo) == true -> {
                    Timber.e("Empty")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentDetailPo = null 
    }
}
package com.pjb.immaapp.ui.purchaseorder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.handler.OnClickedActionDataPo
import com.pjb.immaapp.ui.purchaseorder.adapter.DataPoPagedListAdapter
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.SharedPreferencesKey.KEY_API
import com.pjb.immaapp.utils.SharedPreferencesKey.KEY_TOKEN
import com.pjb.immaapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_po.*

class PurchaseOrderFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var poPagedListAdapter: DataPoPagedListAdapter
    private lateinit var token: String

    private val purchaseOrderViewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(PurchaseOrderViewModel::class.java)
    }

    private val onItemClicked = object : OnClickedActionDataPo {
        override fun onClicked(poEncode: String) {
            val action = PurchaseOrderFragmentDirections.actionNavPoToDetailPurchaseOrderFragment(poEncode)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_po, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        poPagedListAdapter = DataPoPagedListAdapter(onItemClicked)
        with(rv_po) {
            adapter = poPagedListAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        token =
            sharedPreferences.getString(KEY_TOKEN, "Not Found") ?: "Shared Preference Not Found"

//        purchaseOrderViewModel.getListDataPo(token, null).observe(viewLifecycleOwner, Observer {
//            poPagedListAdapter.submitList(it)
//
//        })

        shimmer_view_container.visibility = View.VISIBLE

        showData(token, null)
    }

    private fun showData(token: String, keywords: String?) {

        purchaseOrderViewModel.getListDataPo(token, keywords)
            .observe(viewLifecycleOwner, { dataPo ->
                poPagedListAdapter.submitList(dataPo)
            })

        purchaseOrderViewModel.networkState.observe(viewLifecycleOwner, { network ->
            if (purchaseOrderViewModel.listIsEmpty(token, keywords) && network == NetworkState.LOADING) {
                shimmer_view_container.startShimmer()
            } else {
                shimmer_view_container.stopShimmer()
                shimmer_view_container.visibility = View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmer()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        shimmer_view_container.stopShimmer()
    }
}
package com.pjb.immaapp.ui.purchaseorder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.databinding.FragmentPoBinding
import com.pjb.immaapp.handler.OnClickedActionDataPo
import com.pjb.immaapp.ui.purchaseorder.adapter.DataPoPagedListAdapter
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.SharedPreferencesKey.KEY_TOKEN
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber

@ExperimentalPagingApi
class PurchaseOrderFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var poPagedListAdapter: DataPoPagedListAdapter
    private lateinit var token: String

    private val purchaseOrderViewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it).get(PurchaseOrderViewModel::class.java) }
    }

    private val onItemClicked = object : OnClickedActionDataPo {
        override fun onClicked(poEncode: String) {
            val action =
                PurchaseOrderFragmentDirections.actionNavPoToDetailPurchaseOrderFragment(poEncode)
            findNavController().navigate(action)
        }
    }

    private var _bindingFragmentPo: FragmentPoBinding? = null
    private val binding get() = _bindingFragmentPo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentPo = FragmentPoBinding.inflate(inflater, container, false)
        return _bindingFragmentPo?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        poPagedListAdapter = DataPoPagedListAdapter(onItemClicked)
        with(binding?.rvPo) {
            this?.adapter = poPagedListAdapter
            this?.layoutManager =
                LinearLayoutManager(this?.context?.applicationContext, LinearLayoutManager.VERTICAL, false)
        }

        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        token =
            sharedPreferences.getString(KEY_TOKEN, "Not Found") ?: "Shared Preference Not Found"

        binding?.shimmerViewContainer?.visibility = View.VISIBLE

        showData(token, null)

        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                showSearchedData(token, query)
                Timber.d("searched $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun showData(token: String, keywords: String?) {

//        purchaseOrderViewModel.getListDataPo(token, keywords)
//            .observe(viewLifecycleOwner, { dataPo ->
//                poPagedListAdapter.submitList(dataPo)
//            })

        purchaseOrderViewModel?.getListDataPoPaging(token, keywords)?.observe(viewLifecycleOwner, Observer {
            poPagedListAdapter.submitData(lifecycle, it)
            binding?.shimmerViewContainer?.stopShimmer()
            binding?.shimmerViewContainer?.visibility = View.GONE
            Timber.d("ReceivedFragment $it")
        })

//        purchaseOrderViewModel.networkState.observe(viewLifecycleOwner, { network ->
//            if (purchaseOrderViewModel.listIsEmpty(
//                    token,
//                    keywords
//                ) && network == NetworkState.LOADING
//            ) {
//                binding?.shimmerViewContainer?.startShimmer()
//            } else {
//                binding?.shimmerViewContainer?.stopShimmer()
//                binding?.shimmerViewContainer?.visibility = View.GONE
//            }
//        })
    }

    private fun showSearchedData(token: String, keywords: String?) {
        purchaseOrderViewModel?.getSearchPo(token, keywords)?.observe(viewLifecycleOwner, Observer {
            poPagedListAdapter.submitData(lifecycle, it)
        })
    }

    override fun onResume() {
        super.onResume()
        binding?.shimmerViewContainer?.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding?.shimmerViewContainer?.stopShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentPo = null
    }
}
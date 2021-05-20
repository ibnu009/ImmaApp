package com.pjb.immaapp.ui.usulanpermintaanbarang.supplier

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.databinding.FragmentListSupplierBinding
import com.pjb.immaapp.handler.OnClickedActionDataSupplier
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.SupplierPagedListAdapter
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ViewModelFactory
import timber.log.Timber

@ExperimentalPagingApi
class ListSupplierFragment : Fragment() {

    private var _fragmentListSupplierBinding: FragmentListSupplierBinding? = null
    private val binding get() = _fragmentListSupplierBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private lateinit var apiKey: String

    private lateinit var supplierAdapter: SupplierPagedListAdapter

    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, factory).get(TambahSupplierViewModel::class.java) }
    }

    private val onItemClicked = object : OnClickedActionDataSupplier {
        override fun onClicked(idSupplier: Int, namaSupplier: String) {
            setFragmentResult("requestKey", bundleOf("idSupplier" to idSupplier, "namaSupplier" to namaSupplier))
            Timber.d("ID Supplier terpass : $idSupplier")
            view?.findNavController()?.popBackStack()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentListSupplierBinding =
            FragmentListSupplierBinding.inflate(inflater, container, false)
        return _fragmentListSupplierBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        supplierAdapter = SupplierPagedListAdapter(onItemClicked)
        binding?.rvSupplier?.layoutManager = LinearLayoutManager(this.context?.applicationContext, LinearLayoutManager.VERTICAL, false)
        binding?.rvSupplier?.adapter = supplierAdapter
        initiateKeys()
        initiateData("12345", token)
    }

    private fun initiateData(apiKey: String, token: String) {
        viewModel?.getListSupplier(apiKey, token)?.observe(viewLifecycleOwner, Observer {
            Timber.d("Company list $it")
            supplierAdapter.submitData(lifecycle, it)
        })
//        viewModel?.networkState?.observe(viewLifecycleOwner, Observer { network ->
//            when(network) {
//                NetworkState.LOADING -> isLoading(true)
//                NetworkState.LOADED -> isLoading(false)
//                NetworkState.EXPIRETOKEN -> context?.tokenExpired()?.show()
//                else -> ConverterHelper().convertNetworkStateErrorToSnackbar(binding?.root, network)
//            }
//        })
    }

    private fun initiateKeys() {
        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        apiKey = sharedPreferences.getString(SharedPreferencesKey.KEY_API, "Not Found")
            ?: "Shared Preference Not Found"
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
            ?: "Shared Preference Not Found"
    }

    private fun isLoading(status: Boolean) {
        if (!status) {
            binding?.shimmerViewContainer?.visibility = View.GONE
            binding?.shimmerViewContainer?.stopShimmer()
        } else {
            binding?.shimmerViewContainer?.visibility = View.VISIBLE
            binding?.shimmerViewContainer?.startShimmer()
        }
    }

}
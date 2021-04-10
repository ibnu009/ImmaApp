package com.pjb.immaapp.ui.usulanpermintaanbarang.supplier

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.databinding.FragmentListSupplierBinding
import com.pjb.immaapp.handler.OnClickedActionDataSupplier
import com.pjb.immaapp.handler.RabAddSupplierListener
import com.pjb.immaapp.ui.usulanpermintaanbarang.UsulanViewModel
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.SupplierPagedListAdapter
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ViewModelFactory
import timber.log.Timber

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
        initiateKeys()
        initiateRecyclerView()
        initiateData(apiKey, token)
    }

    private fun initiateRecyclerView() {
        binding?.rvSupplier?.layoutManager = LinearLayoutManager(context?.applicationContext)
        binding?.rvSupplier?.adapter = supplierAdapter
    }

    private fun initiateData(apiKey: String, token: String) {
        viewModel?.getListSupplier(apiKey, token)?.observe(viewLifecycleOwner, Observer {
            supplierAdapter.submitList(it)
        })
    }

    private fun initiateKeys() {
        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        apiKey = sharedPreferences.getString(SharedPreferencesKey.KEY_API, "Not Found")
            ?: "Shared Preference Not Found"
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
            ?: "Shared Preference Not Found"
    }

}
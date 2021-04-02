package com.pjb.immaapp.ui.usulanpermintaanbarang.material

import com.pjb.immaapp.ui.usulanpermintaanbarang.material.DetailMaterialFragmentArgs
import com.pjb.immaapp.ui.usulanpermintaanbarang.material.DetailMaterialViewModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentDetailMaterialBinding
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.CompanyListAdapter
import com.pjb.immaapp.utils.ConverterHelper
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ViewModelFactory

class DetailMaterialFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private lateinit var apiKey: String
    private var idDetail: Int = 0

    private lateinit var adapter: CompanyListAdapter

    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it).get(DetailMaterialViewModel::class.java) }
    }

    private var _bindingDetailMaterialFragment: FragmentDetailMaterialBinding? = null
    private val binding get() = _bindingDetailMaterialFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingDetailMaterialFragment =
            FragmentDetailMaterialBinding.inflate(inflater, container, false)
        return _bindingDetailMaterialFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs = arguments?.let { DetailMaterialFragmentArgs.fromBundle(it) }
        idDetail = safeArgs?.passIdDetail!!

        val toolbar = binding?.customToolbarDetailMaterial
        val txView = toolbar?.root?.findViewById(R.id.tx_title_page) as TextView
        val btnBack = toolbar.root.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding?.fabTambahMaterial?.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailMaterialFragment_to_tambahsSupplierFragment)
        }
        txView.text = context?.resources?.getText(R.string.detail_material)

        initiateRecyclerView()
        initiateKeys()
        initiateDetail()
        initiateCompanyList()

    }

    private fun initiateKeys() {
        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        apiKey = sharedPreferences.getString(SharedPreferencesKey.KEY_API, "Not Found")
            ?: "Shared Preference Not Found"
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
            ?: "Shared Preference Not Found"
    }

    private fun initiateRecyclerView() {
        adapter = CompanyListAdapter()
        with(binding?.rvCompanies) {
            this?.adapter = adapter
            this?.layoutManager = LinearLayoutManager(
                this?.context?.applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }

    private fun initiateDetail() {
        viewModel?.getDetailMaterial(apiKey = apiKey, detailId = idDetail, token = token)
            ?.observe(viewLifecycleOwner, Observer {
                binding?.txNamaMaterial?.text = it.material
                binding?.txQty?.text = it.qty.toString()

                val avgPrice = ConverterHelper().convertAnggaranFormat(it.averageCost)
                binding?.txAverageCost?.text = this.getString(R.string.anggaran_po, avgPrice)

                val rabPrice = ConverterHelper().convertAnggaranFormat(it.rab)

                binding?.txRab?.text = this.getString(R.string.anggaran_po, rabPrice)
                binding?.txLastPo?.text = it.lastPo
            })
    }


    private fun initiateCompanyList() {
        viewModel?.getCompanyList(apiKey = apiKey, detailId = idDetail, token = token)
            ?.observe(viewLifecycleOwner, Observer {
                adapter.setCompanyList(it)
            })
    }

}
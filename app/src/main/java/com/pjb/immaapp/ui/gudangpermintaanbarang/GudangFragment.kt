package com.pjb.immaapp.ui.gudangpermintaanbarang

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.GudangPermintaanBarang
import com.pjb.immaapp.databinding.FragmentGudangBinding
import com.pjb.immaapp.ui.gudangpermintaanbarang.adapter.GudangPermintaanAdapter
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber

class GudangFragment : Fragment() {

    private lateinit var gudangPermintaanAdapter: GudangPermintaanAdapter

    private val gudangViewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(GudangViewModel::class.java)
    }

    private var _gudangFragmentBinding: FragmentGudangBinding? = null
    val binding get() = _gudangFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _gudangFragmentBinding = FragmentGudangBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_gudang, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gudangPermintaanAdapter = GudangPermintaanAdapter()
        val listPermintaanGudang = gudangViewModel.getGudang()

        initRv(requireContext().applicationContext, listPermintaanGudang)

    }

    private fun initRv(context: Context, list: List<GudangPermintaanBarang>) {
        gudangPermintaanAdapter.setList(list)
        Timber.d(list.size.toString())
        with(binding?.rvGudangItem) {
            this?.adapter = gudangPermintaanAdapter
            this?.layoutManager = LinearLayoutManager(context)
        }

    }

}
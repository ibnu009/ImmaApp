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
import com.pjb.immaapp.ui.gudangpermintaanbarang.adapter.GudangPermintaanAdapter
import com.pjb.immaapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_gudang.*
import timber.log.Timber

class GudangFragment : Fragment() {

    private lateinit var gudangPermintaanAdapter: GudangPermintaanAdapter

    private val gudangViewModel by lazy{
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(GudangViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gudang, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gudangPermintaanAdapter = GudangPermintaanAdapter()
        val listPermintaanGudang = gudangViewModel.getGudang()

        initRv(requireContext(), listPermintaanGudang)

    }

    private fun initRv(context: Context, list: List<GudangPermintaanBarang>) {
        gudangPermintaanAdapter.setList(list)
        Timber.d(list.size.toString())
        with(rv_gudang_item){
            adapter = gudangPermintaanAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

}
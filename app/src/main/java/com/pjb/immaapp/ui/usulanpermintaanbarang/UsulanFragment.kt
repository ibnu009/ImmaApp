package com.pjb.immaapp.ui.usulanpermintaanbarang

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.PermintaanBarang
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.UsulanPermintaanAdapter
import com.pjb.immaapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_usulan.*
import kotlinx.android.synthetic.main.fragment_usulan.view.*
import timber.log.Timber


class UsulanFragment : Fragment() {

    private lateinit var usulanViewModel: UsulanViewModel
    private lateinit var usulanPermintaanAdapter: UsulanPermintaanAdapter

    private val viewModel by lazy{
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(UsulanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        usulanViewModel =
            ViewModelProvider(this).get(UsulanViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_usulan, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usulanPermintaanAdapter = UsulanPermintaanAdapter()
        val listUsulan = usulanViewModel.getUsulan()

        shimmer_view_container.visibility = View.GONE
        initRvCart(requireContext(), listUsulan)
    }

    fun initRvCart(context: Context, list: List<PermintaanBarang>){
        usulanPermintaanAdapter.setList(list)
        Timber.d(list.size.toString())
        rv_usulan.adapter = usulanPermintaanAdapter
        rv_usulan.layoutManager = LinearLayoutManager(context)
    }
}
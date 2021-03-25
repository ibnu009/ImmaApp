package com.pjb.immaapp.ui.usulanpermintaanbarang.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentTambahSupplierBinding

class TambahsSupplierFragment : Fragment() {
    private var _tambahSupplierFragment: FragmentTambahSupplierBinding? = null
    private val binding get() = _tambahSupplierFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _tambahSupplierFragment = FragmentTambahSupplierBinding.inflate(inflater, container, false)
        return _tambahSupplierFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.customToolbarTambahSupplier
        val txTitle = toolbar?.findViewById(R.id.tx_title_page) as TextView
        txTitle.text = getString(R.string.tambah_supplier)

        val btnBack = toolbar.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener{
            it.findNavController().popBackStack()
        }

    }

}

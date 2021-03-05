package com.pjb.immaapp.ui.usulanpermintaanbarang.tambah.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentTambahMaterialBinding

class TambahMaterialUpbFragment: Fragment() {

    private var _bindingTambahMaterialFragment: FragmentTambahMaterialBinding? = null
    private val binding get() = _bindingTambahMaterialFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingTambahMaterialFragment = FragmentTambahMaterialBinding.inflate(inflater, container, false)
        return _bindingTambahMaterialFragment?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.customToolbarTambahMaterial
        val txView = toolbar?.root?.findViewById(R.id.tx_title_page) as TextView
        val btnBack = toolbar.root.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        txView.text = getString(R.string.tambah_material)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingTambahMaterialFragment = null
    }

}
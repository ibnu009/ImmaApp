package com.pjb.immaapp.ui.usulanpermintaanbarang.tambah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    }

}
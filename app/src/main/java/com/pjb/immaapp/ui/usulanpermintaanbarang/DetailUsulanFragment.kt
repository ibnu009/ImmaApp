package com.pjb.immaapp.ui.usulanpermintaanbarang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentDetailUsulanBinding
import com.pjb.immaapp.utils.ViewModelFactory

class DetailUsulanFragment : Fragment(){

    companion object{
        const val EXTRA_ID_PERMINTAAN = "EXTRA_ID_UPB"
    }

    private val upbViewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory)[UsulanViewModel::class.java]
    }

    private var _bindingFragmentDetailUsulan : FragmentDetailUsulanBinding? = null
    private val binding get() = _bindingFragmentDetailUsulan

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentDetailUsulan = FragmentDetailUsulanBinding.inflate(inflater, container, false)
        return _bindingFragmentDetailUsulan?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle ? = this.arguments
    }

}
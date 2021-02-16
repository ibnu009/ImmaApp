package com.pjb.immaapp.ui.stokopname

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentOpnameBinding

class StokOpnameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentStokOpname = FragmentOpnameBinding.inflate(inflater, container, false)

        return _bindingFragmentStokOpname?.root
    }

    private var _bindingFragmentStokOpname : FragmentOpnameBinding? = null
    private val binding get() = _bindingFragmentStokOpname

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.txManual?.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val bottomSheetFragment = BottomSheetFragment.newInstance()
                activity?.supportFragmentManager?.let { bottomSheetFragment.show(it,"bottom_sheet") }
            }

        })
    }


}
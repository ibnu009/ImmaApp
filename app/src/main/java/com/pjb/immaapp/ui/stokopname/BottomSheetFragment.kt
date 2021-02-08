package com.pjb.immaapp.ui.stokopname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.BottomSheetFragmentBinding

class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() : BottomSheetFragment {
            return BottomSheetFragment()
        }
    }

    private var _bindingFragmentBottomSheetFragment: BottomSheetFragmentBinding? = null
    private val binding = _bindingFragmentBottomSheetFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentBottomSheetFragment = BottomSheetFragmentBinding.inflate(
            inflater, container, false)

        return _bindingFragmentBottomSheetFragment?.root
    }

}
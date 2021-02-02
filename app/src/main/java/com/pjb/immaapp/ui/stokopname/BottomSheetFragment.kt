package com.pjb.immaapp.ui.stokopname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pjb.immaapp.R

class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() : BottomSheetFragment {
            return BottomSheetFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_fragment, container, false)

        return view
    }

}
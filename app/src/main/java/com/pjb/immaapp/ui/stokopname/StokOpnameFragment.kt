package com.pjb.immaapp.ui.stokopname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pjb.immaapp.R
import kotlinx.android.synthetic.main.fragment_opname.*

class StokOpnameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_opname, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tx_manual.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val bottomSheetFragment = BottomSheetFragment.newInstance()
                activity?.supportFragmentManager?.let { bottomSheetFragment.show(it,"bottom_sheet") }
            }

        })
    }

}
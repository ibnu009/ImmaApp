package com.pjb.immaapp.ui.usulanpermintaanbarang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.R
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.DataUpbPagedListAdapter
import kotlinx.android.synthetic.main.fragment_detail_usulan.view.*

class DetailUsulanFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_usulan, container, false)
    }

}
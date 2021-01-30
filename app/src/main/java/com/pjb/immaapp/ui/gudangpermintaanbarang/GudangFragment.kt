package com.pjb.immaapp.ui.gudangpermintaanbarang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.R
import com.pjb.immaapp.utils.ViewModelFactory

class GudangFragment : Fragment() {

    private val gudangViewModel by lazy{
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(GudangViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gudang, container, false)

        return root
    }
}
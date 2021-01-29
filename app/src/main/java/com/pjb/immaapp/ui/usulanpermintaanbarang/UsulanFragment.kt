package com.pjb.immaapp.ui.usulanpermintaanbarang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.pjb.immaapp.R
import kotlinx.android.synthetic.main.fragment_usulan.*


class UsulanFragment : Fragment() {

    private lateinit var usulanViewModel: UsulanViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        usulanViewModel =
            ViewModelProvider(this).get(UsulanViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_usulan, container, false)

        shimmer_view_container.startShimmer()

        return root
    }
}
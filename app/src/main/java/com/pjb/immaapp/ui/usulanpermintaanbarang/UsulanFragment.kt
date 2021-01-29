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


class UsulanFragment : Fragment() {

    private lateinit var usulanViewModel: UsulanViewModel

    private lateinit var mShimmerViewContainer: ShimmerFrameLayout
    private lateinit var searchView : SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        usulanViewModel =
            ViewModelProvider(this).get(UsulanViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_usulan, container, false)

        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container)
        searchView = root.findViewById(R.id.search_view)
        searchView.queryHint = "Cari Data Usulan"

        mShimmerViewContainer.startShimmerAnimation()

        return root
    }
}
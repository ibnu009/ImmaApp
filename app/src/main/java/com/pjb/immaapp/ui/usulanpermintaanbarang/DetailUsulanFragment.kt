package com.pjb.immaapp.ui.usulanpermintaanbarang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.pjb.immaapp.R
import com.pjb.immaapp.ui.home.HomeViewModel

class DetailUsulanFragment : Fragment(){

    private lateinit var detailUsulanViewModel: DetailUsulanViewModel

    private lateinit var mShimmerViewContainer: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailUsulanViewModel =
            ViewModelProvider(this).get(DetailUsulanViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_detail_usulan, container, false)

        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container)

        mShimmerViewContainer.startShimmerAnimation()

        return root
    }
}
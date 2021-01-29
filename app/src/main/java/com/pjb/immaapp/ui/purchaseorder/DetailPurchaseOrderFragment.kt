package com.pjb.immaapp.ui.purchaseorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout
import com.pjb.immaapp.R

class DetailPurchaseOrderFragment : Fragment() {

    private lateinit var mShimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_detail_po, container, false)

        mShimmerFrameLayout = root.findViewById(R.id.shimmer_view_container)
        return root
    }
}
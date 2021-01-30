package com.pjb.immaapp.ui.purchaseorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout
import com.pjb.immaapp.R
import kotlinx.android.synthetic.main.fragment_detail_po.view.*

class DetailPurchaseOrderFragment : Fragment() {

    private lateinit var mShimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_detail_po, container, false)

        root.shimmer_view_container.startShimmer()

        return root
    }
}
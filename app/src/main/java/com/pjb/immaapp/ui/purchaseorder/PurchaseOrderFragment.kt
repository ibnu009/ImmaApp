package com.pjb.immaapp.ui.purchaseorder

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.pjb.immaapp.R
import kotlinx.android.synthetic.main.fragment_po.view.*

class PurchaseOrderFragment : Fragment() {


    private lateinit var purchaseOrderViewModel: PurchaseOrderViewModel
    private lateinit var mShimmerLayout : ShimmerFrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        purchaseOrderViewModel = ViewModelProvider(this).get(PurchaseOrderViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_po, container, false)

        root.shimmer_view_container.startShimmer()

        return root
    }
}
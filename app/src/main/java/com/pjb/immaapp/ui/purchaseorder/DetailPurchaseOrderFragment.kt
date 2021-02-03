package com.pjb.immaapp.ui.purchaseorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pjb.immaapp.R

class DetailPurchaseOrderFragment : Fragment() {

    companion object{
        const val EXTRA_PO_ENCODE = "EXTRA_PO"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_po, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle? = this.arguments
        if (bundle != null) {
            val code = bundle.getString(EXTRA_PO_ENCODE, "K")
        }
    }
}
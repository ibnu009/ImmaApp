package com.pjb.immaapp.ui.purchaseorder

import androidx.lifecycle.ViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.pjb.immaapp.utils.DataDummy

class PurchaseOrderViewModel : ViewModel() {
    fun getPO() = DataDummy.getPurchaseOrder()
}
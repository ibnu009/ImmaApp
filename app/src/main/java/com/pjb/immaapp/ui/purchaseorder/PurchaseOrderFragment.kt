package com.pjb.immaapp.ui.purchaseorder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.PurchaseOrder
import com.pjb.immaapp.ui.purchaseorder.adapter.PurchaseOrderAdapter
import com.pjb.immaapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_po.*
import kotlinx.android.synthetic.main.fragment_po.view.*
import timber.log.Timber

class PurchaseOrderFragment : Fragment() {

    private lateinit var purchaseOrderAdapter: PurchaseOrderAdapter

    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this).get(PurchaseOrderViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_po, container, false)

        root.shimmer_view_container.startShimmer()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        purchaseOrderAdapter = PurchaseOrderAdapter()
        val listPO = viewModel.getPO()

        initRv(requireContext(),  listPO)

    }

    fun initRv(context: Context, list: List<PurchaseOrder>){
        purchaseOrderAdapter.setList(list)
        Timber.d(list.size.toString())
        rv_po.adapter = purchaseOrderAdapter
        rv_po.layoutManager = LinearLayoutManager(context)
    }

}
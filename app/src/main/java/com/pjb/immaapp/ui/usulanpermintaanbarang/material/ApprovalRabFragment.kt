package com.pjb.immaapp.ui.usulanpermintaanbarang.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentApprovalRabBinding

class ApprovalRabFragment: Fragment() {

    private var _approvalRabFragmentBinding: FragmentApprovalRabBinding? = null
    private val binding get() = _approvalRabFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _approvalRabFragmentBinding = FragmentApprovalRabBinding.inflate(inflater, container, false)
        return _approvalRabFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = binding?.customToolbar
        val txView = toolbar?.root?.findViewById(R.id.tx_title_page) as TextView
        val btnBack = toolbar.root.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        txView.text = getString(R.string.approval_rab)

    }

}
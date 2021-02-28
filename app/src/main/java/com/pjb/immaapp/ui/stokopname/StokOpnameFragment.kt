package com.pjb.immaapp.ui.stokopname

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentOpnameBinding

class StokOpnameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentStokOpname = FragmentOpnameBinding.inflate(inflater, container, false)

        return _bindingFragmentStokOpname?.root
    }

    private var _bindingFragmentStokOpname: FragmentOpnameBinding? = null
    private val binding get() = _bindingFragmentStokOpname

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.txManual?.setOnClickListener {
            initiateDialogView(it)
        }

        binding?.btnInitiateScan?.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_opname_to_barcodeScanFragment)
        }
    }

    private fun initiateDialogView(v: View)  {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_item_num, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
            .show()

        val edtItemNum = mDialogView.findViewById(R.id.edt_item_num) as EditText
        val btnKonfirmasi =  mDialogView.findViewById(R.id.btn_konfirmasi) as Button

        btnKonfirmasi.setOnClickListener {
            val raw = edtItemNum.text.toString()
            val itemNum = Integer.parseInt(raw)

            val action =
                StokOpnameFragmentDirections.actionNavOpnameToStokOpnameResultFragment(itemNum)
            v.findNavController().navigate(action)

            mBuilder.dismiss()
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentStokOpname = null
    }

}
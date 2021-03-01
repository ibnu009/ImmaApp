package com.pjb.immaapp.ui.stokopname

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.zxing.Result
import com.pjb.immaapp.databinding.FragmentBarcodeScanBinding
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.lang.NumberFormatException

class BarcodeScanFragment : Fragment(), ZXingScannerView.ResultHandler {

    private var zXingScannerView: ZXingScannerView? = null

    private var _bindingBarcodeScanFragment: FragmentBarcodeScanBinding? = null
    private val binding get() = _bindingBarcodeScanFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingBarcodeScanFragment = FragmentBarcodeScanBinding.inflate(inflater, container, false)
        return _bindingBarcodeScanFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScannerView()
    }

    override fun onStart() {
        zXingScannerView?.startCamera()
        doRequestPermission()
        super.onStart()
    }

    private fun initScannerView() {
        zXingScannerView = ZXingScannerView(this.context?.applicationContext)
        zXingScannerView!!.setAutoFocus(true)
        zXingScannerView!!.setResultHandler(this)
        binding?.cameraFrame?.addView(zXingScannerView)
    }

    private fun doRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.context?.applicationContext?.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> initScannerView()
            else -> { /*Nothing to do here*/
            }
        }
    }

    override fun handleResult(result: Result) {
        try {
            val itemNum = result.text.toInt()
            val action = BarcodeScanFragmentDirections
                .actionBarcodeScanFragmentToStokOpnameResultFragment(itemNum)
            findNavController().navigate(action)
        }catch (ex: NumberFormatException){
            Toast.makeText(context?.applicationContext, "Item num tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingBarcodeScanFragment = null
    }

}
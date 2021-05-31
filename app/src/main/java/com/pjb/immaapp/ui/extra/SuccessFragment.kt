package com.pjb.immaapp.ui.extra

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentSuccessScreenBinding
import com.pjb.immaapp.utils.global.ConstVal
import timber.log.Timber

class SuccessFragment : Fragment() {

    private var idPermintaan: Int? = null

    private val callBack = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            when (idPermintaan) {
                ConstVal.RAB_TYPE -> {
                    view?.findNavController()?.navigate(R.id.action_successFragment_to_nav_usulan)
                }
                ConstVal.MATERIAL_CREATE_TYPE -> {
                    val action = idPermintaan?.let { idPermintaan ->
                        SuccessFragmentDirections.actionSuccessFragmentToDetailUsulanPermintaanBarangFragment(
                            idPermintaan
                        )
                    }
                    action?.let { it1 -> view?.findNavController()?.navigate(it1) }
                }
                ConstVal.STOCK_OPNAME_CREATE_TYPE -> {
                    view?.findNavController()?.navigate(R.id.action_successFragment_to_nav_opname)
                }

                else -> {
                    Timber.d("I dunno")
                }
            }
        }
    }

    private var _bindingSuccessFragment: FragmentSuccessScreenBinding? = null
    private val binding get() = _bindingSuccessFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingSuccessFragment = FragmentSuccessScreenBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        return _bindingSuccessFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)

        val safeArgs = arguments?.let { SuccessFragmentArgs.fromBundle(it) }
        val successType = safeArgs?.passType
        idPermintaan = safeArgs?.passIdPermintaan

        initiateSuccessScreen(successType ?: 0, requireContext())
    }

    private fun initiateSuccessScreen(successType: Int, context: Context) {
        when (successType) {
            ConstVal.RAB_TYPE -> {
                binding?.txSuccessKeterangan?.text = context.getString(R.string.rab_telah_dibuat)
                binding?.btnBack?.setOnClickListener {
                    view?.findNavController()?.navigate(R.id.action_successFragment_to_nav_usulan)
                }
                binding?.customSuccessToolbar?.btnBackMenu?.setOnClickListener {
                    view?.findNavController()?.navigate(R.id.action_successFragment_to_nav_usulan)
                }
            }
            ConstVal.MATERIAL_CREATE_TYPE -> {
                binding?.txSuccessKeterangan?.text =
                    context.getString(R.string.material_telah_dibuat)
                binding?.btnBack?.setOnClickListener {
                    val action = idPermintaan?.let { idPermintaan ->
                        SuccessFragmentDirections.actionSuccessFragmentToDetailUsulanPermintaanBarangFragment(
                            idPermintaan
                        )
                    }
                    action?.let { it1 -> view?.findNavController()?.navigate(it1) }
                }
                binding?.customSuccessToolbar?.btnBackMenu?.setOnClickListener {
                    val action = idPermintaan?.let { idPermintaan ->
                        SuccessFragmentDirections.actionSuccessFragmentToDetailUsulanPermintaanBarangFragment(
                            idPermintaan
                        )
                    }
                    action?.let { it1 -> view?.findNavController()?.navigate(it1) }
                }
            }
            ConstVal.STOCK_OPNAME_CREATE_TYPE -> {
                binding?.txSuccessKeterangan?.text = context.getString(R.string.stock_opname_telah_dibuat)
                binding?.btnBack?.setOnClickListener {
                    view?.findNavController()?.navigate(R.id.action_successFragment_to_nav_opname)
                }
                binding?.customSuccessToolbar?.btnBackMenu?.setOnClickListener {
                    view?.findNavController()?.navigate(R.id.action_successFragment_to_nav_opname)
                }
            }
            else -> {
                Timber.e("Unknown type!")
            }
        }
    }
}
package com.pjb.immaapp.ui.stokopname

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentOpnameResultBinding
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.SharedPreferencesKey.PREFS_NAME
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber

class StokOpnameResultFragment : Fragment() {

    private var _bindingFragmentOpnameResult: FragmentOpnameResultBinding? = null
    private val binding get() = _bindingFragmentOpnameResult

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    private val stokOpnameViewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it) }?.get(StokOpnameViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentOpnameResult =
            FragmentOpnameResultBinding.inflate(inflater, container, false)
        return _bindingFragmentOpnameResult?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs = arguments?.let { StokOpnameResultFragmentArgs.fromBundle(it) }
        val itemNum = safeArgs?.passItemNum

        sharedPreferences =
            context?.applicationContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found").toString()

        binding?.layoutKeterangan?.visibility = View.GONE
        binding?.parentTdd?.visibility = View.GONE

        initiateDataStokOpname(token, itemNum!!)

        binding?.imgCollapse?.setOnClickListener {
            if (binding?.layoutDataOpname?.visibility == View.GONE) {
                binding?.imgCollapse?.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_arrow_down,
                        null
                    )
                )
                binding?.layoutDataOpname?.visibility = View.VISIBLE
            } else {
                binding?.imgCollapse?.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_keyboard_up,
                        null
                    )
                )
                binding?.layoutDataOpname?.visibility = View.GONE
            }
        }

        binding?.btnSimpanData?.setOnClickListener {
            val notes = binding?.edtKeterangan?.text.toString().trim()
            val stock = Integer.parseInt(binding?.edtStokNyata?.text.toString())
            val kondisi = binding?.edtKondisiBarang?.text.toString().trim()
            addDataStokOpname(token, itemNum, notes, stock, kondisi)
        }
    }

    private fun addDataStokOpname(
        token: String,
        itemNum: Int,
        notes: String,
        stock: Int,
        kondisi: String
    ) {
        stokOpnameViewModel?.addDataStokOpname("12345", token, itemNum, notes, stock, kondisi)
            ?.observe(viewLifecycleOwner, {
                Timber.d("New data created : ${it.message}")
                Toast.makeText(
                    context?.applicationContext,
                    "Data berhasil ditambah",
                    Toast.LENGTH_SHORT
                ).show()
            })
        stokOpnameViewModel?.networkState?.observe(viewLifecycleOwner, {
            if (it == NetworkState.FAILEDTOADD) {
                Toast.makeText(context?.applicationContext, it.toString(), Toast.LENGTH_SHORT)
                    .show()
                Timber.e("Error")
            }
        })
    }

    private fun initiateDataStokOpname(token: String, itemNum: Int) {
        stokOpnameViewModel?.getDataStokOpname("12345", token, itemNum)
            ?.observe(viewLifecycleOwner, {
                Timber.d("Check data $it")

                binding?.txNomorBarang?.text =
                    context?.applicationContext?.getString(R.string.num_item, itemNum.toString())
                binding?.txDeskripsiUnit?.text =
                    context?.applicationContext?.getString(R.string.deskripsi_item, it.description)
                binding?.txSatuan?.text =
                    context?.applicationContext?.getString(R.string.satuan_item, it.satuan)
            })

        stokOpnameViewModel?.networkState?.observe(viewLifecycleOwner, {
            when (it) {
                NetworkState.LOADING -> {
                    Timber.d("Check Loading")
                    binding?.shimmerViewContainerDetailOpname?.startShimmer()
                }
                NetworkState.ERROR-> {
                    binding?.shimmerViewContainerDetailOpname?.stopShimmer()
                    binding?.shimmerViewContainerDetailOpname?.visibility = View.GONE
                    binding?.layoutKeterangan?.visibility = View.GONE
                    binding?.parentTdd?.visibility = View.GONE
                    Toast.makeText(
                        context?.applicationContext,
                        "Data tidak ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NetworkState.LOADED -> {
                    Timber.d("Check Loaded Data")
                    Toast.makeText(context?.applicationContext, "${it.status}", Toast.LENGTH_SHORT).show()
                    binding?.shimmerViewContainerDetailOpname?.stopShimmer()
                    binding?.shimmerViewContainerDetailOpname?.visibility = View.GONE
                    binding?.layoutKeterangan?.visibility = View.VISIBLE
                    binding?.parentTdd?.visibility = View.VISIBLE
                }
                else -> {
                    Timber.d("Check Loaded Data")
                    Toast.makeText(context?.applicationContext, "${it.status}", Toast.LENGTH_SHORT).show()
                    binding?.shimmerViewContainerDetailOpname?.stopShimmer()
                    binding?.shimmerViewContainerDetailOpname?.visibility = View.GONE
                    binding?.layoutKeterangan?.visibility = View.VISIBLE
                    binding?.parentTdd?.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentOpnameResult = null
    }

}
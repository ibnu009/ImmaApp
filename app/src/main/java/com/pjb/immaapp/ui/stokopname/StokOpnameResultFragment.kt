package com.pjb.immaapp.ui.stokopname

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentOpnameResultBinding
import com.pjb.immaapp.utils.ConverterHelper
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.SharedPreferencesKey.PREFS_NAME
import com.pjb.immaapp.utils.global.ConstVal
import com.pjb.immaapp.utils.global.ViewModelFactory
import com.pjb.immaapp.utils.global.snackbar
import timber.log.Timber


class StokOpnameResultFragment : Fragment(), StockOpnameListener {

    private var _bindingFragmentOpnameResult: FragmentOpnameResultBinding? = null
    private val binding get() = _bindingFragmentOpnameResult

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private var dialog: AlertDialog? = null

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

        stokOpnameViewModel?.listener = this
        
        val toolbar = binding?.customToolbarDetailOpname
        val txView = toolbar?.root?.findViewById(R.id.tx_title_page) as TextView
        val btnBack = toolbar.root.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        txView.text = context?.resources?.getString(R.string.detail_stok_opname)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.root)

        val safeArgs = arguments?.let { StokOpnameResultFragmentArgs.fromBundle(it) }
        val itemNum = safeArgs?.passItemNum

        dialog = setProgressDialog(activity?.applicationContext!!, "Menambahkan")

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
            val raw = binding?.edtStokNyata?.text.toString()
            val rawKondisi = binding?.spinnerKondisiBarang?.selectedItem.toString()
            val kondisi: String

            when(rawKondisi){
                getString(R.string.baik) -> {
                    kondisi = "B"
                }

                getString(R.string.rusak) -> {
                    kondisi = "R"
                }

                getString(R.string.baik_tapi_tidak_bisa_dipakai) -> {
                    kondisi = "T"
                }

                getString(R.string.rusak_dan_tidak_bisa_diperbaiki) -> {
                    kondisi = "P"
                }

                else -> {
                    kondisi = "invalid"
                }
            }
            if(checkIsEmpty(notes, raw)){
                val stock = Integer.parseInt(raw)
                stokOpnameViewModel?.addDataStokOpname("12345", token, itemNum, notes, stock, kondisi)
            }
        }
    }

    private fun checkIsEmpty(keterangan: String, stok: String): Boolean {
        with(binding) {
            when {
                stok.isEmpty() -> {
                    this?.edtStokNyata?.requestFocus()
                    this?.edtStokNyata?.error = "Stok tidak boleh kosong"

                    return false
                }
                keterangan.length < 10 -> {
                    this?.edtKeterangan?.requestFocus()
                    this?.edtKeterangan?.error = "Keterangan minimal 10 Huruf"

                    return false
                }
            }
            return true
        }
    }

    private fun initiateDataStokOpname(token: String, itemNum: Int) {
        stokOpnameViewModel?.getDataStokOpname("12345", token, itemNum)
            ?.observe(viewLifecycleOwner, Observer {
                Timber.d("Check data $it")

                binding?.txNomorBarang?.text =
                    context?.applicationContext?.getString(R.string.num_item, itemNum.toString())
                binding?.txDeskripsiUnit?.text =
                    context?.applicationContext?.getString(R.string.deskripsi_item, it.description)
                binding?.txSatuan?.text =
                    context?.applicationContext?.getString(R.string.satuan_item, it.satuan)
            })

        stokOpnameViewModel?.networkState?.observe(viewLifecycleOwner, Observer{
            when (it) {
                NetworkState.LOADING -> {
                    Timber.d("Check Loading")
                    binding?.shimmerViewContainerDetailOpname?.startShimmer()
                }
                NetworkState.ERROR -> {
                    binding?.shimmerViewContainerDetailOpname?.stopShimmer()
                    binding?.shimmerViewContainerDetailOpname?.visibility = View.GONE
                    binding?.layoutKeterangan?.visibility = View.GONE
                    binding?.parentTdd?.visibility = View.GONE
                }
                NetworkState.LOADED -> {
                    Timber.d("Check Loaded Data")
                    binding?.shimmerViewContainerDetailOpname?.stopShimmer()
                    binding?.shimmerViewContainerDetailOpname?.visibility = View.GONE
                    binding?.layoutKeterangan?.visibility = View.VISIBLE
                    binding?.parentTdd?.visibility = View.VISIBLE
                }
                else -> {
                    Timber.d("Check Loaded Data")
                    binding?.shimmerViewContainerDetailOpname?.stopShimmer()
                    binding?.shimmerViewContainerDetailOpname?.visibility = View.GONE
                    binding?.layoutKeterangan?.visibility = View.VISIBLE
                    binding?.parentTdd?.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onSuccess() {
        Timber.d("success!!!!")
        val action = StokOpnameResultFragmentDirections.actionStokOpnameResultFragmentToSuccessFragment(
            passType = ConstVal.STOCK_OPNAME_CREATE_TYPE,
            passIdPermintaan = 0,
        )
        findNavController().navigate(action)
    }

    override fun onFailure(message: String) {
        view?.snackbar(message)
    }

    private fun setProgressDialog(context: Context, message: String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20.toFloat()
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentOpnameResult = null
        dialog = null
    }

}
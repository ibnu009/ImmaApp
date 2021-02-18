package com.pjb.immaapp.ui.usulanpermintaanbarang.tambah

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.pjb.immaapp.databinding.FragmentTambahUsulanBinding
import java.text.SimpleDateFormat
import java.util.*

class TambahUsulanFragment: Fragment() {

    private var _bindingFragmentTambahUsulan: FragmentTambahUsulanBinding? = null
    private val binding get() = _bindingFragmentTambahUsulan

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentTambahUsulan = FragmentTambahUsulanBinding.inflate(inflater, container, false)
        return _bindingFragmentTambahUsulan?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = object: DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showDate()
            }
        }

        binding?.edtTanggalDibutuhkan?.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                context?.applicationContext?.let {
                    DatePickerDialog(
                        it,
                        date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show()
                }
            }

        })
    }

    fun showDate() {
        val myFormat = "yyyy-mm-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding?.edtTanggalDibutuhkan?.setText(sdf.format(calendar.time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentTambahUsulan = null
    }

}
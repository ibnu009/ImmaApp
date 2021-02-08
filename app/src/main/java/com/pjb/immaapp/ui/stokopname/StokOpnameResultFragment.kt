package com.pjb.immaapp.ui.stokopname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentOpnameResultBinding
import kotlinx.android.synthetic.main.fragment_opname_result.*

class StokOpnameResultFragment : Fragment() {

    private var _bindingFragmentOpnameResult: FragmentOpnameResultBinding? = null
    private val binding get() = _bindingFragmentOpnameResult

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentOpnameResult = FragmentOpnameResultBinding.inflate(inflater,container, false)
        return _bindingFragmentOpnameResult?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_collapse.setOnClickListener {
            if (layout_data_opname.visibility == View.GONE) {
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

    }

}
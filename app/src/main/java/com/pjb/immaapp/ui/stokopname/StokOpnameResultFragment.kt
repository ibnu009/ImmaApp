package com.pjb.immaapp.ui.stokopname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.pjb.immaapp.R
import kotlinx.android.synthetic.main.fragment_opname_result.*

class StokOpnameResultFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_opname_result, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_collapse.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (layout_data_opname.visibility == View.GONE){
                    img_collapse.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_down, null))
                    layout_data_opname.visibility = View.VISIBLE
                } else {
                    img_collapse.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_keyboard_up, null))
                    layout_data_opname.visibility = View.GONE
                }
            }

        })

    }

}
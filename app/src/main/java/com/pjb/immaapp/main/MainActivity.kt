package com.pjb.immaapp.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.pjb.immaapp.R
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        val tx = supportActionBar?.customView?.findViewById(R.id.tx_title_page) as TextView

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.title.observe(this, {
            tx.text = it
        })

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }
}
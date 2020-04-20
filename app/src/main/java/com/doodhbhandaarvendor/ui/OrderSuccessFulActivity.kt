package com.doodhbhandaarvendor.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.ui.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_order_success_ful.*

class OrderSuccessFulActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_success_ful)
        supportActionBar?.hide()
        btn_ok.setOnClickListener {
            startActivity((Intent(this,HomeFragment::class.java)))

        }
    }
}

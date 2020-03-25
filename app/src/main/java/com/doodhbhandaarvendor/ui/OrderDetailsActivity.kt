package com.doodhbhandaarvendor.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.doodhbhandaarvendor.R
import kotlinx.android.synthetic.main.item_history.*

class OrderDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        tv_orderDetails.setOnClickListener {
            startActivity(Intent(this,OrderDetailsActivity::class.java))

        }
    }
}

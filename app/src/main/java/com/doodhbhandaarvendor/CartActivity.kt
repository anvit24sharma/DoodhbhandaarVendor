package com.doodhbhandaarvendor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        btn_place_order.setOnClickListener {
            startActivity(Intent(this, OrderPlacedActivity::class.java))
            finish()

        }
    }
}

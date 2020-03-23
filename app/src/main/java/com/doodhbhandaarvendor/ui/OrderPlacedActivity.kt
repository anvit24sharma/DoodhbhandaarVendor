package com.doodhbhandaarvendor.ui

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.OrderPlaceModel
import com.doodhbhandaarvendor.model.OrderPlaceProductModel
import com.doodhbhandaarvendor.ui.fragments.HomeFragment.Companion.cartProductList
import com.doodhbhandaarvendor.ui.fragments.HomeFragment.Companion.orderDR
import kotlinx.android.synthetic.main.activity_confirm_place_order.*
import java.util.*
import kotlin.collections.ArrayList

class OrderPlacedActivity : AppCompatActivity() {

   // lateinit var cartProductList : ArrayList<ProductModel>
//    lateinit var totalCost : String
    var orderPlaceProductModel :ArrayList<OrderPlaceProductModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_place_order)

       // cartProductList= intent.getSerializableExtra("CartList") as ArrayList<ProductModel>
        //totalCost = intent.getStringExtra("TotalCost")?:""

        CartActivity.totalOrderCost.observe( this, androidx.lifecycle.Observer {
            tv_totalPrice.text = it.toString()
        })


        cartProductList.forEach {
            orderPlaceProductModel.add(OrderPlaceProductModel(it.product_name,it.product_cost,it.variants))
        }

        btn_place_order.setOnClickListener {
            val orderPlaceModel = OrderPlaceModel(orderPlaceProductModel,"abc",
                tv_address.text.toString(),
                et_date.text.toString(),
                findViewById<RadioButton>(rg_payment.checkedRadioButtonId).text.toString(),
                Date().toString(),
                "order_pending",
                tv_totalPrice.text.toString())

            val orderId = orderDR.push().key.toString()
            orderDR.child(orderId).setValue(orderPlaceModel)

        }

    }
}

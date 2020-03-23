package com.doodhbhandaarvendor.ui

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.prefs
import com.doodhbhandaarvendor.model.OrderPlaceModel
import com.doodhbhandaarvendor.model.OrderPlaceProductModel
import com.doodhbhandaarvendor.model.VariantModel
import com.doodhbhandaarvendor.ui.fragments.HomeFragment.Companion.cartProductList
import com.doodhbhandaarvendor.ui.fragments.HomeFragment.Companion.orderDR
import com.doodhbhandaarvendor.utils.Constants.Companion.ADDRESS
import com.doodhbhandaarvendor.utils.Constants.Companion.USER_ID
import kotlinx.android.synthetic.main.activity_confirm_place_order.*
import java.util.*
import kotlin.collections.ArrayList

class OrderPlacedActivity : AppCompatActivity() {

    var orderPlaceProductModel :ArrayList<OrderPlaceProductModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_place_order)

        CartActivity.totalOrderCost.observe( this, androidx.lifecycle.Observer {
            tv_totalPrice.text = it.toString()
        })

        tv_address.text = prefs.getString(ADDRESS,"")?:""

        cartProductList.forEach {
            val variants = ArrayList<VariantModel>()
            it.variants.forEach {   it1->
                if(it1.qty >0)
                    variants.add(it1)
            }
            orderPlaceProductModel.add(OrderPlaceProductModel(it.product_name,it.product_cost,variants))
        }

        btn_place_order.setOnClickListener {
            val orderPlaceModel = OrderPlaceModel(orderPlaceProductModel,
                prefs.getString(USER_ID,"")?:"",
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

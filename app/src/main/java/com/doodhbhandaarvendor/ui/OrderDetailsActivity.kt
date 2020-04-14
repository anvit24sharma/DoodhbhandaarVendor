package com.doodhbhandaarvendor.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.adapter.OrderDetailsAdapter
import com.doodhbhandaarvendor.model.OrderPlaceModel
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment.Companion.pastOrderList
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.activity_order_details.tv_totalPrice
import kotlinx.android.synthetic.main.item_order_detail.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailsActivity : AppCompatActivity() {
    var orderId = ""
    var orderPlaceModel : OrderPlaceModel = OrderPlaceModel()
    lateinit var orderDetailAdapter : OrderDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        orderId = intent.getStringExtra("orderId")?:""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pastOrderList.forEach {
                if(orderId == it.orderId)
                    orderPlaceModel = it
            }
        }
        initView()
    }

    private fun initView() {

        tv_orderNo.text = orderId
        val formatter1 = SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy")
        val date1: Date = formatter1.parse(orderPlaceModel.orderDate)
        val cal = Calendar.getInstance()
        cal.time = date1
        val formatedDate = cal[Calendar.DATE].toString() + "/" + (cal[Calendar.MONTH] + 1) + "/" + cal[Calendar.YEAR]
        tv_orderDate.text = formatedDate
        tv_scheduleDate.text = orderPlaceModel.schedule
        tv_totalPrice.text =getString(R.string.bill_amount_s,orderPlaceModel.totalCost)
        tv_total.text=getString(R.string.rs_s,orderPlaceModel.totalCost)
        tv_productStatus.text = orderPlaceModel.status
        tv_address_name.text=orderPlaceModel.address
        tv_payment.text=orderPlaceModel.paymentMode

        initRecyclerView()
    }

        private fun initRecyclerView() {
            val units = ArrayList<String>()
            orderPlaceModel.products.forEach {
                units.add(it.productCost.split("/")[1])
            }
            orderDetailAdapter = orderPlaceModel.products.let {
                OrderDetailsAdapter(this, it,units)
            }

            rv_order_products.apply {
                adapter = orderDetailAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
    }
}

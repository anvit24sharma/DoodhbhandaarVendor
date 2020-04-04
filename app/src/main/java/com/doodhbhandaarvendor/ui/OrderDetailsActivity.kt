package com.doodhbhandaarvendor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.adapter.OrderDetailsAdapter
import com.doodhbhandaarvendor.model.OrderPlaceModel
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment.Companion.pastOrderList
import kotlinx.android.synthetic.main.activity_order_details.*
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
        pastOrderList.forEach {
            if(orderId == it.orderId)
                orderPlaceModel = it
        }
        initView()
    }

    private fun initView() {

        orderNo.text = orderId
        val formatter1 = SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy")
        val date1: Date = formatter1.parse(orderPlaceModel.orderDate)
        val cal = Calendar.getInstance()
        cal.time = date1
        val formatedDate = cal[Calendar.DATE].toString() + "/" + (cal[Calendar.MONTH] + 1) + "/" + cal[Calendar.YEAR]
        orderDate.text = getString(R.string.order_date,formatedDate)
        scheduleDate.text =  getString(R.string.schedule_date,orderPlaceModel.schedule)
        tv_totalPrice.text = getString(R.string.bill_amount_s,orderPlaceModel.totalCost)
        tv_productStatus.text = orderPlaceModel.status

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

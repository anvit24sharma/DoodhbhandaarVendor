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
        orderDate.text = getString(R.string.order_date,orderPlaceModel.orderDate)
        scheduleDate.text =  getString(R.string.schedule_date,orderPlaceModel.schedule)
        tv_totalPrice.text = getString(R.string.bill_amount_s,orderPlaceModel.totalCost)
        tv_productStatus.text = orderPlaceModel.status

        initRecyclerView()
    }

        private fun initRecyclerView() {

            orderDetailAdapter = orderPlaceModel.products.let {
                OrderDetailsAdapter(this, it)
            }

            rv_order_products.apply {
                adapter = orderDetailAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
    }
}

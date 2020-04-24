package com.doodhbhandaarvendor.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.adapter.OrderDetailsAdapter
import com.doodhbhandaarvendor.auth.LoginActivity
import com.doodhbhandaarvendor.model.NotificationData
import com.doodhbhandaarvendor.model.NotificationModel
import com.doodhbhandaarvendor.model.NotificationResponse
import com.doodhbhandaarvendor.model.OrderPlaceModel
import com.doodhbhandaarvendor.notification.SendNotification.Companion.sendNotification
import com.doodhbhandaarvendor.remote.ApiCallInterface
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment.Companion.ordersDR
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment.Companion.pastOrderList
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment.Companion.userOrdersDR
import com.doodhbhandaarvendor.utils.Constants
import kotlinx.android.synthetic.main.activity_order_details.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderDetailsActivity : AppCompatActivity() {
    var orderId = ""
    companion object {
        var orderPlaceModel: OrderPlaceModel = OrderPlaceModel()
    }
    lateinit var orderDetailAdapter : OrderDetailsAdapter
    var totalBillDue = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        supportActionBar?.title = "Order Summary"

        orderId = intent.getStringExtra("orderId")?:""
        pastOrderList.forEach {
            if(orderId == it.orderId)
                    orderPlaceModel = it
        }

        initView()
        initClicks()
        initRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        orderDetailAdapter.notifyDataSetChanged()
    }
    @SuppressLint("SimpleDateFormat")
    private fun initView() {

        tv_orderNo.text = orderId
        val formatter1 = SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy")
        val date1: Date = formatter1.parse(orderPlaceModel.orderDate)
        val cal = Calendar.getInstance()
        cal.time = date1
        val formatedDate = cal[Calendar.DATE].toString() + "/" + (cal[Calendar.MONTH] + 1) + "/" + cal[Calendar.YEAR]
        tv_orderDate.text = formatedDate
        tv_scheduleDate.text = orderPlaceModel.schedule
        if(orderPlaceModel.totalCost == "0.0"){
            calculatePrice()
            tv_total.text =  getString(R.string.rs_s, totalBillDue.toString())
            totalBillDue =0.0
        }else {
            tv_total.text = getString(R.string.rs_s, orderPlaceModel.totalCost)
            totalBillDue = orderPlaceModel.totalCost.toDouble()
        }
        tv_productStatus.text = orderPlaceModel.status
        tv_address_name.text=orderPlaceModel.address
        tv_payment.text=orderPlaceModel.paymentMode
        tvSplIns.text = orderPlaceModel.specialInstruction

    }

    private fun initClicks() {
        iv_calendar.setOnClickListener {
            val intent = Intent(this@OrderDetailsActivity, CalendarActivity::class.java)
            intent.putExtra("orderId", orderPlaceModel.orderId)
            startActivity(intent)
        }

        btn_cancel.setOnClickListener {
            cancelOrder()
        }

        btn_edit.setOnClickListener {
            editOrder()
        }
    }

    private fun editOrder() {
        val intent = Intent(this@OrderDetailsActivity, CartActivity::class.java)
        intent.putExtra("from","orderDetails")
        intent.putExtra("order", orderPlaceModel)

        startActivity(intent)
    }



    private fun cancelOrder() {
        val cal =Calendar.getInstance()
        val datePickerDialog : DatePickerDialog
        val mY = cal.get(Calendar.YEAR)
        val mM = cal.get(Calendar.MONTH)
        val mD = cal.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(this@OrderDetailsActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            title = "Order??"
            Toast.makeText(this@OrderDetailsActivity, """$dayOfMonth/${month+ 1}/$year""", Toast.LENGTH_LONG).show()
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val lastOrderDate: Date = formatter.parse(orderPlaceModel.lastOrderDate)
            val scheduleDate: Date = formatter.parse(orderPlaceModel.schedule)
            var nextDate: Date = formatter.parse("""${mD}/${mM+1}/$mY""")
            val selectedDate: Date = formatter.parse("""${dayOfMonth}/${month+ 1}/$year""")
            cal.time = nextDate
            cal.add(Calendar.DATE, 1)
            nextDate = cal.time

            if(selectedDate.compareTo(lastOrderDate) == -1 && (nextDate.compareTo(selectedDate) == -1 || nextDate.compareTo(selectedDate)==0)) {
                while (nextDate.compareTo(selectedDate) != 0) {
                    userOrdersDR.child(orderPlaceModel.userId).child(orderId).child("""${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH)+1}-${cal.get(Calendar.YEAR)}""").setValue("user_canceled")
                    cal.time = nextDate
                    cal.add(Calendar.DATE, 1)
                    nextDate = cal.time
                }
                userOrdersDR.child(orderPlaceModel.userId).child(orderId).child("""${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH)+1}-${cal.get(Calendar.YEAR)}""").setValue("user_canceled")
                ordersDR.child(orderId).child("schedule").setValue("""${dayOfMonth+1}/${month + 1}/$year""")

                sendNotification("Order Canceled for some days by " + LoginActivity.prefs.getString(Constants.NAME,""))

            }
        }, mY, mM, mD)
        val title = TextView(this)
        title.text = "Order"
        datePickerDialog.setCustomTitle(title)
        datePickerDialog.show()
    }


    private fun calculatePrice() {
        orderPlaceModel.products.forEach {product->
                product.variants.forEach {
                    totalBillDue += product.productCost.split("/")[0].toDouble().times(it.variantName.split("/")[0].toDouble())
                }
        }
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

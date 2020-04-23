package com.doodhbhandaarvendor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.adapter.OrderDetailsAdapter
import com.doodhbhandaarvendor.model.OrderPlaceModel
import com.doodhbhandaarvendor.model.OrderPlaceProductModel
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment.Companion.ordersDR
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment.Companion.userOrdersDR
import com.doodhbhandaarvendor.utils.CircleDecorator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_order_summary.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : AppCompatActivity() {

    var orderId :String = ""
    var lastOrderDate = ""
    var isDaily =false
    var isWeekly =false
    var dates :ArrayList<CalendarDay> = ArrayList()
    var weeklyDates :ArrayList<CalendarDay> = ArrayList()
    val units = ArrayList<String>()
    var dueDates :ArrayList<CalendarDay> = ArrayList()
    var deliveredDates :ArrayList<CalendarDay> = ArrayList()
    var cancelledDates :ArrayList<CalendarDay> = ArrayList()
    var  orderModel : OrderPlaceModel = OrderPlaceModel()
    lateinit var orderDetailAdapter : OrderDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        orderId = intent.getStringExtra("orderId")?:""
        supportActionBar?.title = "Calendar"

        initialiseCalendar()
        selectCalendarDate()

    }

    private fun selectCalendarDate() {
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            initOrderView(date)
            orderDetailAdapter.notifyDataSetChanged()
        })
    }


    private fun initialiseCalendar() {
        ordersDR.child(orderId).addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                orderModel = snapshot.getValue(OrderPlaceModel::class.java)!!
                lastOrderDate = orderModel.lastOrderDate.toString()

                orderModel.products.forEach {
                    if (it.subscriptionPlan == "Daily") {
                        isDaily = true
                    } else if (it.subscriptionPlan == "Weekly") {
                        isWeekly = true
                    }
                }

                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val cal =Calendar.getInstance()
                val lastOrderDate: Date = formatter.parse(orderModel.lastOrderDate)
                cal.time = lastOrderDate

                if (isDaily) {
                    for (i in 0..29) {
                        dates.add(CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH)))
                        cal.add(Calendar.DATE, -1)
                    }
                }else if(!isDaily && isWeekly){
                    cal.add(Calendar.DATE, -1)
                    for (i in 0..4) {
                        dates.add(CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH)))
                        cal.add(Calendar.DATE, -7)
                    }
                }else if(!isDaily && !isWeekly){
                    dates.add(CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH)))
                }

                calendarView.addDecorator(CircleDecorator(this@CalendarActivity, R.drawable.select_color_blue, dates))

                userOrdersDR.child(orderModel.userId).addValueEventListener(object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.child(orderId).children.forEach {
                            val date = it.key?.split("-")?.get(0)?.toInt()
                            val month = it.key?.split("-")?.get(1)?.toInt()
                            val year = it.key?.split("-")?.get(2)?.toInt()

                            if(it.getValue(String::class.java) == "order_delivered") {
                                deliveredDates.add(CalendarDay.from(year!!, month!!, date!!))
                            }
                            else if(it.getValue(String::class.java) == "user_canceled" || it.getValue(String::class.java) == "admin_canceled") {
                                cancelledDates.add(CalendarDay.from(year!!, month!!, date!!))
                            }
                        }
                        calendarView.addDecorator(CircleDecorator(this@CalendarActivity, R.drawable.select_color_green, deliveredDates))
                        calendarView.addDecorator(CircleDecorator(this@CalendarActivity, R.drawable.select_color_red, cancelledDates))

                        val cal = Calendar.getInstance()
                        dueDates.add(CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH)))
                        calendarView.addDecorator(CircleDecorator(this@CalendarActivity, R.drawable.select_color_yellow, dueDates))
                        initOrderView(dueDates[0])
                    }
                })
            }
        })
    }
    private fun initOrderView(calendarDay: CalendarDay) {
        val products = ArrayList<OrderPlaceProductModel>()
        products.clear()
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val cal =Calendar.getInstance()
        orderModel.products.forEach {
            val lastOrderDate: Date = formatter.parse(orderModel.lastOrderDate)
            cal.time = lastOrderDate

            if(it.subscriptionPlan == "Weekly") {
                weeklyDates.clear()
                cal.add(Calendar.DATE, -1)
                for (i in 0..4) {
                    weeklyDates.add(CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH)))
                    cal.add(Calendar.DATE, -7)
                }
                weeklyDates.forEach {date ->
                    if(date == calendarDay)
                        products.add(it)
                }
            }else if(it.subscriptionPlan == "Daily") {
//                val scheduleDate: Date = formatter.parse(orderModel.schedule)
//                val currentDate = formatter.parse(""+calendarDay.day+"/"+calendarDay.month+"/"+calendarDay.year)
//                if ((currentDate.compareTo(lastOrderDate) == -1  || currentDate.compareTo(lastOrderDate) == 0) && (scheduleDate.compareTo(currentDate)== -1 || scheduleDate.compareTo(currentDate)== -1)) {
                    products.add(it)
            }else{
                val date = it.subscriptionPlan.split("/")[0].toInt()
                val month = it.subscriptionPlan.split("/")[1].toInt()
                val year = it.subscriptionPlan.split("/")[2].toInt()
                if(CalendarDay.from(year,month,date) == calendarDay)
                    products.add(it)
            }
            units.add(it.productCost.split("/")[1])

        }

        orderDetailAdapter = OrderDetailsAdapter(this,products,units)

        rv_product.apply {
            adapter = orderDetailAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }
}

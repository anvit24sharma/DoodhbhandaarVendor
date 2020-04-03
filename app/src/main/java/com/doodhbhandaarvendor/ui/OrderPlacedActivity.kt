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
    var scheduleDate :String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_place_order)

        initCalendar()
        initCalendarClicks()

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
            val orderId = orderDR.push().key.toString()

            val orderPlaceModel = OrderPlaceModel(orderPlaceProductModel,
                prefs.getString(USER_ID,"")?:"",
                tv_address.text.toString(),
                scheduleDate,
                findViewById<RadioButton>(rg_payment.checkedRadioButtonId).text.toString(),
                Date().toString(),
                "order_pending",
                tv_totalPrice.text.toString(),
                orderId)

            orderDR.child(orderId).setValue(orderPlaceModel)


        }

    }

    private fun initCalendarClicks() {
        llDate1.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            val currentDate =  Date()
            val day = currentDate.date
            val month = currentDate.month + 1
            val year = currentDate.year +1900
            scheduleDate = "" + day + "/" + month + "/" + year
        }
        llDate2.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            val currentDate =  Date(Date().time + 24 * 60 * 60 * 1000)
            val day = currentDate.date
            val month = currentDate.month + 1
            val year = currentDate.year +1900
            scheduleDate = "" + day + "/" + month + "/" + year
        }
        llDate3.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            val currentDate =  Date(Date().time + 2*24 * 60 * 60 * 1000)
            val day = currentDate.date
            val month = currentDate.month + 1
            val year = currentDate.year +1900
            scheduleDate = "" + day + "/" + month + "/" + year
        }
        llDate4.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            val currentDate =  Date(Date().time + 3*24 * 60 * 60 * 1000)
            val day = currentDate.date
            val month = currentDate.month + 1
            val year = currentDate.year +1900
            scheduleDate = "" + day + "/" + month + "/" + year
        }
        llDate5.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            val currentDate =  Date(Date().time + 4*24 * 60 * 60 * 1000)
            val day = currentDate.date
            val month = currentDate.month + 1
            val year = currentDate.year +1900
            scheduleDate = "" + day + "/" + month + "/" + year
        }
        llDate6.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            val currentDate =  Date(Date().time + 5*24 * 60 * 60 * 1000)
            val day = currentDate.date
            val month = currentDate.month + 1
            val year = currentDate.year +1900
            scheduleDate = "" + day + "/" + month + "/" + year
        }
        llDate7.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.selected_calendar_box)

            val currentDate =  Date(Date().time + 6*24 * 60 * 60 * 1000)
            val day = currentDate.date
            val month = currentDate.month + 1
            val year = currentDate.year + 1900
            scheduleDate = "" + day + "/" + month + "/" + year
        }
    }


    private fun initCalendar() {
        val calendar :Calendar = Calendar.getInstance()

        val currentDate =  Date()
        val day = currentDate.date
        val month = currentDate.month + 1
        val year = currentDate.year +1900
        scheduleDate = "" + day + "/" + month + "/" + year

        tvDate1.text = calendar.get(Calendar.DATE).toString()
        tvMonth1.text = getMonthName(calendar.get(Calendar.MONTH))
        llDate1.setBackgroundResource(R.drawable.selected_calendar_box)

        calendar.add(Calendar.DAY_OF_YEAR,1)
        tvDate2.text = calendar.get(Calendar.DATE).toString()
        tvMonth2.text =  getMonthName(calendar.get(Calendar.MONTH))

        calendar.add(Calendar.DAY_OF_YEAR,1)
        tvDate3.text = calendar.get(Calendar.DATE).toString()
        tvMonth3.text =  getMonthName(calendar.get(Calendar.MONTH))

        calendar.add(Calendar.DAY_OF_YEAR,1)
        tvDate4.text = calendar.get(Calendar.DATE).toString()
        tvMonth4.text = getMonthName(calendar.get(Calendar.MONTH))

        calendar.add(Calendar.DAY_OF_YEAR,1)
        tvDate5.text = calendar.get(Calendar.DATE).toString()
        tvMonth5.text =  getMonthName(calendar.get(Calendar.MONTH))

        calendar.add(Calendar.DAY_OF_YEAR,1)
        tvDate6.text = calendar.get(Calendar.DATE).toString()
        tvMonth6.text =  getMonthName(calendar.get(Calendar.MONTH))

        calendar.add(Calendar.DAY_OF_YEAR,1)
        tvDate7.text = calendar.get(Calendar.DATE).toString()
        tvMonth7.text = getMonthName(calendar.get(Calendar.MONTH))


    }

    private fun getMonthName(month: Int): String? {

        return when(month){
            0->"JAN"
            1->"FEB"
            2->"MAR"
            3->"APR"
            4->"MAY"
            5->"JUN"
            6->"JUL"
            7->"AUG"
            8->"SEP"
            9->"OCT"
            10->"NOV"
            11->"DEC"
            else -> "WRONG"
        }
    }
}

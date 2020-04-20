package com.doodhbhandaarvendor.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.adapter.OrderPlaceAdapter
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.prefs
import com.doodhbhandaarvendor.model.OrderPlaceModel
import com.doodhbhandaarvendor.model.OrderPlaceProductModel
import com.doodhbhandaarvendor.model.ProductModel
import com.doodhbhandaarvendor.model.VariantModel
import com.doodhbhandaarvendor.ui.fragments.EditAddressBottomSheet
import com.doodhbhandaarvendor.ui.fragments.HomeFragment.Companion.cartProductList
import com.doodhbhandaarvendor.ui.fragments.HomeFragment.Companion.orderDR
import com.doodhbhandaarvendor.ui.fragments.HomeFragment.Companion.userOrdersDR
import com.doodhbhandaarvendor.ui.fragments.PaymentCollectionBottomSheet
import com.doodhbhandaarvendor.utils.Constants.Companion.ADDRESS
import com.doodhbhandaarvendor.utils.Constants.Companion.USER_ID
import kotlinx.android.synthetic.main.activity_confirm_place_order.*
import java.util.*
import kotlin.collections.ArrayList

class  OrderPlacedActivity : AppCompatActivity() ,
    EditAddressBottomSheet.BottomSheetListner,
    PaymentCollectionBottomSheet.PaymentCollectionListner{
    lateinit var addAddress : EditAddressBottomSheet
    lateinit var paymentCollection: PaymentCollectionBottomSheet
    var orderPlaceProductModel :ArrayList<OrderPlaceProductModel> = ArrayList()
    var scheduleDate :String =""
    var paymentCollectDay = true
    var tvPaymentCollection: TextView? = null
    var orderPlaceAdapter : OrderPlaceAdapter? = null
    var currentDate : Date =Date()
    var day =0
    var month  =-1
    var year  =0
    var selectedColumn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_place_order)

        supportActionBar?.hide()

        paymentCollection = PaymentCollectionBottomSheet()
        addAddress = EditAddressBottomSheet()
        initRecyclerView()
        initCalendar()
        initCalendarClicks()

        CartActivity.totalOrderCost.observe( this, androidx.lifecycle.Observer {
            tv_totalPrice.text = "₹$it"
        })

        tvAddress.text = prefs.getString(ADDRESS,"")?:""

        btn_place_order.setOnClickListener {
            placeOrder()
        }

        tvChange.setOnClickListener {
            addAddress.show(supportFragmentManager, "Example")
        }
        plc_order.setOnClickListener {
            startActivity((Intent(this,OrderSuccessFulActivity::class.java)))

        }

    }

    private fun placeOrder() {
        var selectedMode =  ""
        var lastScheduleDate = scheduleDate
        orderPlaceProductModel.clear()

        cartProductList.forEach {
            val variants = ArrayList<VariantModel>()
            it.variants.forEach {   it1->
                if(it1.qty >0)
                    variants.add(it1)
            }
            if(it.paymentCollectionDay !=""){
                orderPlaceProductModel.add(OrderPlaceProductModel(it.product_name,it.product_cost,variants,it.subscriptionPlan,"",it.paymentCollectionDay,scheduleDate))
            }else {
                paymentCollectDay = false
            }
            if(it.subscriptionPlan == "Daily" || it.subscriptionPlan =="Weekly"){
                val cal = Calendar.getInstance()
                cal.add(Calendar.DAY_OF_YEAR,29+selectedColumn)
                lastScheduleDate =""+ cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)
            }

        }

        selectedMode = if(rg_payment.checkedRadioButtonId != -1)
            findViewById<RadioButton>(rg_payment.checkedRadioButtonId).text.toString()
        else
            ""
        if(!paymentCollectDay) {
            Toast.makeText(this, "Choose Payment Collection Date",Toast.LENGTH_SHORT).show()
            return
        }
        if(scheduleDate!="" &&  selectedMode!="" ) {
            val orderId = orderDR.push().key.toString()
            val orderPlaceModel = OrderPlaceModel(
                orderPlaceProductModel,
                prefs.getString(USER_ID, "") ?: "",
                tvAddress.text.toString(),
                scheduleDate,
                lastScheduleDate,
                selectedMode,
                Date().toString(),
                "order_pending",
                "0.0",
                orderId)

            orderDR.child(orderId).setValue(orderPlaceModel)

            userOrdersDR.child(prefs.getString(USER_ID, "") ?: "")
                .child(orderId).setValue(orderId).addOnCompleteListener{
                    cartProductList.clear()
                    val intent = Intent(this@OrderPlacedActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }

        }else{
            Toast.makeText(this,"Select payment mode",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView() {
           orderPlaceAdapter= cartProductList.let { OrderPlaceAdapter(this, it, object : OrderPlaceAdapter.OnItemClickListener {
                override fun OnChooseClick(position: Int, view: View, tvPaymentDate: TextView) {
                    val args = Bundle()
                    args.putParcelable("productModel",it[position])
                    paymentCollection.arguments=args
                    paymentCollection.show(supportFragmentManager,"ex")
                    tvPaymentCollection = tvPaymentDate
                }
                override fun onApplyCouponClick(position: Int, view: View) {
                }

            })
        }
        rv_placed_orders.apply {
            adapter = orderPlaceAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
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

            currentDate =  Date(Date().time + 24 * 60 * 60 * 1000)
            day = currentDate.date
            month = currentDate.month + 1
            year = currentDate.year +1900
            scheduleDate = "$day/$month/$year"
            setSubscriptionOnceDate()
            selectedColumn =1
        }
        llDate2.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            currentDate =  Date(Date().time + 2*24 * 60 * 60 * 1000)
            day = currentDate.date
            month = currentDate.month + 1
            year = currentDate.year +1900
            scheduleDate = "$day/$month/$year"
            setSubscriptionOnceDate()
            selectedColumn =2
        }
        llDate3.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            currentDate =  Date(Date().time + 3*24 * 60 * 60 * 1000)
            day = currentDate.date
            month = currentDate.month + 1
            year = currentDate.year +1900
            scheduleDate = "$day/$month/$year"
            setSubscriptionOnceDate()
            selectedColumn =3

        }
        llDate4.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            currentDate =  Date(Date().time + 4*24 * 60 * 60 * 1000)
            day = currentDate.date
            month = currentDate.month + 1
            year = currentDate.year +1900
            scheduleDate = "$day/$month/$year"
            setSubscriptionOnceDate()
            selectedColumn =4

        }
        llDate5.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            currentDate =  Date(Date().time + 5*24 * 60 * 60 * 1000)
            day = currentDate.date
            month = currentDate.month + 1
            year = currentDate.year +1900
            scheduleDate = "$day/$month/$year"
            setSubscriptionOnceDate()
            selectedColumn =5

        }
        llDate6.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.selected_calendar_box)
            llDate7.setBackgroundResource(R.drawable.calender_box)

            currentDate =  Date(Date().time + 6*24 * 60 * 60 * 1000)
            day = currentDate.date
            month = currentDate.month + 1
            year = currentDate.year +1900
            scheduleDate = "$day/$month/$year"
            setSubscriptionOnceDate()
            selectedColumn =6

        }
        llDate7.setOnClickListener {
            llDate1.setBackgroundResource(R.drawable.calender_box)
            llDate2.setBackgroundResource(R.drawable.calender_box)
            llDate3.setBackgroundResource(R.drawable.calender_box)
            llDate4.setBackgroundResource(R.drawable.calender_box)
            llDate5.setBackgroundResource(R.drawable.calender_box)
            llDate6.setBackgroundResource(R.drawable.calender_box)
            llDate7.setBackgroundResource(R.drawable.selected_calendar_box)

            currentDate =  Date(Date().time + 7*24 * 60 * 60 * 1000)
            day = currentDate.date
            month = currentDate.month + 1
            year = currentDate.year + 1900
            scheduleDate = "$day/$month/$year"
            setSubscriptionOnceDate()
            selectedColumn =7

        }
    }

    private fun initCalendar() {
        val calendar :Calendar = Calendar.getInstance()

        val currentDate =  Date(Date().time + 24 * 60 * 60 * 1000)
        val day = currentDate.date
        val month = currentDate.month + 1
        val year = currentDate.year +1900
        scheduleDate = "$day/$month/$year"
        selectedColumn =1
        setSubscriptionOnceDate()

        calendar.add(Calendar.DAY_OF_YEAR,1)
        tvDate1.text = calendar.get(Calendar.DATE).toString()
        tvMonth1.text =  getMonthName(calendar.get(Calendar.MONTH))
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

    private fun setSubscriptionOnceDate() {
        cartProductList.forEach {
            if(it.subscriptionPlan != "Daily" && it.subscriptionPlan != "Weekly"){
                it.subscriptionPlan = scheduleDate
                orderPlaceAdapter?.notifyDataSetChanged()
            }
        }

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

    override fun onSaveClicked(string: String?) {
        tvAddress.text = string
        addAddress.dismiss()
    }

    override fun onPaymentDaySelected(string: String,productModel: ProductModel) {
        productModel.paymentCollectionDay = string
        tvPaymentCollection?.text = string
        paymentCollection.dismiss()
    }


}

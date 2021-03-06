package com.doodhbhandaarvendor.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.adapter.CartAdapter
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.prefs
import com.doodhbhandaarvendor.model.*
import com.doodhbhandaarvendor.notification.SendNotification.Companion.sendNotification
import com.doodhbhandaarvendor.remote.ApiCallInterface
import com.doodhbhandaarvendor.ui.OrderDetailsActivity.Companion.orderPlaceModel
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment.Companion.ordersDR
import com.doodhbhandaarvendor.ui.fragments.HomeFragment
import com.doodhbhandaarvendor.utils.Constants.Companion.NAME
import kotlinx.android.synthetic.main.activity_cart.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity() {

    companion object {
         var totalOrderCost: MutableLiveData<Double> = MutableLiveData()
    }
    lateinit var cartAdapter :CartAdapter
    var orderModel =OrderPlaceModel()
    var from =""
    val productModelList = ArrayList<ProductModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        orderModel = intent.getParcelableExtra("order")?:orderModel
        from = intent.getStringExtra("from")?:""

        supportActionBar?.title = "Your Cart"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        initClicks()
        totalOrderCost.value = 0.0
        initRecyclerView()

        totalOrderCost.observe( this, Observer {
            if(from == "orderDetails") {
                btn_confirm_order.text = getString(R.string.update_order_s, it.toString())
            }else{
                btn_confirm_order.text = getString(R.string.confirm_order, it.toString())
            }
        })
    }

    private fun initView() {
        if(from == "orderDetails"){
            btn_confirm_order.text = "Update Order"
            supportActionBar?.title = "Update Order"
        }
    }

    private fun initClicks() {
        btn_confirm_order.setOnClickListener {
            if(from == "orderDetails") {
                updateOrder()
            }else {
                confirmOrder()
            }
        }
    }


    private fun updateOrder() {

        var c=0
        val moreThanOneVariants = ArrayList<VariantModel>()
        var allItemSelected =true


        for (productModel in productModelList) {
            var itemSelected = false

            productModel.variants.forEach {
                if(it.qty >0){
                    itemSelected =true
                }
            }
            if(!itemSelected) {
                allItemSelected = false
                break
            }
        }

        if(!allItemSelected){
            Toast.makeText(this,"Select Quantity for each Product",Toast.LENGTH_SHORT).show()
            return
        }

        productModelList.forEach{ product->
            orderPlaceModel.products[c].variants.clear()
            moreThanOneVariants.clear()
            product.variants.forEach{ variant->
                if(variant.qty>0){
                    moreThanOneVariants.add(variant)
                }
            }
            orderPlaceModel.products[c].variants.addAll(moreThanOneVariants)
            ordersDR.child(orderModel.orderId).child("products").child(c.toString()).child("variants").setValue(moreThanOneVariants)
            c++
        }
        Toast.makeText(this,"Order Updated",Toast.LENGTH_SHORT).show()
        sendNotification("Order Updated by "+prefs.getString(NAME,""))
        finish()

    }

    private fun confirmOrder() {
        var allItemSelected =true
        var planSelected = true
        for (productModel in HomeFragment.cartProductList) {
            var itemSelected = false

            if(productModel.subscriptionPlan ==""){
                planSelected = false
            }
            productModel.variants.forEach {
                if(it.qty >0){
                    itemSelected =true
                }
            }
            if(!itemSelected) {
                allItemSelected = false
                break
            }
        }

        if(totalOrderCost.value == 0.0 || !allItemSelected){
            Toast.makeText(this,"Select Quantity for each Product",Toast.LENGTH_SHORT).show()
        }
        else if(!planSelected){
            Toast.makeText(this,"Please Select Subscription Plan",Toast.LENGTH_SHORT).show()

        } else{
            val intent = Intent(this, OrderPlacedActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initRecyclerView() {
        if(from == "orderDetails") {

            orderModel.products.forEach{
                productModelList.add(ProductModel(it.productName,it.productCost,"","",it.variants,it.subscriptionPlan,it.paymentCollectionDay,""))
            }
            cartAdapter = productModelList.let {
                CartAdapter(this, it, object : CartAdapter.OnItemClickListener {
                    override fun onOnetimeClick(position: Int, view: View, btnDaily: Button, btnWeekly: Button) {
                       Toast.makeText(this@CartActivity,"Cannot Edit Subscription Plan",Toast.LENGTH_SHORT).show()
                    }

                    override fun onWeeklyClick(position: Int, btnOnetime: Button, btnDaily: Button, view: View) {
                        Toast.makeText(this@CartActivity,"Cannot Edit Subscription Plan",Toast.LENGTH_SHORT).show()
                    }

                    override fun onDailyClick(position: Int, btnOnetime: Button, view: View, btnWeekly: Button) {
                        Toast.makeText(this@CartActivity,"Cannot Edit Subscription Plan",Toast.LENGTH_SHORT).show()

                    }

                    override fun onCancelClick(position: Int, view: View) {
                        it.removeAt(position)
                        cartAdapter.notifyDataSetChanged()
                    }
                },from)
            }
        }else{
            cartAdapter = HomeFragment.cartProductList.let {
                CartAdapter(this, it, object : CartAdapter.OnItemClickListener {
                    override fun onOnetimeClick(position: Int, view: View, btnDaily: Button, btnWeekly: Button) {
                        view.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.selected_btn)
                        btnDaily.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.white_btn)
                        btnWeekly.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.white_btn)
                        it[position].subscriptionPlan = "Once"

//                        val cal =Calendar.getInstance()
//                        val datePickerDialog : DatePickerDialog
//                        val mY = cal.get(Calendar.YEAR)
//                        val mM = cal.get(Calendar.MONTH)
//                        val mD = cal.get(Calendar.DAY_OF_MONTH)
//
//                        datePickerDialog = DatePickerDialog(this@CartActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//                            Toast.makeText(this@CartActivity, """$dayOfMonth/${month+ 1}/$year""", Toast.LENGTH_LONG).show()
//                            it[position].subscriptionPlan = "$dayOfMonth/${month+ 1}/$year"
//                        }, mY, mM, mD)
//                        datePickerDialog.show()
                    }
                    override fun onWeeklyClick(position: Int, btnOnetime: Button, btnDaily: Button, view: View ){
                        btnDaily.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.white_btn)
                        btnOnetime.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.white_btn)
                        view.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.selected_btn)
                        it[position].subscriptionPlan = "Weekly"
                    }
                    override fun onDailyClick(position: Int, btnOnetime: Button, view: View, btnWeekly: Button) {
                        btnWeekly.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.white_btn)
                        btnOnetime.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.white_btn)
                        view.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.selected_btn)
                        it[position].subscriptionPlan = "Daily"

                    }

                    override fun onCancelClick(position: Int, view: View) {
                        it.removeAt(position)
                        cartAdapter.notifyDataSetChanged()
                    }
                },from)
            }
        }
            rv_cart.apply {
                adapter = cartAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }

}

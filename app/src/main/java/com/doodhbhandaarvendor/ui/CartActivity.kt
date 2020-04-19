package com.doodhbhandaarvendor.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.doodhbhandaarvendor.ui.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_cart.*
import java.util.*

class CartActivity : AppCompatActivity() {

    companion object {
         var totalOrderCost: MutableLiveData<Double> = MutableLiveData()
    }
    lateinit var cartAdapter :CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar?.title = "Cart"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_confirm_order.setOnClickListener {

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
        totalOrderCost.value = 0.0
        initRecyclerView()

        totalOrderCost.observe( this, Observer {
            btn_confirm_order.text = getString(R.string.confirm_order, it.toString())
        })
    }

        private fun initRecyclerView() {
            cartAdapter = HomeFragment.cartProductList.let {
                CartAdapter(this, it, object : CartAdapter.OnItemClickListener {
                    override fun onOnetimeClick(position: Int, view: View, btnDaily: Button, btnWeekly: Button) {
                        view.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.selected_btn)
                        btnDaily.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.white_btn)
                        btnWeekly.background = ContextCompat.getDrawable(this@CartActivity, R.drawable.white_btn)
                        val cal =Calendar.getInstance()
                        val datePickerDialog : DatePickerDialog
                        val mY = cal.get(Calendar.YEAR)
                        val mM = cal.get(Calendar.MONTH)
                        val mD = cal.get(Calendar.DAY_OF_MONTH)

                        datePickerDialog = DatePickerDialog(this@CartActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                            Toast.makeText(this@CartActivity, """$dayOfMonth/${month+ 1}/$year""", Toast.LENGTH_LONG).show()
                            it[position].subscriptionPlan = "$dayOfMonth/${month+ 1}/$year"
                        }, mY, mM, mD)
                        datePickerDialog.show()
                    }

                    override fun onWeeklyClick(position: Int, btnOnetime: Button, btnDaily: Button, view: View) {
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
                })
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

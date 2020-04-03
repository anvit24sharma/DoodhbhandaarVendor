package com.doodhbhandaarvendor.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.adapter.CartAdapter
import com.doodhbhandaarvendor.ui.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_cart.*

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
            for (productModel in HomeFragment.cartProductList) {
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

            if(totalOrderCost.value == 0.0 || !allItemSelected){
                Toast.makeText(this,"Select Quantity for each Product",Toast.LENGTH_SHORT).show()
            }else {
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
                    override fun onAddClick(position: Int, view: View) {

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

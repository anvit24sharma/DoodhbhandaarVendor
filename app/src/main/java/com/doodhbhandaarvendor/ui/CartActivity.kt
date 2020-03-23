package com.doodhbhandaarvendor.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
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

        btn_confirm_order.setOnClickListener {
            val intent =Intent(this,OrderPlacedActivity::class.java)
            //intent.putExtra("CartList", cartProductList)
            //intent.putExtra("TotalCost", totalOrderCost.value.toString())
            startActivity(intent)
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


}

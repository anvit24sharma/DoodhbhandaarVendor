package com.doodhbhandaarvendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaar.adapter.HistoryAdapter
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.prefs
import com.doodhbhandaarvendor.model.OrderPlaceModel
import com.doodhbhandaarvendor.ui.OrderDetailsActivity
import com.doodhbhandaarvendor.utils.Constants.Companion.USER_ID
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*


class HistoryFragment : Fragment() {

    companion object {
        val pastOrderList = ArrayList<OrderPlaceModel>()
    }

    lateinit var historyAdapter: HistoryAdapter
    val ordersIdList =  ArrayList<String>()
    lateinit var  userOrdersDR : DatabaseReference
    lateinit var  ordersDR : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_count.visibility = GONE
        iv_cart.visibility = GONE
        userOrdersDR = FirebaseDatabase.getInstance().getReference("UserOrders")
        ordersDR = FirebaseDatabase.getInstance().getReference("Orders")

        getOrders()

        historyAdapter = HistoryAdapter(context, pastOrderList, object : HistoryAdapter.onItemClickListener {
            override fun onOrderDetailsClick(position: Int, view: View?) {
                val intent = Intent(context,OrderDetailsActivity::class.java)
                intent.putExtra("orderId",pastOrderList[position].orderId)
                startActivity(intent)
            }
        })

        rv_product.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun getOrders() {

        userOrdersDR.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                ordersIdList.clear()
                snapshot.child(prefs.getString(USER_ID,"")?:"").children.forEach {
                    ordersIdList.add(it.getValue(String::class.java)!!)
                }
                getOrderDetails()
            }

        })

    }

    private fun getOrderDetails() {
        ordersDR.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {

                pastOrderList.clear()
                ordersIdList.forEach {
                    val  orderModel = snapshot.child(it).getValue(OrderPlaceModel::class.java)
                    pastOrderList.add( orderModel?: OrderPlaceModel())
                }
                historyAdapter.notifyDataSetChanged()
            }

        })



    }

}

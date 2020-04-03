package com.doodhbhandaar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.OrderPlaceModel

class HistoryAdapter(
    private var context: Context?,
    private var orders: ArrayList<OrderPlaceModel>,
    private var mlistener: onItemClickListener
) :
    RecyclerView.Adapter<HistoryAdapter.OrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.item_history,
                parent,
                false)
        )
    }

    override fun onBindViewHolder(
        holder: OrderViewHolder,
        position: Int
    ) {

        holder.orderNo.text = orders[position].orderId
        holder.amount.text = orders[position].totalCost
        holder.status.text = orders[position].status
        holder.date.text = orders[position].orderDate
        holder.tvOrderDetails.setOnClickListener {
            mlistener.onOrderDetailsClick(position, it)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    class OrderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var orderNo: TextView
        var amount: TextView
        var status: TextView
        var date: TextView
        var tvOrderDetails: TextView

        init {
            orderNo = itemView.findViewById(R.id.orderNo)
            amount = itemView.findViewById(R.id.tv_totalPrice)
            status = itemView.findViewById(R.id.tv_productStatusDate)
            date = itemView.findViewById(R.id.orderDate)
            tvOrderDetails = itemView.findViewById(R.id.tv_orderDetails)
        }
    }

    interface onItemClickListener {
        fun onOrderDetailsClick(position: Int, view: View?)
    }


}
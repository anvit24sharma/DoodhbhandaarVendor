package com.doodhbhandaar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.OrderPlaceModel
import java.text.SimpleDateFormat
import java.util.*

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: OrderViewHolder,
        position: Int
    ) {

        holder.orderNo.text = orders[position].orderId
        var totalCost =0.0

        if(orders[position].totalCost =="0.0") {
            orders[position].products.forEach { product ->
                product.variants.forEach {
                    totalCost += product.productCost.split("/")[0].toDouble()
                        .times(it.variantName.split("/")[0].toDouble())
                }
            }
            holder.amount.text = totalCost.toString()
        }else{
            holder.amount.text = orders[position].totalCost + "(Due)"

        }
        holder.status.text = orders[position].status
        val formatter1 = SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy")
        val date1: Date = formatter1.parse(orders[position].orderDate)
        val cal = Calendar.getInstance()
        cal.time = date1
        val formatedDate = cal[Calendar.DATE].toString() + "/" + (cal[Calendar.MONTH] + 1) + "/" + cal[Calendar.YEAR]
        holder.date.text =formatedDate
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
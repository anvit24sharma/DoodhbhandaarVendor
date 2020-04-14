package com.doodhbhandaarvendor.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.OrderPlaceProductModel


class OrderDetailsAdapter(
    private var mContext: Context?,
    private var product: ArrayList<OrderPlaceProductModel>,
    private var units: ArrayList<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_order_detail, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(product[position],position)
    }

    override fun getItemCount(): Int {
        return product.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName= itemView.findViewById<TextView>(R.id.tv_productName)
        private val rvVariants = itemView.findViewById<RecyclerView>(R.id.rv_variants)

        private lateinit var variantAdapter :OrderDetailVariantAdapter

        fun setData(orderModel: OrderPlaceProductModel, position: Int) {
            tvProductName.text = orderModel.productName

            variantAdapter = orderModel.let {
                OrderDetailVariantAdapter(mContext, it.variants,units[position])
            }
            rvVariants.apply {
                adapter = variantAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }

        }
    }


}






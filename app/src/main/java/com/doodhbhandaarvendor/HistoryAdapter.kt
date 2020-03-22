package com.doodhbhandaarvendor


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


@Suppress("UNCHECKED_CAST")
class HistoryAdapter(
    private var mContext: Context?,
    private var product: ArrayList<HistoryModel>,
    private var mListener: OnItemClickListener
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  holder.setData(product[position],position)

    }

    override fun getItemCount(): Int {
        return product.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgProduct = itemView.findViewById<TextView>(R.id.img_product)
        private val tvProductName = itemView.findViewById<TextView>(R.id.tv_productName)
        private val tvTotalPrice = itemView.findViewById<TextView>(R.id.tv_totalPrice)
        private val tvTotalQuantity = itemView.findViewById<TextView>(R.id.tv_totalQuantity)
        private val tvProductStatus = itemView.findViewById<TextView>(R.id.tv_productStatus)
        private val tvProductStatusDate = itemView.findViewById<TextView>(R.id.tv_productStatusDate)


    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)

    }
}








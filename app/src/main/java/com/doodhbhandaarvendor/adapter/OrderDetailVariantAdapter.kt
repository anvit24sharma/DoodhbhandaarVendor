package com.doodhbhandaarvendor.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.VariantModel

class OrderDetailVariantAdapter(
    private val context: Context?,
    private val products: ArrayList<VariantModel>,
    private val unit: String
) : RecyclerView.Adapter<OrderDetailVariantAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_quantity_detail, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if( products[position].variantName.toDouble() >=1.0 && unit == "Kg")
            holder.variantName.text = products[position].variantName + " " +unit
        else if( products[position].variantName.toDouble() <1.0 && unit == "Kg")
            holder.variantName.text = ""+(products[position].variantName.toDouble()*1000 )+ " " +"gm"
        else if( products[position].variantName.toDouble() >=1.0 && unit == "Ltr")
            holder.variantName.text = products[position].variantName + " " +unit
        else if( products[position].variantName.toDouble() <1.0 && unit == "Ltr")
            holder.variantName.text = ""+ products[position].variantName.toDouble() * 1000 + " " +"ml"

        holder.totalQty.text = products[position].qty.toString()

    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  variantName = itemView.findViewById<View>(R.id.qty1) as TextView
        val  totalQty = itemView.findViewById<View>(R.id.qty2) as TextView

    }


}
package com.doodhbhandaarvendor.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R


class VariantAdapter(
    private var mContext: Context?,
    private var variants: ArrayList<String>,
    private var mListener: OnItemClickListener
) : RecyclerView.Adapter<VariantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_variants, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(variants[position],position)

    }

    override fun getItemCount(): Int {
        return variants.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val tvVariantName= itemView.findViewById<TextView>(R.id.tv_half_kg)



        fun setData(variantName: String, position: Int) {
            tvVariantName.text = variantName


        }




    }
    interface OnItemClickListener {
        fun onAddClick(position: Int, view: View)
    }

}






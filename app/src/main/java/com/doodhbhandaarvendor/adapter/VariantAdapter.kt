package com.doodhbhandaarvendor.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.VariantModel


class VariantAdapter(
    private var mContext: Context?,
    private var variants: ArrayList<VariantModel>,
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
        private val ibAdd= itemView.findViewById<ImageButton>(R.id.ib_add)
        private val ibRemove= itemView.findViewById<ImageButton>(R.id.ib_remove)
        private val tvQty= itemView.findViewById<TextView>(R.id.tv_qty)

        fun setData(variant: VariantModel, position: Int) {
            tvVariantName.text = variant.variantName
            tvQty.text = variant.qty.toString()


            ibAdd.setOnClickListener {
                mListener.onAddClick(position,it)
            }

            ibRemove.setOnClickListener {
                mListener.onSubtractClick(position,it)
            }
        }
    }
    interface OnItemClickListener {
        fun onAddClick(position: Int, view: View)
        fun onSubtractClick(position: Int, view: View)
    }

}






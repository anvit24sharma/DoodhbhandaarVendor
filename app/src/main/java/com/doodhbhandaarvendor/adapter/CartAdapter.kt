package com.doodhbhandaarvendor.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.ProductModel


class CartAdapter(
    private var mContext: Context?,
    private var product: ArrayList<ProductModel>,
    private var mListener: OnItemClickListener
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.setData(product[position],position)
    }

    override fun getItemCount(): Int {
        return product.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvProductName= itemView.findViewById<TextView>(R.id.tv_product_name)
        private val rvVariant = itemView.findViewById<RecyclerView>(R.id.rv_variants)
        lateinit var variantAdapter :VariantAdapter

        fun setData(productModel: ProductModel, position: Int) {
            tvProductName.text = productModel.product_name

            variantAdapter = productModel.let {
                VariantAdapter(mContext, it.variants, object : VariantAdapter.OnItemClickListener {
                    override fun onAddClick(position: Int, view: View) {

                    }
        })
    }
    rvVariant.apply {
        adapter = variantAdapter
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }
        }




    }
    interface OnItemClickListener {
        fun onAddClick(position: Int, view: View)
    }

}






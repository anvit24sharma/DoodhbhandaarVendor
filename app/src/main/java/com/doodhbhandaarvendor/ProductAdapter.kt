package com.doodhbhandaarvendor


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


@Suppress("UNCHECKED_CAST")
class ProductAdapter(
    private var mContext: Context?,
    private var product: ArrayList<ProductModel>,
    private var mListener: OnItemClickListener
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(product[position],position)

    }

    override fun getItemCount(): Int {
        return product.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val imgProduct= itemView.findViewById<ImageView>(R.id.img_product)
        private val tvProductName= itemView.findViewById<TextView>(R.id.tv_productName)
        private val tvProductDetails = itemView.findViewById<TextView>(R.id.tv_productDetails)
        private val tvPricePerKg = itemView.findViewById<TextView>(R.id.tv_PricePerKg)
        private val btnAdd=itemView.findViewById<Button>(R.id.btn_add)


        fun setData(productModel: ProductModel, position: Int) {
            tvProductName.text = productModel.product_name
            tvPricePerKg.text = productModel.product_cost
        }

        }
    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)

    }

}






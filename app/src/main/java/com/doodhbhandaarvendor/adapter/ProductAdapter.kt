package com.doodhbhandaarvendor.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.ProductModel


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

            if(productModel.selected)
                btnAdd.text = "Remove"
            else
                btnAdd.text = "Add to Cart"

            if(productModel.product_image_url !="") {
                Glide.with(mContext!!).load(productModel.product_image_url).centerCrop().into(imgProduct)
            }

            btnAdd.setOnClickListener {
                mListener.onAddClick(position,it)
            }
        }




        }
    interface OnItemClickListener {
        fun onAddClick(position: Int, view: View)
    }

}






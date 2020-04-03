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
import com.doodhbhandaarvendor.ui.CartActivity.Companion.totalOrderCost





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
        private val tvProductCost= itemView.findViewById<TextView>(R.id.tv_product_cost)
        private val rvVariant = itemView.findViewById<RecyclerView>(R.id.rv_variants)
        private lateinit var variantAdapter :VariantAdapter

        fun setData(productModel: ProductModel, position: Int) {
            tvProductName.text = productModel.product_name
            var totalCost =0.0
            productModel.variants.forEach {
                totalCost += productModel.product_cost.split("/")[0].toInt() * it.variantName.toDouble() * it.qty
            }
            tvProductCost.text = "₹" + totalCost.toString()
            totalOrderCost.value = totalOrderCost.value?.plus(totalCost)

            variantAdapter = productModel.let {
                VariantAdapter(mContext, it.variants, object : VariantAdapter.OnItemClickListener {
                    override fun onAddClick(position: Int, view: View) {
                        totalCost += it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble()
                        tvProductCost.text = "₹" + totalCost.toString()
                        totalOrderCost.value = totalOrderCost.value?.plus( it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble())
                        it.variants[position].qty += 1
                        variantAdapter.notifyDataSetChanged()
                    }

                    override fun onSubtractClick(position: Int, view: View) {
                        if( it.variants[position].qty != 0) {
                            it.variants[position].qty -= 1
                            variantAdapter.notifyDataSetChanged()
                            totalCost -= it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble()
                            tvProductCost.text = "₹" + totalCost.toString()
                            totalOrderCost.value = totalOrderCost.value?.minus( it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble())
                        }
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






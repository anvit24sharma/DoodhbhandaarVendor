package com.doodhbhandaarvendor.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        private val tvProductsQty= itemView.findViewById<TextView>(R.id.tv_totalQty)
        private val rvVariant = itemView.findViewById<RecyclerView>(R.id.rv_variants)
        private val btnCustom = itemView.findViewById<Button>(R.id.btn_custom)
        private val btnDaily = itemView.findViewById<Button>(R.id.btn_daily)
        private val btnWeekly = itemView.findViewById<Button>(R.id.btn_weekly)
        private lateinit var variantAdapter :VariantAdapter

        @SuppressLint("SetTextI18n")
        fun setData(productModel: ProductModel, position: Int) {
            tvProductName.text = productModel.product_name
            var totalCost =0.0
            val unit = ArrayList<String>()
            productModel.variants.forEach {
                totalCost += productModel.product_cost.split("/")[0].toInt() * it.variantName.toDouble() * it.qty
                unit.add(productModel.product_cost.split("/")[1])
            }

            tvProductCost.text = "₹$totalCost"
            tvProductsQty.text = (totalCost/productModel.product_cost.split("/")[0].toDouble()).toString()
            totalOrderCost.value = totalOrderCost.value?.plus(totalCost)

            variantAdapter = productModel.let {
                VariantAdapter(mContext,unit, it.variants, object : VariantAdapter.OnItemClickListener {
                    override fun onAddClick(position: Int, view: View) {
                        totalCost += it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble()
                        tvProductCost.text = "₹$totalCost"
                        tvProductsQty.text = (totalCost/productModel.product_cost.split("/")[0].toDouble()).toString()
                        totalOrderCost.value = totalOrderCost.value?.plus( it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble())
                        it.variants[position].qty += 1
                        variantAdapter.notifyDataSetChanged()
                    }

                    override fun onSubtractClick(position: Int, view: View) {
                        if( it.variants[position].qty != 0) {
                            it.variants[position].qty -= 1
                            variantAdapter.notifyDataSetChanged()
                            totalCost -= it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble()
                            tvProductCost.text = "₹$totalCost"
                            tvProductsQty.text = (totalCost/productModel.product_cost.split("/")[0].toDouble()).toString()
                            totalOrderCost.value = totalOrderCost.value?.minus( it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble())
                        }
                    }
                })
            }
            rvVariant.apply {
                adapter = variantAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }


            btnCustom.setOnClickListener {
                  mListener.onCustomClick(position,it,btnDaily, btnWeekly)
            }

            btnDaily.setOnClickListener {
                 mListener.onDailyClick(position,btnCustom,it, btnWeekly)

            }

            btnWeekly.setOnClickListener {
                  mListener.onWeeklyClick(position,btnCustom,btnDaily,it)

            }


        }
    }

    interface OnItemClickListener {
        fun onCustomClick(
            position: Int,
            view: View,
            btnDaily: Button,
            btnWeekly: Button
        )
        fun onWeeklyClick(
            position: Int,
            btnCustom: Button,
            btnDaily: Button,
            it: View
        )
        fun onDailyClick(
            position: Int,
            btnCustom: Button,
            it: View,
            btnWeekly: Button
        )

    }

}






package com.doodhbhandaarvendor.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.ProductModel
import com.doodhbhandaarvendor.ui.CartActivity.Companion.totalOrderCost
import java.util.*
import kotlin.collections.ArrayList


class CartAdapter(

    private var mContext: Context?,
    private var product: ArrayList<ProductModel>,
    private var mListener: OnItemClickListener
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart_layout, parent, false)
        return ViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(product[position], position)
    }

    override fun getItemCount(): Int {
        return product.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvProductName = itemView.findViewById<TextView>(R.id.tv_product_name)
        private val tvProductCost = itemView.findViewById<TextView>(R.id.tv_product_cost)
        private val tvProductsQty = itemView.findViewById<TextView>(R.id.tv_totalQty)
        private val rvVariant = itemView.findViewById<RecyclerView>(R.id.rv_variants)
        private val btnOnetime = itemView.findViewById<Button>(R.id.btn_onetime)
        private val btnDaily = itemView.findViewById<Button>(R.id.btn_daily)
        private val btnWeekly = itemView.findViewById<Button>(R.id.btn_weekly)
        private lateinit var variantAdapter: VariantAdapter

        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("SetTextI18n")
        fun setData(productModel: ProductModel, position: Int) {
            tvProductName.text = productModel.product_name
            var totalCost = 0.0
            val unit = ArrayList<String>()
            productModel.variants.forEach {
                totalCost += productModel.product_cost.split("/")[0].toInt() * it.variantName.toDouble() * it.qty
                unit.add(productModel.product_cost.split("/")[1])
            }

            tvProductCost.text = "Amount: ₹$totalCost"
            tvProductsQty.text =
                (totalCost / productModel.product_cost.split("/")[0].toDouble()).toString()
            totalOrderCost.value = totalOrderCost.value?.plus(totalCost)

            variantAdapter = productModel.let {
                VariantAdapter(mContext, unit, it.variants,
                    object : VariantAdapter.OnItemClickListener {
                        override fun onAddClick(position: Int, view: View) {
                            totalCost += it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble()
                            tvProductCost.text = "Amount: ₹$totalCost"
                            tvProductsQty.text = (totalCost / productModel.product_cost.split("/")[0].toDouble()).toString() + " "+ productModel.product_cost.split("/")[1]
                            totalOrderCost.value = totalOrderCost.value?.plus(it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble())
                            it.variants[position].qty += 1
                            variantAdapter.notifyDataSetChanged()
                        }

                        override fun onSubtractClick(position: Int, view: View) {
                            if (it.variants[position].qty != 0) {
                                it.variants[position].qty -= 1
                                variantAdapter.notifyDataSetChanged()
                                totalCost -= it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble()
                                tvProductCost.text = "Amount: ₹$totalCost"
                                tvProductsQty.text =
                                    (totalCost / productModel.product_cost.split("/")[0].toDouble()).toString() + " " + productModel.product_cost.split("/")[1]
                                totalOrderCost.value =
                                    totalOrderCost.value?.minus(it.product_cost.split("/")[0].toInt() * it.variants[position].variantName.toDouble())
                            }
                        }
                    })
            }
            rvVariant.apply {
                adapter = variantAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }


            btnOnetime.setOnClickListener {
//                cal = Calendar.getInstance()
//                var mY = cal.get(Calendar.YEAR)
//                var mM = cal.get(Calendar.MONTH)
//                var mD = cal.get(Calendar.DAY_OF_MONTH)
//
//                datePickerDialog = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//                    Toast.makeText(mContext, """$dayOfMonth/${month+ 1}/$year""", Toast.LENGTH_LONG).show()
//                }, mY, mM, mD)
//                     datePickerDialog.show()

                mListener.onOnetimeClick(position, it, btnDaily, btnWeekly)
            }

            btnDaily.setOnClickListener {
                mListener.onDailyClick(position, btnOnetime, it, btnWeekly)

            }

            btnWeekly.setOnClickListener {
                mListener.onWeeklyClick(position, btnOnetime, btnDaily, it)

            }


        }
    }

    interface OnItemClickListener {
        fun onOnetimeClick(position: Int, view: View, btnDaily: Button, btnWeekly: Button)
        fun onWeeklyClick(position: Int, btnOnetime: Button, btnDaily: Button, it: View)
        fun onDailyClick(position: Int, btnOntime: Button, it: View, btnWeekly: Button)
    }

}






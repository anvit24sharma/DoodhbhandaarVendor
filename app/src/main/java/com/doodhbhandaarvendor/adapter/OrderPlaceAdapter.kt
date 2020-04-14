package com.doodhbhandaarvendor.adapter



import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.model.ProductModel
import java.util.*


class OrderPlaceAdapter(
    private var mContext: Context?,
    private var product: ArrayList<ProductModel>,
    private var mListener: OnItemClickListener
) : RecyclerView.Adapter<OrderPlaceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_place_order, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(product[position], position)
    }

    override fun getItemCount(): Int {
        return product.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvProductName = itemView.findViewById<TextView>(R.id.tv_product_name)
        private val tvProductCostPerDay = itemView.findViewById<TextView>(R.id.tv_cost_perDay)
        private val tvProductsQty = itemView.findViewById<TextView>(R.id.tv_totalQty)
        private val tvMonthlyEstimatedCost = itemView.findViewById<TextView>(R.id.tv_estimatedCost)
        private val tvSelectedPlan = itemView.findViewById<TextView>(R.id.tv_selectedPlan)
        private val tvApplyPromocode = itemView.findViewById<TextView>(R.id.tv_applyPromoCode)
        private val tvPaymentDate = itemView.findViewById<TextView>(R.id.tv_payment_collectionDate)
        private val btnChoosePaymentDay = itemView.findViewById<Button>(R.id.btn_choose)

        @SuppressLint("SetTextI18n")
        fun setData(productModel: ProductModel, position: Int) {

            tvProductName.text = productModel.product_name
            var totalQty  =0.0

            productModel.variants.forEach {
                totalQty += it.variantName.toDouble() * it.qty
            }
            tvProductsQty.text = mContext?.getString(R.string.total_qty_s,totalQty.toString()+ " " +productModel.product_cost.split("/")[1])

            var costPerDay = (productModel.product_cost.split("/")[0].toDouble() * totalQty)
            tvProductCostPerDay.text = mContext?.getString(R.string.cost_per_day_s,costPerDay.toString())
            tvSelectedPlan.text=  mContext?.getString(R.string.plan_selected_s,productModel.subscriptionPlan)

            val cal = Calendar.getInstance()
            val currentMonth = cal.get(Calendar.MONTH)
            val todayDate = cal.get(Calendar.DAY_OF_MONTH)
            val lastDate = if(currentMonth == 0 || currentMonth == 2 || currentMonth == 4 ||currentMonth == 6 || currentMonth == 7 || currentMonth == 9 || currentMonth ==11)
                    31
                else if(currentMonth == 3 || currentMonth ==5 || currentMonth == 8 ||currentMonth == 10)
                    30
                else
                    28

            if(productModel.subscriptionPlan == "Weekly"){
                costPerDay *= 5
            }else  if(productModel.subscriptionPlan == "Daily"){
                //costPerDay *= ((lastDate - todayDate) + 1)
                costPerDay *= 30
            }
            tvMonthlyEstimatedCost.text = mContext?.getString(R.string.estimated_total_cost_of_month_s,costPerDay.toString())

            tvApplyPromocode.setOnClickListener {
                mListener.onApplyCouponClick(position,it)
            }

            btnChoosePaymentDay.setOnClickListener {
                mListener.OnChooseClick(position,it,tvPaymentDate)
            }

        }
    }
    interface OnItemClickListener {
        fun OnChooseClick(position: Int, it: View, tvPaymentDate: TextView)
        fun onApplyCouponClick(position: Int, it: View)
    }

}






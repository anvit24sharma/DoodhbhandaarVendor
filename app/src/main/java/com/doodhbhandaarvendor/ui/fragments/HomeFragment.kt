package com.doodhbhandaarvendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.R.string
import com.doodhbhandaarvendor.adapter.ProductAdapter
import com.doodhbhandaarvendor.model.ProductModel
import com.doodhbhandaarvendor.model.VariantModel
import com.doodhbhandaarvendor.ui.CartActivity
import com.doodhbhandaarvendor.utils.Constants
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*


class HomeFragment : Fragment() {

    lateinit var productAdapter: ProductAdapter

    companion object {
        val productList = ArrayList<ProductModel>()
        val cartProductList = ArrayList<ProductModel>()

        lateinit var productsDR : DatabaseReference
        lateinit var orderDR : DatabaseReference
        lateinit var userOrdersDR : DatabaseReference

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progress.visibility =VISIBLE
        productsDR = FirebaseDatabase.getInstance().getReference(Constants.PRODUCTS_TABLE)
        orderDR = FirebaseDatabase.getInstance().getReference(Constants.ORDER_TABLE)
        userOrdersDR =  FirebaseDatabase.getInstance().getReference(Constants.USERORDERS_TABLE)
        tv_count.text = "0"
        getProducts()
        initRecyclerView()
        initClicks()
    }

    private fun initClicks() {

        iv_cart.setOnClickListener{
            if(cartProductList.isEmpty()){
                Toast.makeText(context,"Cart is empty",Toast.LENGTH_SHORT).show()
            }else {
                startActivity(Intent(context, CartActivity::class.java))
            }
        }
    }

    private fun initRecyclerView() {
        productAdapter = productList.let {
            ProductAdapter(context, it, object : ProductAdapter.OnItemClickListener {
                override fun onAddClick(position: Int, view: View) {
                    var present : Boolean =false
                    val btn = view as Button

                    cartProductList.forEach {
                        if(productList[position].productId == it.productId)
                            present= true
                    }
                    if(!present) {
                        cartProductList.add(productList[position])
                        btn.text = getString(string.remove)
                    }
                    else {
                        cartProductList.remove(productList[position])
                      btn.text = getString(string.add)

                    }

                    tv_count.text = cartProductList.size.toString()

                }
            })
        }
        rv_product.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(context,2) as RecyclerView.LayoutManager?
        }
    }

    private fun getProducts() {

        productsDR.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,"Database error :" + p0.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapShot: DataSnapshot) {

                productList.clear()
                for (snap in snapShot.children){
                    val variantsList = ArrayList<VariantModel>()
                    snap.child("variants").children.forEach {
                        variantsList.add(VariantModel(it.child("available").value as Boolean,it.child("variantName").value.toString(),0))
                    }
                   val productModel = ProductModel(
                       snap.child("product_name").getValue().toString(),
                       snap.child("product_cost").getValue().toString(),
                       snap.child("product_image_url").getValue().toString(),
                       snap.key.toString(),
                       variantsList,"","",""
                   )
                        productList.add(productModel)
                }
                progress.visibility = GONE
                productAdapter.notifyDataSetChanged()
            }

        })
    }


}
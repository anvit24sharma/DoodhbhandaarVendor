package com.doodhbhandaarvendor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodhbhandaarvendor.LoginActivity.Companion.productsDR
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    lateinit var productAdapter: ProductAdapter
    val productList = ArrayList<ProductModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getProducts()
        productAdapter = productList.let {
            ProductAdapter(context, it, object : ProductAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, view: View) {

                }
            })
        }
        rv_product.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun getProducts() {

        productsDR.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,"Database error :" + p0.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapShot: DataSnapshot) {
                for (snap in snapShot.children){

                   val productModel = ProductModel(snap.child("product_name").toString(),
                       snap.child("product_cost").toString(),
                       snap.child("product_image_url").toString()
                   )
                        productList.add(productModel)
                }
                productAdapter.notifyDataSetChanged()
            }

        })
    }


}
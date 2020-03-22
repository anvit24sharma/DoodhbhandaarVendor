package com.doodhbhandaarvendor

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*


class HistoryFragment : Fragment() {

    lateinit var historyAdapter: HistoryAdapter
    val history = ArrayList<HistoryModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        historyAdapter = history.let {
            HistoryAdapter(context, it, object : HistoryAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, view: View) {

                }
            })
        }
        rv_product.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }




}

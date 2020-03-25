package com.doodhbhandaarvendor.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.adapter.ViewPagerAdapter
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment
import com.doodhbhandaarvendor.ui.fragments.HomeFragment
import com.doodhbhandaarvendor.ui.fragments.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    internal var prevMenuItem: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menu_bottom.get(0).isSelected =true
        menu_bottom.setOnItemSelectedListener {  
            when (it) {
                R.id.home -> viewpager.currentItem = 0
                R.id.history -> viewpager.currentItem = 1
                R.id.profile -> viewpager.currentItem = 2
            }
        }
        

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    menu_bottom.get(prevMenuItem!!).isSelected =false
                } else {
                    menu_bottom.get(0).isSelected =false
                }
                Log.d("page", "onPageSelected: $position")
                menu_bottom.get(position).isSelected =true
                prevMenuItem =  position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        setupViewPager(viewpager)

    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        val homeFragment = HomeFragment()
        adapter.addFragment(homeFragment)
        adapter.addFragment(HistoryFragment())
        adapter.addFragment(ProfileFragment())
        viewPager.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.view_cart -> {
                startActivity(Intent(this,CartActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

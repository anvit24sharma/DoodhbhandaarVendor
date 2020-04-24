package com.doodhbhandaarvendor.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.adapter.ViewPagerAdapter
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.prefs
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.usersDR
import com.doodhbhandaarvendor.model.TokenModel
import com.doodhbhandaarvendor.ui.fragments.HistoryFragment
import com.doodhbhandaarvendor.ui.fragments.HomeFragment
import com.doodhbhandaarvendor.ui.fragments.ProfileFragment
import com.doodhbhandaarvendor.utils.Constants.Companion.USER_ID
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        var BASE_URL = "https://fcm.googleapis.com/fcm/"
        var tokenList = ArrayList<String>()
        var adminDR = FirebaseDatabase.getInstance().getReference("admin")

    }
    internal var prevMenuItem: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        getAdminToken()
        menu_bottom[0].isSelected = true
        menu_bottom.setOnItemSelectedListener {
            when (it) {
                R.id.home -> viewpager.currentItem = 0
                R.id.history -> viewpager.currentItem = 1
                R.id.profile -> viewpager.currentItem = 2
            }
        }

        updateDeviceToken()


        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    menu_bottom.get(prevMenuItem!!).isSelected = false
                } else {
                    menu_bottom.get(0).isSelected = false
                }
                Log.d("page", "onPageSelected: $position")
                menu_bottom.get(position).isSelected = true
                prevMenuItem = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        setupViewPager(viewpager)

    }

    private fun getAdminToken() {
        adminDR.child("tokens").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    tokenList.add(it.value.toString())
                }
            }

        })
    }

    private fun updateDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult: InstanceIdResult ->
            val newToken = instanceIdResult.token
            usersDR.child(prefs.getString(USER_ID, "") ?: "").child("tokens").child(instanceIdResult.id).setValue(newToken) }
    }



    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        val homeFragment = HomeFragment()
        adapter.addFragment(homeFragment)
        adapter.addFragment(HistoryFragment())
        adapter.addFragment(ProfileFragment())
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
    }


}


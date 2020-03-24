package com.doodhbhandaarvendor.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.prefs
import com.doodhbhandaarvendor.ui.MainActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.activity_user_detail.tv_email
import kotlinx.android.synthetic.main.activity_user_detail.et_name

class UserDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        LoginActivity.prefs = getSharedPreferences("Db Vendor", Context.MODE_PRIVATE)

        tv_email.text = prefs.getString("email", "")

        btn_Save.setOnClickListener {
            validateDetails()
        }
    }

    private fun validateDetails() {
        if (et_name.text.toString().length < 3){
            Toast.makeText(this, "Name should be min. 3 Characters", Toast.LENGTH_SHORT).show()}
        else if (et_mobile_no.text.toString().length != 10){
            Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show()}
        else if (et_delivery_address.text.toString().isEmpty()){
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show()}
        else {
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()

        }
    }
}











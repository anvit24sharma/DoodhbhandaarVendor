package com.doodhbhandaarvendor.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.prefs
import com.doodhbhandaarvendor.model.UserModel
import com.doodhbhandaarvendor.ui.MainActivity
import com.doodhbhandaarvendor.utils.Constants
import com.doodhbhandaarvendor.utils.Constants.Companion.NAME
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {
    lateinit var user: FirebaseUser
    lateinit var userId :String
    lateinit var userEmail :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        prefs = getSharedPreferences("Db Vendor", Context.MODE_PRIVATE)

        user = FirebaseAuth.getInstance().currentUser!!
        userId= user.uid
        userEmail = user.email!!

        et_email.setText(userEmail)

        if(prefs.getString(NAME,"")!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        btn_Save.setOnClickListener {
            validateDetails()
        }
    }

    private fun validateDetails() {
        when {
            et_name.text.toString().length < 3 -> {
                Toast.makeText(this, "Name should be min. 3 Characters", Toast.LENGTH_SHORT).show()}
            et_mobile_no.text.toString().length != 10 -> {
                Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show()}
            et_delivery_address.text.toString().isEmpty() -> {
                Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show()}
            else -> {
                val userModel = UserModel(et_name.text.toString(),
                    et_mobile_no.text.toString(),
                    et_delivery_address.text.toString(),
                    userEmail,
                    userId)
                LoginActivity.usersDR.child(userId).setValue(userModel).addOnCompleteListener(
                    OnCompleteListener {
                        if (it.isSuccessful) {
                            val editor = prefs.edit()
                            editor.putString(Constants.NAME, userModel.name)
                            editor.putString(Constants.PHONE_NUMBER, userModel.phone_number)
                            editor.putString(Constants.EMAIL, userModel.email)
                            editor.putString(Constants.ADDRESS, userModel.address)
                            editor.putString(Constants.USER_ID, userModel.userId)
                            editor.apply()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    })
            }
        }
    }
}











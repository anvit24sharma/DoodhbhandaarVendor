package com.doodhbhandaarvendor.auth

import android.app.AlertDialog
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
import com.doodhbhandaarvendor.utils.Constants.Companion.ADDRESS
import com.doodhbhandaarvendor.utils.Constants.Companion.NAME
import com.doodhbhandaarvendor.utils.Constants.Companion.PHONE_NUMBER
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {
    lateinit var user: FirebaseUser
    lateinit var userId: String
    lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        prefs = getSharedPreferences("Db Vendor", Context.MODE_PRIVATE)

        val from = intent.getStringExtra("from")
        user = FirebaseAuth.getInstance().currentUser!!
        userId = user.uid
        userEmail = user.email!!

        et_email.setText(userEmail)

        if (prefs.getString(NAME, "") != null && prefs.getString(
                NAME,
                ""
            ) != "" && from != "ProfileFragment"
        ) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (from == "ProfileFragment") {
            et_mobile_no.setText(prefs.getString(PHONE_NUMBER, ""))
            et_name.setText(prefs.getString(NAME, ""))
            et_delivery_address.setText(prefs.getString(ADDRESS, ""))
        }

        btn_Save.setOnClickListener {
            validateDetails()
        }
        txt_logOut.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Logout")

            builder.setMessage("Are you want to logout the app?")
            builder.setPositiveButton("YES")
            { dialog, which ->

                FirebaseAuth.getInstance().signOut()
                this?.finish()
                startActivity(Intent(this, LoginActivity::class.java))
                prefs.edit().clear().apply()
            }
            builder.setNegativeButton("NO")
            { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun validateDetails() {
        when {
            et_name.text.toString().length < 3 -> {
                Toast.makeText(this, "Name should be min. 3 Characters", Toast.LENGTH_SHORT).show()
            }
            et_mobile_no.text.toString().length != 10 -> {
                Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show()
            }
            et_delivery_address.text.toString().isEmpty() -> {
                Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val userModel = UserModel(
                    et_name.text.toString(),
                    et_mobile_no.text.toString(),
                    et_delivery_address.text.toString(),
                    userEmail,
                    userId
                )
                LoginActivity.usersDR.child(userId).setValue(userModel).addOnCompleteListener(
                    OnCompleteListener {
                        if (it.isSuccessful) {
                            val editor = prefs.edit()
                            editor.putString(NAME, userModel.name)
                            editor.putString(PHONE_NUMBER, userModel.phone_number)
                            editor.putString(Constants.EMAIL, userModel.email)
                            editor.putString(ADDRESS, userModel.address)
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











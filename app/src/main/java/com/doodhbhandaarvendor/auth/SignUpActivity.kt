package com.doodhbhandaarvendor.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.prefs
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.usersDR
import com.doodhbhandaarvendor.model.UserModel
import com.doodhbhandaarvendor.ui.MainActivity
import com.doodhbhandaarvendor.utils.Constants.Companion.ADDRESS
import com.doodhbhandaarvendor.utils.Constants.Companion.EMAIL
import com.doodhbhandaarvendor.utils.Constants.Companion.NAME
import com.doodhbhandaarvendor.utils.Constants.Companion.PHONE_NUMBER
import com.doodhbhandaarvendor.utils.Constants.Companion.USER_ID
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        btn_SignUp.setOnClickListener {
            signUpUser()
        }
        txt_SignIn.setOnClickListener {
            signInUser()
        }

    }

    private fun signInUser() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun signUpUser() {
        if (et_email.text.toString().isEmpty()) {
            et_email.error = "Please enter email"
            et_email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches()) {
            et_email.error = "Please enter valid email"
            et_email.requestFocus()
            return
        }
        if (et_password.text.toString().isEmpty()) {
            et_password.error = "Please enter password"
            et_password.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(et_email.text.toString(), et_password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                            val userId = user?.uid
                    var newToken=""


                    val userModel = UserModel(et_name.text.toString(),
                                et_mobile_no.text.toString(),
                                et_delivery_address.text.toString(),
                                et_email.text.toString(),
                                userId!!)
                            usersDR.child(userId).setValue(user).addOnCompleteListener(
                                OnCompleteListener {
                                    if(it.isSuccessful){
                                        val editor = prefs.edit()
                                        editor.putString(NAME,userModel.name)
                                        editor.putString(PHONE_NUMBER,userModel.phone_number)
                                        editor.putString(EMAIL,userModel.email)
                                        editor.putString(ADDRESS,userModel.address)
                                        editor.putString(USER_ID,userModel.userId)
                                        editor.apply()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    }
                                })


                } else {
                    Toast.makeText(baseContext, "Sign Up Failed.", Toast.LENGTH_SHORT).show()
                }
            }


    }
}

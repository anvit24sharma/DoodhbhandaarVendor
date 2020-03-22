package com.doodhbhandaarvendor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar?.hide()

        btn_Next.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        startActivity(Intent(this,ResetPasswordActivity::class.java))
        finish()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

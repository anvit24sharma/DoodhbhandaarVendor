package com.doodhbhandaarvendor.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.doodhbhandaarvendor.R

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        supportActionBar?.hide()

    }
}

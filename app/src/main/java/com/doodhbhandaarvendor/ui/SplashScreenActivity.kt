package com.doodhbhandaarvendor.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.auth.LoginActivity


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        Handler().postDelayed({
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            },
            2000)
    }
}

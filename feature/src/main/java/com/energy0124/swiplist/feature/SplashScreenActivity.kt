package com.energy0124.swiplist.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            if (!isFinishing) {
                val sharedPref = this@SplashScreenActivity.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                val auth = sharedPref.getString(getString(R.string.authorization_key), "null")
                if (auth != "null") {
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }, 2000)
    }
}
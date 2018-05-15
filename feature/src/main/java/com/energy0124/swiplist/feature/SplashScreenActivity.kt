package com.energy0124.swiplist.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.energy0124.swiplist.feature.model.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = ""

        Handler().postDelayed({
            if (!isFinishing) {
                val sharedPref = this@SplashScreenActivity.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                val auth = sharedPref.getString(getString(R.string.authorization_key), "null")
                if (auth != "null") {
                    FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer $auth")
                    Fuel.get(getString(R.string.server_base_url) + "/api/users/me").response { request, response, result ->
                        when (result) {
                            is Result.Success -> {
                                if (response.statusCode == 200) {     // HTTP OK
                                    val responseBody = String(response.data)
                                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                    val jsonAdapter = moshi.adapter(User::class.java)
                                    (application as SwiplistApplication).user = jsonAdapter.fromJson(responseBody)

                                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            is Result.Failure -> {
                                val ex = result.getException()
                                Log.d("re-exception", ex.toString())
                                Toast.makeText(applicationContext, ex.toString(),
                                        Toast.LENGTH_LONG).show()
                                sharedPref.edit().clear().commit()
                                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                } else {
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)
    }
}
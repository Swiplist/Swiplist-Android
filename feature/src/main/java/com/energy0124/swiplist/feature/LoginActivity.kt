package com.energy0124.swiplist.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.energy0124.swiplist.feature.model.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.

        sign_in_button.setOnClickListener {
            var validInput = true
            val tUsername = username.text.toString()
            val tPassword = password.text.toString()
            if (tUsername.isEmpty()) {
                username.error = getString(R.string.error_field_required)
                validInput = false
            }
            if (tPassword.isEmpty()) {
                password.error = getString(R.string.error_field_required)
                validInput = false
            }
            if (validInput) {
                // TODO: send authentication request to server
                Fuel.post(getString(R.string.server_base_url)+"/api/auth/login",
                        listOf("username" to tUsername, "password" to tPassword))
                        .response { request, response, result ->
                            // FIXME: this is only skeleton, please check for valid response
                            Log.d("request", request.toString())
                            Log.d("response", response.toString())
                            when (result) {
                                is Result.Success -> {
                                    val res = result.get()
                                    Log.d("re-response", res.toString())
                                    if (response.statusCode == 200) {       // HTTP OK
                                        val responseBody = String(response.data)
                                        Log.d("response-body", responseBody)
                                        val authKey : String? = Json(responseBody).obj()["token"].toString()
                                        Log.d("auth-key", authKey)

                                        FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer $authKey")
                                        Fuel.get(getString(R.string.server_base_url) + "/api/users/me").response { meRequest, meResponse, meResult ->
                                            when (meResult) {
                                                is Result.Success -> {
                                                    if (meResponse.statusCode == 200) {     // HTTP OK
                                                        val meResponseBody = String(meResponse.data)
                                                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                                        val jsonAdapter = moshi.adapter(User::class.java)
                                                        (application as SwiplistApplication).user = jsonAdapter.fromJson(meResponseBody)

                                                        val sharedPref = this@LoginActivity.getSharedPreferences(
                                                                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                                                        with (sharedPref.edit()) {
                                                            putString(getString(R.string.authorization_key), authKey)
                                                            commit()
                                                        }

                                                        val intent = Intent(this, MainActivity::class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                                }
                                                is Result.Failure -> {
                                                    val ex = meResult.getException()
                                                    Log.d("re-exception", ex.toString())
                                                    Toast.makeText(applicationContext, ex.toString(),
                                                            Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        }
                                    }
                                }
                                is Result.Failure -> {
                                    val ex = result.getException()
                                    Log.d("re-exception", ex.toString())
                                    Toast.makeText(applicationContext, ex.toString(),
                                            Toast.LENGTH_LONG).show()
                                }
                            }
                        }
            }
        }

        forward_register_button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}

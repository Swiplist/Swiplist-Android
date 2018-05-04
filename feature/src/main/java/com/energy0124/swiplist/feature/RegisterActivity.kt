package com.energy0124.swiplist.feature

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // Set up the login form.

        register_button.setOnClickListener {
            var validInput = true
            val tEmail = register_email.text.toString()
            val tUsername = register_username.text.toString()
            val tPassword = register_password.text.toString()
            val tConfirm = register_confirm_password.text.toString()
            if (tEmail.isEmpty()) {
                register_email.error = getString(R.string.error_field_required)
                validInput = false
            }
            if (tUsername.isEmpty()) {
                register_username.error = getString(R.string.error_field_required)
                validInput = false
            }
            if (tPassword.isEmpty()) {
                register_password.error = getString(R.string.error_field_required)
                validInput = false
            }
            if (tConfirm.isEmpty()) {
                register_confirm_password.error = getString(R.string.error_field_required)
                validInput = false
            }
            if (validInput) {
                if (tConfirm != tPassword) {
                    register_confirm_password.error = getString(R.string.error_confirm_not_match)
                } else {
                    // TODO: send authorization request to server
                    Fuel.post(getString(R.string.server_base_url)+"/api/auth/register",
                            listOf("username" to tUsername,
                                    "password" to tPassword,
                                    "email" to tEmail))
                            .response { request, response, result ->
                                // FIXME: this is only skeleton, please check for valid response
                                Log.d("request", request.toString())
                                Log.d("response", response.toString())
                                when (result) {
                                    is Result.Success -> {
                                        val res = result.get()
                                        Log.d("re-response", res.toString())
                                        if (response.statusCode == 200) {       // HTTP OK
                                            // start login activity
                                            val intent = Intent(this,
                                                    LoginActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(applicationContext, "registration failed",
                                                    Toast.LENGTH_LONG).show()
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
        }
    }
}

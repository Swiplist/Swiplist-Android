package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.

//        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
//            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//
//                return@OnEditorActionListener true
//            }
//            false
//        })

        sign_in_button.setOnClickListener {
            var validInput = true
            val tUsername = username.text.toString()
            val tPassword = password.text.toString()
            if (tUsername.isEmpty()) {
                username.error = getString(R.string.error_invalid_username)
                validInput = false
            }
            if (tPassword.isEmpty()) {
                password.error = getString(R.string.error_invalid_password)
                validInput = false
            }
            if (validInput) {
                // TODO: send authentication request to server
                Fuel.post(getString(R.string.server_base_url)+"sth")
                        .body("{ \"username\" : \"$tUsername\"," +
                                " \"password\" : \"$tPassword\" }")
                        .response { request, response, result ->
                            Log.d("request", request.httpString())
                            Log.d("response", response.responseMessage)
                        }
            } else {

            }

        }
    }
}

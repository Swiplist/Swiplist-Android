package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // Set up the login form.

//        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
//            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//
//                return@OnEditorActionListener true
//            }
//            false
//        })

        register_button.setOnClickListener {
            var validInput = true
            val tEmail = register_email.text.toString()
            val tUsername = register_username.text.toString()
            val tPassword = register_password.text.toString()
            val tConfirm = register_confirm_password.text.toString()
            if (tEmail.isEmpty()) {
                username.error = getString(R.string.error_invalid_email)
                validInput = false
            }
            if (tUsername.isEmpty()) {
                username.error = getString(R.string.error_invalid_username)
                validInput = false
            }
            if (tPassword.isEmpty()) {
                username.error = getString(R.string.error_invalid_password)
                validInput = false
            }
            if (tConfirm.isEmpty()) {
                password.error = getString(R.string.error_invalid_password)
                validInput = false
            }
            if (validInput) {
                // TODO: send authentication request to server
                Fuel.post(getString(R.string.server_base_url)+"sth")
                        .body("{ \"username\" : \"$tUsername\"," +
                                " \"password\" : \"$tPassword\"," +
                                "\"email\" : \"$tEmail\" }")
                        .response { request, response, result ->
                            Log.d("request", request.httpString())
                            Log.d("response", response.responseMessage)
                        }
            } else {

            }

        }
    }
}

package com.energy0124.swiplist.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.energy0124.swiplist.feature.model.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi

import kotlinx.android.synthetic.main.activity_import_data.*

class ImportDataActivity : AppCompatActivity(), OnItemSelectedListener, View.OnClickListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                val importDataTag = findViewById<TextView>(R.id.import_data_tag)
                importDataTag.text = getString(R.string.import_data_user_id_tag)
                val inputFieldView = findViewById<TextInputEditText>(R.id.import_data_input_field)
                inputFieldView.setText("")
                currentService = services[position]
            }
            1 -> {
                val importDataTag = findViewById<TextView>(R.id.import_data_tag)
                importDataTag.text = getString(R.string.import_data_username_tag)
                val inputFieldView = findViewById<TextInputEditText>(R.id.import_data_input_field)
                inputFieldView.setText("")
                currentService = services[position]
            }
            2 -> {
                val importDataTag = findViewById<TextView>(R.id.import_data_tag)
                importDataTag.text = getString(R.string.import_data_username_tag)
                val inputFieldView = findViewById<TextInputEditText>(R.id.import_data_input_field)
                inputFieldView.setText("")
                currentService = services[position]
            }
            else -> {
            }
        }
    }

    override fun onClick(view: View) {
        val sharePref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val token = sharePref.getString(getString(R.string.authorization_key), "null")
        FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer $token")
        when (currentService) {
            "Steam-Game" -> {
                val userId = findViewById<TextInputEditText>(R.id.import_data_input_field).text
                Fuel.post(getString(R.string.server_base_url) + "/api/import/steam/games")
                        .body("{ \"steamId\" : \"$userId\" }")
                        .header("Content-Type" to "application/json")
                        .response { request, response, result ->
                            when (result) {
                                is Result.Success -> {
                                    if (response.statusCode == 200) {     // HTTP OK
                                        Fuel.get(getString(R.string.server_base_url) + "/api/users/me").response { request, response, result ->
                                            when (result) {
                                                is Result.Success -> {
                                                    if (response.statusCode == 200) {     // HTTP OK
                                                        val responseBody = String(response.data)
                                                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                                        val jsonAdapter = moshi.adapter(User::class.java)
                                                        (application as SwiplistApplication).user = jsonAdapter.fromJson(responseBody)
                                                        Snackbar.make(view, "Profile Refresh Success", Snackbar.LENGTH_LONG)
                                                                .setAction("Action", null).show()
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
                                        Snackbar.make(view, "Import Success, refreshing profile", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show()
                                    }
                                }
                                is Result.Failure -> {
                                    Snackbar.make(view, "Import Fail", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show()
                                }
                            }
                        }
            }
            "MyAnimeList-Anime" -> {
                val username = findViewById<TextInputEditText>(R.id.import_data_input_field).text
                Fuel.post(getString(R.string.server_base_url) + "/api/import/mal/anime")
                        .body("{ \"username\" : \"$username\" }")
                        .header("Content-Type" to "application/json")
                        .response { request, response, result ->
                            when (result) {
                                is Result.Success -> {
                                    if (response.statusCode == 200) {     // HTTP OK
                                        Fuel.get(getString(R.string.server_base_url) + "/api/users/me").response { request, response, result ->
                                            when (result) {
                                                is Result.Success -> {
                                                    if (response.statusCode == 200) {     // HTTP OK
                                                        val responseBody = String(response.data)
                                                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                                        val jsonAdapter = moshi.adapter(User::class.java)
                                                        (application as SwiplistApplication).user = jsonAdapter.fromJson(responseBody)
                                                        Snackbar.make(view, "Profile Refresh Success", Snackbar.LENGTH_LONG)
                                                                .setAction("Action", null).show()
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
                                        Snackbar.make(view, "Import Success, refreshing profile", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show()
                                    }
                                }
                                is Result.Failure -> {
                                    Snackbar.make(view, "Import Fail", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show()
                                }
                            }
                        }
            }
            "MyAnimeList-Manga" -> {
                val username = findViewById<TextInputEditText>(R.id.import_data_input_field).text
                Fuel.post(getString(R.string.server_base_url) + "/api/import/mal/manga")
                        .body("{ \"username\" : \"$username\" }")
                        .header("Content-Type" to "application/json")
                        .response { request, response, result ->
                            when (result) {
                                is Result.Success -> {
                                    if (response.statusCode == 200) {     // HTTP OK
                                        Fuel.get(getString(R.string.server_base_url) + "/api/users/me").response { request, response, result ->
                                            when (result) {
                                                is Result.Success -> {
                                                    if (response.statusCode == 200) {     // HTTP OK
                                                        val responseBody = String(response.data)
                                                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                                        val jsonAdapter = moshi.adapter(User::class.java)
                                                        (application as SwiplistApplication).user = jsonAdapter.fromJson(responseBody)
                                                        Snackbar.make(view, "Profile Refresh Success", Snackbar.LENGTH_LONG)
                                                                .setAction("Action", null).show()
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
                                        Snackbar.make(view, "Import Success, refreshing profile", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show()
                                    }
                                }
                                is Result.Failure -> {
                                    Snackbar.make(view, "Import Fail", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show()
                                }
                            }
                        }
            }
        }
    }

    private val services = arrayOf("Steam-Game", "MyAnimeList-Anime", "MyAnimeList-Manga")
    private lateinit var currentService: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_data)

        val spinner = findViewById<Spinner>(R.id.import_data_service_spinner)
        val spinnerAdapter = ArrayAdapter<String>(this, R.layout.spinner_item, services)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = this

        import_data_import_button.setOnClickListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}

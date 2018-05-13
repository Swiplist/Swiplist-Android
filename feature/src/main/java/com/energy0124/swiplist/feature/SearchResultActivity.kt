package com.energy0124.swiplist.feature

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.energy0124.swiplist.feature.model.Item
import com.energy0124.swiplist.feature.model.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.activity_search_result.*
import org.json.JSONArray

class SearchResultActivity : AppCompatActivity() {
    private var list: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharePref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val token = sharePref.getString(getString(R.string.authorization_key), "null")
        FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer $token")

        list = search_result_list
        val searchType = intent.getStringExtra("searchType")
        val query = intent.getStringExtra("query")
        when (searchType) {
            "item" -> {
                supportActionBar?.title = String.format(getString(R.string.search_item_title), query)

                val category = intent.getStringArrayListExtra("category")
                Log.d("category", JSONArray(category).toString())
                if (category != null) {
                    Fuel.post(getString(R.string.server_base_url) + "/api/items/search",
                            listOf("query" to query, "categories" to JSONArray(category)))
                            .response { request, response, result ->
                                when (result) {
                                    is Result.Success -> {
                                        if (response.statusCode == 200) {       // HTTP OK
                                            val responseBody = String(response.data)
                                            val itemArray = JSONArray(responseBody)
                                            Log.d("arraySize", itemArray.length().toString())
                                            if (itemArray.length() > 0) {       // has result
                                                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                                val type = Types.newParameterizedType(List::class.java, Item::class.java)
                                                val itemListAdapter: JsonAdapter<List<Item>> = moshi.adapter(type)
                                                val itemList = itemListAdapter.fromJson(responseBody)
                                                val adapter = ViewItemAdapter(this, itemList!!)
                                                list?.adapter = adapter
                                            } else {                            // has no result
                                                search_no_result_text.visibility = View.VISIBLE
                                                list?.visibility = View.GONE
                                            }

                                        } else {
                                            Log.e("searchResErr", response.responseMessage)
                                        }
                                    }
                                    is Result.Failure -> {
                                        Log.e("searchErr", result.error.toString())
                                    }
                                }
                            }
                }
            }
            "user" -> {
                supportActionBar?.title = String.format(getString(R.string.search_user_title), query)

                Fuel.post(getString(R.string.server_base_url) + "/api/users/search",
                        listOf("query" to query))
                        .response { request, response, result ->
                            when (result) {
                                is Result.Success -> {
                                    if (response.statusCode == 200) {     // HTTP OK
                                        val responseBody = String(response.data)
                                        val userArray = JSONArray(responseBody)
                                        Log.d("arraySize", userArray.length().toString())
                                        if (userArray.length() > 0) {       // has result
                                            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                            val type = Types.newParameterizedType(List::class.java, User::class.java)
                                            val userListAdapter: JsonAdapter<List<User>> = moshi.adapter(type)
                                            val userList = userListAdapter.fromJson(responseBody)
                                            val adapter = ViewUserAdapter(this, userList!!)
                                            list?.adapter = adapter
                                        } else {                            // has no result
                                            search_no_result_text.visibility = View.VISIBLE
                                            list?.visibility = View.GONE
                                        }
                                    } else {
                                        Log.d("searchResErr", response.responseMessage)
                                    }
                                }
                                is Result.Failure -> {
                                    Log.d("searchErr", result.error.toString())
                                }
                            }
                        }
            }
            else -> {
                Toast.makeText(this, "No searchType key exist", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}

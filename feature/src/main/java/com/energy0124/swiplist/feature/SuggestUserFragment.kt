package com.energy0124.swiplist.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.energy0124.swiplist.feature.model.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.picasso.Picasso

class SuggestUserFragment : Fragment() {
    private val userList: MutableList<User> = mutableListOf()
    private var card: CardView? = null
    private var nameText: TextView? = null
    private var imageButton: ImageButton? = null
    private var fabLeft: FloatingActionButton? = null
    private var fabRight: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_suggest_user, container, false)
        card = view.findViewById(R.id.suggest_user_card)
        nameText = view.findViewById(R.id.suggest_user_name)
        imageButton = view.findViewById(R.id.suggest_user_button)
        fabLeft = view.findViewById(R.id.suggest_user_interested)
        fabRight = view.findViewById(R.id.suggest_user_uninterested)

        val sharePref = context!!.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val token = sharePref.getString(getString(R.string.authorization_key), "null")
        FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer $token")
        fetchSuggestion()

        imageButton!!.setOnClickListener {
            Log.d("imageBtn", "Clicked")
            if (userList.isNotEmpty()) {
                val intent = Intent(context, ViewItemActivity::class.java)
                intent.putExtra("userId", userList[0].id)
                startActivity(intent)
            }
        }
        fabLeft!!.setOnClickListener {
            Log.d("fab", "Interested")
            if (userList.isNotEmpty()) {
                val user = userList[0]
                // FIXME: request path and body need change
                Fuel.post(getString(R.string.server_base_url) + "/api/users/like")
                        .body("{}")
                        .header("Content-Type" to "application/json")
                        .response { request, response, result ->
                            when (result) {
                                is Result.Success -> {
                                    if (response.statusCode == 200) {
                                        Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
                                        userList.removeAt(0)
                                        if (userList.isEmpty()) {
                                            fetchSuggestion()
                                        } else {
                                            fillCardInfo()
                                        }
                                    } else {
                                        Log.e("likeError", response.responseMessage)
                                    }
                                }
                                is Result.Failure -> {
                                    Log.e("likeError", result.error.toString())
                                }
                            }
                        }
            }
        }
        fabRight!!.setOnClickListener {
            Log.d("fab", "Not Interested")
            if (userList.isNotEmpty()) {
                val user = userList[0]
                // FIXME: request path and body need change
                Fuel.post(getString(R.string.server_base_url) + "/api/users/like")
                        .body("{}")
                        .header("Content-Type" to "application/json")
                        .response { request, response, result ->
                            when (result) {
                                is Result.Success -> {
                                    if (response.statusCode == 200) {
                                        Toast.makeText(context, "Disliked", Toast.LENGTH_SHORT).show()
                                        userList.removeAt(0)
                                        if (userList.isEmpty()) {
                                            fetchSuggestion()
                                        } else {
                                            fillCardInfo()
                                        }
                                    } else {
                                        Log.e("dislikeError", response.responseMessage)
                                    }
                                }
                                is Result.Failure -> {
                                    Log.e("dislikeError", result.error.toString())
                                }
                            }
                        }
            }
        }

        return view

    }

    private fun fetchSuggestion(renderToCard: Boolean = true) {
        // FIXME: request path and body need change
        Fuel.post(getString(R.string.server_base_url) + "/api/users/recommend")
                .body("{}")
                .header("Content-Type" to "application/json")
                .response { request, response, result ->
                    when (result) {
                        is Result.Success -> {
                            if (response.statusCode == 200) {
                                val responseBody = String(response.data)
                                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                val type = Types.newParameterizedType(List::class.java, User::class.java)
                                val userListAdapter: JsonAdapter<List<User>> = moshi.adapter(type)
                                userList.clear()
                                userList.addAll(userListAdapter.fromJson(responseBody)!!)
                                if (renderToCard) {
                                    fillCardInfo()
                                }
                            } else {
                                Log.e("searchResErr", response.responseMessage)
                            }
                        }
                        is Result.Failure -> {
                            Log.e("searchResErr", response.responseMessage)
                        }
                    }
                }
    }

    private fun fillCardInfo() {
        if (userList.isNotEmpty()) {
            val user = userList[0]
            if (user.iconUrl != null && user.iconUrl != "") {
                Picasso.with(context).load(user.iconUrl)
                        .fit()
                        .centerInside()
                        .into(imageButton)
            }
            nameText?.text = user.username
        } else {
            imageButton?.setImageDrawable(resources.getDrawable(R.drawable.ic_account_box_black_48dp))
            nameText?.text = getString(R.string.information_not_available)
        }
    }
}
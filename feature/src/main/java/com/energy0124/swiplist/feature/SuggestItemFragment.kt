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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.energy0124.swiplist.feature.model.Item
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.io.Serializable

class SuggestItemFragment : Fragment() {
    private val gameList: MutableList<Item> = mutableListOf()
    private val animeList: MutableList<Item> = mutableListOf()
    private val mangaList: MutableList<Item> = mutableListOf()
    private var card: CardView? = null
    private var imageButton: ImageButton? = null
    private var nameText: TextView? = null
    private var fabLeft: FloatingActionButton? = null
    private var fabRight: FloatingActionButton? = null
    private var currentCategory: String = "game"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_suggest_item, container, false)
        card = view.findViewById(R.id.suggest_user_card)
        nameText = view.findViewById(R.id.suggest_user_name)
        imageButton = view.findViewById(R.id.suggest_user_button)
        fabLeft = view.findViewById(R.id.suggest_user_interested)
        fabRight = view.findViewById(R.id.suggest_user_uninterested)

        // pre-fetch game items
        val sharePref = context!!.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val token = sharePref.getString(getString(R.string.authorization_key), "null")
        FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer $token")
        fetchSuggestion("game")
        fetchSuggestion("anime")
        fetchSuggestion("manga")
        fillCardInfo(gameList)
        fillCardInfo(animeList)
        fillCardInfo(mangaList)

        view.findViewById<RadioButton>(R.id.game_filter_button).isChecked = true
        currentCategory = "game"

        val radioGroup = view.findViewById<RadioGroup>(R.id.filter_button_group)
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            // TODO: when change filter, fetch suggest list from server, refresh card information
            when (i) {
                R.id.game_filter_button -> {
                    Log.d("button", "game")
                    currentCategory = "game"
                    if (gameList.count() < 5) {
                        fetchSuggestion("game")
                        fillCardInfo(gameList)
                    }
                }
                R.id.anime_filter_button -> {
                    Log.d("button", "anime")
                    currentCategory = "anime"
                    if (animeList.count() < 5) {
                        fetchSuggestion("anime")
                        fillCardInfo(animeList)
                    }
                }
                R.id.manga_filter_button -> {
                    Log.d("button", "manga")
                    currentCategory = "manga"
                    if (mangaList.count() < 5) {
                        fetchSuggestion("manga")
                        fillCardInfo(mangaList)
                    }
                }
            }

        }

        imageButton!!.setOnClickListener {
            // TODO: send intent to ViewItemActivity with item object as extra
            Log.d("imageBtn", "Clicked")
            val list = when (currentCategory) {
                "game" -> gameList
                "anime" -> animeList
                "manga" -> mangaList
                else -> gameList
            }
            if (list.isNotEmpty()) {
                val intent = Intent(context, ViewItemActivity::class.java)
                intent.putExtra("item", list[0] as Serializable)
                startActivity(intent)
            }
        }
        fabLeft!!.setOnClickListener {
            // TODO: refresh card information, send score to server
            Log.d("fab", "Interested")
        }
        fabRight!!.setOnClickListener {
            // TODO: refresh card information, send score to server
            Log.d("fab", "Not Interested")
        }

        return view
    }

    private fun fetchSuggestion(category: String) {
        val list = when (category) {
            "game" -> gameList
            "anime" -> animeList
            "manga" -> mangaList
            else -> gameList
        }
        Fuel.post(getString(R.string.server_base_url) + "/api/items/recommend",
                listOf("categories" to JSONArray(arrayOf(category))))
                .response { request, response, result ->
                    when (result) {
                        is Result.Success -> {
                            if (response.statusCode == 200) {       // HTTP OK
                                val responseBody = String(response.data)
                                val itemArray = JSONArray(responseBody)
                                if (itemArray.length() > 0) {
                                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                    val type = Types.newParameterizedType(List::class.java, Item::class.java)
                                    val itemListAdapter: JsonAdapter<List<Item>> = moshi.adapter(type)
                                    list.clear()
                                    list.addAll(itemListAdapter.fromJson(responseBody)!!)
                                } else {

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

    private fun fillCardInfo(list: MutableList<Item>) {
        if (list.count() > 0) {
            val item = list[0]
            for (i in list) {
                Log.d("name", i.name)
            }
            if (item.imageUrl != null)
            Picasso.with(context).load(item.imageUrl)
                    .fit()
                    .centerInside()
                    .into(imageButton)
            nameText?.text = item.name
        } else {

        }
    }
}
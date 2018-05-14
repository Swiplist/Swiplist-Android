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
import android.widget.*
import com.energy0124.swiplist.feature.R.drawable.ic_menu_gallery
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
        fetchSuggestion("anime", false)
        fetchSuggestion("manga", false)

        view.findViewById<RadioButton>(R.id.game_filter_button).isChecked = true
        currentCategory = "game"

        val radioGroup = view.findViewById<RadioGroup>(R.id.filter_button_group)
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            // when change filter, fetch suggest list from server, refresh card information
            when (i) {
                R.id.game_filter_button -> {
                    currentCategory = "game"
                    if (gameList.isEmpty()) {
                        fetchSuggestion("game")
                    } else {
                        fillCardInfo(gameList)
                    }
                }
                R.id.anime_filter_button -> {
                    currentCategory = "anime"
                    if (animeList.isEmpty()) {
                        fetchSuggestion("anime")
                    } else {
                        fillCardInfo(animeList)
                    }
                }
                R.id.manga_filter_button -> {
                    currentCategory = "manga"
                    if (mangaList.isEmpty()) {
                        fetchSuggestion("manga")
                    } else {
                        fillCardInfo(mangaList)
                    }
                }
            }

        }

        imageButton!!.setOnClickListener {
            // send intent to ViewItemActivity with item object as extra
            Log.d("imageBtn", "Clicked")
            val list = getList(currentCategory)
            if (list.isNotEmpty()) {
                val intent = Intent(context, ViewItemActivity::class.java)
                intent.putExtra("item", list[0] as Serializable)
                startActivity(intent)
            }
        }
        fabLeft!!.setOnClickListener {
            Log.d("fab", "Interested")
            val list = getList(currentCategory)
            if (list.isNotEmpty()) {
                val item = list[0]
                Fuel.post(getString(R.string.server_base_url) + "/api/users/like")
                        .body("{ \"categories\": [\"$currentCategory\"]," +
                                "\"item\": \"${item.id}\"," +
                                "\"like\": \"true\" }")
                        .header("Content-Type" to "application/json")
                        .response { request, response, result ->
                            when (result) {
                                is Result.Success -> {
                                    if (response.statusCode == 200) {
                                        Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
                                        val currentUser = (this.activity!!.application as SwiplistApplication).user
                                        when (currentCategory) {
                                            "game" -> currentUser!!.games.add(list.first())
                                            "anime" -> currentUser!!.anime.add(list.first())
                                            "manga" -> currentUser!!.manga.add(list.first())
                                        }
                                        list.removeAt(0)
                                        if (list.isEmpty()) {
                                            fetchSuggestion(currentCategory)
                                        } else {
                                            fillCardInfo(list)
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
            val list = getList(currentCategory)
            if (list.isNotEmpty()) {
                val item = list[0]
                Fuel.post(getString(R.string.server_base_url) + "/api/users/like")
                        .body("{ \"categories\": [\"$currentCategory\"]," +
                                "\"item\": \"${item.id}\"," +
                                "\"like\": \"false\" }")
                        .header("Content-Type" to "application/json")
                        .response { request, response, result ->
                            when (result) {
                                is Result.Success -> {
                                    if (response.statusCode == 200) {
                                        Toast.makeText(context, "Disliked", Toast.LENGTH_SHORT).show()
                                        list.removeAt(0)
                                        if (list.isEmpty()) {
                                            fetchSuggestion(currentCategory)
                                        } else {
                                            fillCardInfo(list)
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

    private fun fetchSuggestion(category: String, renderToCard: Boolean = true) {
        val list = getList(category)
        Fuel.post(getString(R.string.server_base_url) + "/api/items/recommend")
                .body("{ \"categories\": [\"$category\"] }")
                .header("Content-Type" to "application/json")
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
                                    if (renderToCard) {
                                        fillCardInfo(list)
                                    }
                                } else {
                                    Log.d("item", "no item")
                                    list.clear()
                                    if (renderToCard) {
                                        fillCardInfo(list)
                                    }
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
        if (list.isNotEmpty()) {
            val item = list[0]
            if (item.imageUrl != null && item.imageUrl != "") {
                Picasso.with(context).load(item.imageUrl)
                        .fit()
                        .centerInside()
                        .into(imageButton)
            }
            nameText?.text = item.name
        } else {
            imageButton?.setImageDrawable(resources.getDrawable(ic_menu_gallery))
            nameText?.text = getString(R.string.information_not_available)
        }
    }

    private fun getList(category: String): MutableList<Item> {
        when (category) {
            "game" -> return gameList
            "anime" -> return animeList
            "manga" -> return mangaList
            else -> return gameList
        }
    }
}
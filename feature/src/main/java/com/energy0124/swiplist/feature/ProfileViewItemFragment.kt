package com.energy0124.swiplist.feature

import android.content.Context
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.energy0124.swiplist.feature.model.Item
import com.github.kittinunf.forge.core.JSON
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONArray
import org.json.JSONObject

class ProfileViewItemFragment : ListFragment() {
    private var list: ListView? = null
    private var gameList: List<Item>? = null
    private var animeList: List<Item>? = null
    private var mangaList: List<Item>? = null
    private lateinit var noItemsTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view_item, container, false)
        list = view.findViewById(android.R.id.list)
        noItemsTextView = view.findViewById(R.id.profile_no_items_text)

        val sharePref = this.activity!!.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val userJson = sharePref.getString(getString(R.string.user_info_key), "null")
        val userObject = JSONObject(userJson)
        val gamesArray: JSONArray = userObject.optJSONArray("games")
        val animeArray: JSONArray = userObject.optJSONArray("anime")
        val mangaArray: JSONArray = userObject.optJSONArray("manga")

        if (gamesArray.length() > 0) {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val type = Types.newParameterizedType(List::class.java, Item::class.java)
            val itemListAdapter: JsonAdapter<List<Item>> = moshi.adapter(type)
            gameList = itemListAdapter.fromJson(gamesArray.toString())
        }
        if (animeArray.length() > 0) {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val type = Types.newParameterizedType(List::class.java, Item::class.java)
            val itemListAdapter: JsonAdapter<List<Item>> = moshi.adapter(type)
            animeList = itemListAdapter.fromJson(animeArray.toString())
        }
        if (mangaArray.length() > 0) {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val type = Types.newParameterizedType(List::class.java, Item::class.java)
            val itemListAdapter: JsonAdapter<List<Item>> = moshi.adapter(type)
            mangaList = itemListAdapter.fromJson(mangaArray.toString())
        }

        view.findViewById<RadioButton>(R.id.game_filter_button).isChecked = true

        if (null != gameList && gameList!!.count() > 0) {
            val presetAdapter = ViewItemAdapter(context!!, gameList!!)
            noItemsTextView.visibility = View.GONE
            list?.visibility = View.VISIBLE
            list?.adapter = presetAdapter
        } else {
            noItemsTextView.visibility = View.VISIBLE
            list?.visibility = View.GONE
        }

        val radioGroup = view.findViewById<RadioGroup>(R.id.filter_button_group)
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.game_filter_button -> {
                    //Log.d("button", "game")
                    if (null != gameList && gameList!!.count() > 0) {
                        val adapter = ViewItemAdapter(context!!, gameList!!)
                        noItemsTextView.visibility = View.GONE
                        list?.visibility = View.VISIBLE
                        list?.adapter = adapter
                    } else {
                        noItemsTextView.visibility = View.VISIBLE
                        list?.visibility = View.GONE
                    }
                }
                R.id.anime_filter_button -> {
                    //Log.d("button", "anime")
                    if (null != animeList && animeList!!.count() > 0) {
                        val adapter = ViewItemAdapter(context!!, animeList!!)
                        noItemsTextView.visibility = View.GONE
                        list?.visibility = View.VISIBLE
                        list?.adapter = adapter
                    } else {
                        noItemsTextView.visibility = View.VISIBLE
                        list?.visibility = View.GONE
                    }
                }
                R.id.manga_filter_button -> {
                    //Log.d("button", "manga")
                    if (null != mangaList && mangaList!!.count() > 0) {
                        val adapter = ViewItemAdapter(context!!, mangaList!!)
                        noItemsTextView.visibility = View.GONE
                        list?.visibility = View.VISIBLE
                        list?.adapter = adapter
                    } else {
                        noItemsTextView.visibility = View.VISIBLE
                        list?.visibility = View.GONE
                    }
                }
                else -> {
                    Log.d("button", "error")
                }
            }
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}

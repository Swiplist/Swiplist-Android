package com.energy0124.swiplist.feature

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.energy0124.swiplist.feature.model.Item
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.activity_ranking.*
import org.json.JSONArray

class RankingActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val categories = arrayOf("Game", "Anime", "Manga")
    private var list: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        list = ranking_listview

        val spinner = item_filter_spinner
        val spinnerAdaptor = ArrayAdapter<String>(this, R.layout.spinner_item, categories)
        spinnerAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdaptor
        spinner.onItemSelectedListener = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // TODO: change implementation of feeding data to listview, handle large data set
        val sharePref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val token = sharePref.getString(getString(R.string.authorization_key), "null")
        FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer $token")

        var category = ""
        when (position) {
            0 -> {
                Log.d("item", "game")
                category = "game"
                // val adapter = ViewItemAdapter(this, listOf(Item(name="game1"), Item(name="game2"), Item(name="game3"), Item(name="game4")))
                // list?.adapter = adapter
            }
            1 -> {
                Log.d("item", "anime")
                category = "anime"
                // val adapter = ViewItemAdapter(this, listOf(Item(name="anime1"), Item(name="anime2"), Item(name="anime3"), Item(name="anime4")))
                // list?.adapter = adapter
            }
            2 -> {
                Log.d("item", "manga")
                category = "manga"
                // val adapter = ViewItemAdapter(this, listOf(Item(name="manga1"), Item(name="manga2"), Item(name="manga3"), Item(name="manga4")))
                // list?.adapter = adapter
            }
        }
        Fuel.post(getString(R.string.server_base_url) + "/api/items/ranking",
                listOf("categories" to category))
                .response { request, response, result ->
                    when (result) {
                        is Result.Success -> {
                            if (response.statusCode == 200) {
                                val responseBody = String(response.data)
                                val itemArray = JSONArray(responseBody)
                                if (itemArray.length() > 0) {
                                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                    val type = Types.newParameterizedType(List::class.java, Item::class.java)
                                    val itemListAdapter: JsonAdapter<List<Item>> = moshi.adapter(type)
                                    val itemList = itemListAdapter.fromJson(responseBody)
                                    val adapter = ViewItemAdapter(this, itemList!!)
                                    list?.adapter = adapter
                                    list?.visibility = View.VISIBLE
                                    ranking_no_result_text.visibility = View.GONE
                                } else {
                                    list?.visibility = View.GONE
                                    ranking_no_result_text.visibility = View.VISIBLE
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

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("item", "nothing selected")
    }
}

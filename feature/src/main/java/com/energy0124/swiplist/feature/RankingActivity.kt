package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_ranking.*

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
        when (position) {
            0 -> {
                Log.d("item", "game")
                val adapter = ViewItemAdapter(this, arrayListOf("game1", "game2", "game3", "game4"))
                list?.adapter = adapter
            }
            1 -> {
                Log.d("item", "anime")
                val adapter = ViewItemAdapter(this, arrayListOf("anime1", "anime2", "anime3", "anime4"))
                list?.adapter = adapter
            }
            2 -> {
                Log.d("item", "manga")
                val adapter = ViewItemAdapter(this, arrayListOf("manga1", "manga2", "manga3", "manga4"))
                list?.adapter = adapter
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("item", "nothing selected")
    }
}

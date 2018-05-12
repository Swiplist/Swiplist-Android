package com.energy0124.swiplist.feature

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var mSearchView: SearchView
    private var isSearchingItem = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        val radioGroup = findViewById<RadioGroup>(R.id.search_filter_button_group)
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.search_item_filter_button -> {
                    Log.d("button", "item")
                    search_checkbox_container.visibility = View.VISIBLE
                    isSearchingItem = true
                }
                R.id.search_user_filter_button -> {
                    Log.d("button", "user")
                    search_checkbox_container.visibility = View.GONE
                    isSearchingItem = false
                }
                else -> {
                    Log.d("button", "error")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_app_bar_menu, menu)

        mSearchView = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        mSearchView.setIconifiedByDefault(false)
        mSearchView.queryHint = getString(R.string.search_hint)

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                var fullQuery = ""
                var searchType = ""
                val category = ArrayList<String>()

                // TODO: add search function on the query string
                if (isSearchingItem) {
                    var hasCategory = false
                    fullQuery += "item:"
                    searchType = "item"
                    if (game_option_checkbox.isChecked) {
                        fullQuery += " game"
                        category.add("game")
                        hasCategory = true
                    }
                    if (anime_option_checkbox.isChecked) {
                        fullQuery += " anime"
                        category.add("anime")
                        hasCategory = true
                    }
                    if (manga_option_checkbox.isChecked) {
                        fullQuery += " manga"
                        category.add("manga")
                        hasCategory = true
                    }
                    if (!hasCategory) {
                        Toast.makeText(applicationContext, "Please choose at least one category",
                                Toast.LENGTH_LONG).show()
                        return false
                    }
                } else {
                    fullQuery += "user:"
                    searchType = "user"
                }
                fullQuery += " $query"
                Toast.makeText(applicationContext, fullQuery, Toast.LENGTH_LONG).show()

                val intent = Intent(applicationContext, SearchResultActivity::class.java)
                intent.putExtra("searchType", searchType)
                intent.putExtra("query", query)
                if (isSearchingItem) {
                    intent.putExtra("category", category)
                }
                startActivity(intent)
                mSearchView.clearFocus()
                mSearchView.setQuery("", false)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}

package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast

class SearchActivity : AppCompatActivity() {

    private lateinit var mSearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                // TODO: add search function on the query string

                return false
            }

        })
        return true
    }
}

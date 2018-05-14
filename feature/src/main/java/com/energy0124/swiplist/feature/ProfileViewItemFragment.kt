package com.energy0124.swiplist.feature

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

class ProfileViewItemFragment : ListFragment() {
    private var list: ListView? = null
    private lateinit var noItemsTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view_item, container, false)
        list = view.findViewById(android.R.id.list)
        noItemsTextView = view.findViewById(R.id.profile_no_items_text)

        val gameList = (this.activity!!.application as SwiplistApplication).user!!.games
        val animeList = (this.activity!!.application as SwiplistApplication).user!!.anime
        val mangaList = (this.activity!!.application as SwiplistApplication).user!!.manga

        view.findViewById<RadioButton>(R.id.game_filter_button).isChecked = true

        if (gameList.count() > 0) {
            val presetAdapter = ViewItemAdapter(context!!, gameList)
            noItemsTextView.visibility = View.GONE
            list?.visibility = View.VISIBLE
            list?.adapter = presetAdapter
        } else {
            noItemsTextView.visibility = View.VISIBLE
            list?.visibility = View.GONE
        }

        val radioGroup = view.findViewById<RadioGroup>(R.id.filter_button_group)
        radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.game_filter_button -> {
                    //Log.d("button", "game")
                    if (gameList.count() > 0) {
                        val adapter = ViewItemAdapter(context!!, gameList)
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
                    if (animeList.count() > 0) {
                        val adapter = ViewItemAdapter(context!!, animeList)
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
                    if (mangaList.count() > 0) {
                        val adapter = ViewItemAdapter(context!!, mangaList)
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
}

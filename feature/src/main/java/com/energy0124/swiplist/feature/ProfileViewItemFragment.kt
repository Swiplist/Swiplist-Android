package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.RadioGroup

class ProfileViewItemFragment : ListFragment() {
    private var list: ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view_item, container, false)
        list = view.findViewById(android.R.id.list)

        val radioGroup = view.findViewById<RadioGroup>(R.id.filter_button_group)
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.game_filter_button -> {
                    Log.d("button", "game")
                    val adapter = ViewItemAdapter(context!!, arrayListOf("game1", "game2", "game3", "game4"))
                    list?.adapter = adapter
                }
                R.id.anime_filter_button -> {
                    Log.d("button", "anime")
                    val adapter = ViewItemAdapter(context!!, arrayListOf("anime1", "anime2", "anime3", "anime4"))
                    list?.adapter = adapter
                }
                R.id.manga_filter_button -> {
                    Log.d("button", "manga")
                    val adapter = ViewItemAdapter(context!!, arrayListOf("manga1", "manga2", "manga3", "manga4"))
                    list?.adapter = adapter
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

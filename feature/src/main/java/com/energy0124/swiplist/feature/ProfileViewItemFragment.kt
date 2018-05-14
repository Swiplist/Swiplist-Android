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
import com.energy0124.swiplist.feature.model.Item

class ProfileViewItemFragment : ListFragment() {
    private var list: ListView? = null
    private lateinit var noItemsTextView: TextView
    private var currentCategory: String = ""
    private var gameList: MutableList<Item> = mutableListOf()
    private var animeList: MutableList<Item> = mutableListOf()
    private var mangaList: MutableList<Item> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view_item, container, false)
        list = view.findViewById(android.R.id.list)
        noItemsTextView = view.findViewById(R.id.profile_no_items_text)

        view.findViewById<RadioButton>(R.id.game_filter_button).isChecked = true

        val radioGroup = view.findViewById<RadioGroup>(R.id.filter_button_group)
        radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.game_filter_button -> {
                    currentCategory = "game"
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
                    currentCategory = "anime"
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
                    currentCategory = "manga"
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

    override fun onResume() {
        super.onResume()
        val currentUserId = arguments!!.get("currentUserId")
        val currentUser = if ((this.activity!!.application as SwiplistApplication).user!!.id == currentUserId) {
            (this.activity!!.application as SwiplistApplication).user
        } else {
            (this.activity!!.application as SwiplistApplication).friend
        }
        gameList = currentUser!!.games
        animeList = currentUser.anime
        mangaList = currentUser.manga

        if ("" == currentCategory) {
            currentCategory = "game"
            if (gameList.count() > 0) {
                val presetAdapter = ViewItemAdapter(context!!, gameList)
                noItemsTextView.visibility = View.GONE
                list?.visibility = View.VISIBLE
                list?.adapter = presetAdapter
            } else {
                noItemsTextView.visibility = View.VISIBLE
                list?.visibility = View.GONE
            }
        } else {
            when (currentCategory) {
                "game" -> {
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
                "anime" -> {
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
                "manga" -> {
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
            }
        }
    }
}

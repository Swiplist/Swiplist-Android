package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.energy0124.swiplist.feature.model.User

class SuggestUserFragment : Fragment() {
    private val userList: MutableList<User> = mutableListOf()
    private var card: CardView? = null
    private var imageButton: ImageButton? = null
    private var fabLeft: FloatingActionButton? = null
    private var fabRight: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_suggest_user, container, false)
        card = view.findViewById(R.id.suggest_user_card)
        imageButton = view.findViewById(R.id.suggest_user_button)
        fabLeft = view.findViewById(R.id.suggest_user_interested)
        fabRight = view.findViewById(R.id.suggest_user_uninterested)

        imageButton!!.setOnClickListener {
            // TODO: send intent to ProfileActivity with user id as extra
            Log.d("imageBtn", "Clicked")
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
}
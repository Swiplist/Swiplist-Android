package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.energy0124.swiplist.feature.model.User


class ProfileViewFriendFragment : ListFragment() {
    private var list : ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view_friend, container, false)
        list = view.findViewById(android.R.id.list)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = ViewUserAdapter(context!!, listOf(User(username="user1"), User(username="user2")))
        listAdapter = adapter
    }
}

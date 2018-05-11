package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView


class ProfileViewFriendFragment : ListFragment() {
    private var list : ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view_friend, container, false)
        list = view.findViewById(android.R.id.list)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = ViewUserAdapter(context!!, arrayListOf("test1", "test2", "test3", "test4", "test5"))
        listAdapter = adapter
    }
}

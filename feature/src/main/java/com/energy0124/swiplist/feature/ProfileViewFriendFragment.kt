package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.energy0124.swiplist.feature.model.User


class ProfileViewFriendFragment : ListFragment() {
    private var list: ListView? = null
    private lateinit var noFriendsTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view_friend, container, false)
        list = view.findViewById(android.R.id.list)
        noFriendsTextView = view.findViewById(R.id.profile_no_friends_text)

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

        val friendList = currentUser!!.friends
        if (friendList.count() > 0) {
            val adapter = ViewUserAdapter(context!!, friendList)
            noFriendsTextView.visibility = View.GONE
            list?.visibility = View.VISIBLE
            list?.adapter = adapter
        } else {
            noFriendsTextView.visibility = View.VISIBLE
            list?.visibility = View.GONE
        }
    }
}

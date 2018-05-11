package com.energy0124.swiplist.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast


class ProfileViewFriendFragment : ListFragment() {
    private var list : ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view_friend, container, false)
        list = view.findViewById(android.R.id.list)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = CustomAdapter(context!!, arrayListOf("test1", "test2", "test3", "test4", "test5"))
        listAdapter = adapter
    }

    // FIXME: change data type to friend  or other reasonable class object
    inner class CustomAdapter(context: Context, data: ArrayList<String>) : BaseAdapter() {
        private val mContext = context
        private val friendList = data
        private val inflater = LayoutInflater.from(mContext)

        override fun getCount(): Int {
            return friendList.count()
        }

        override fun getItem(position: Int): Any {
            return friendList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view: View? = convertView
            if (view == null) {
                view = inflater.inflate(R.layout.view_friend_row, null)
            }

            val friendNameTextView: TextView = view!!.findViewById(R.id.view_friend_name)
            friendNameTextView.text = friendList[position]

            view.setOnClickListener {
                Toast.makeText(mContext, friendList[position], Toast.LENGTH_SHORT).show()
                val intent = Intent(mContext, ViewFriendActivity::class.java)
                // FIXME: send intent with friend's userID that can identify the user
                intent.putExtra("userId", friendList[position])
                startActivity(intent)
            }
            return view
        }
    }
}

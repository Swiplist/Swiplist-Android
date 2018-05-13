package com.energy0124.swiplist.feature

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.energy0124.swiplist.feature.model.User

class ViewUserAdapter(context: Context, data: List<User>) : BaseAdapter() {
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
            friendNameTextView.text = friendList[position].username

            view.setOnClickListener {
                Toast.makeText(mContext, friendList[position].username, Toast.LENGTH_SHORT).show()
                val intent = Intent(mContext, ViewFriendActivity::class.java)
                // FIXME: send intent with friend's userID that can identify the user
                intent.putExtra("userId", friendList[position].username)
                mContext.startActivity(intent)
            }
            return view
        }
}
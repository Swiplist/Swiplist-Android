package com.energy0124.swiplist.feature

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.energy0124.swiplist.feature.model.User
import com.squareup.picasso.Picasso

class ViewUserAdapter(context: Context, data: List<User>) : BaseAdapter() {
        private val mContext = context
        private val userList = data
        private val inflater = LayoutInflater.from(mContext)

        override fun getCount(): Int {
            return userList.count()
        }

        override fun getItem(position: Int): Any {
            return userList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view: View? = convertView
            val user = userList[position]
            if (view == null) {
                view = inflater.inflate(R.layout.view_user_row, null)
            }

            val userImageView: ImageView = view!!.findViewById(R.id.view_user_image)
            if (user.iconUrl != null) {
                Picasso.with(mContext).load(user.iconUrl)
                        .fit()
                        .centerCrop()
                        .into(userImageView)
            }

            val userNameTextView: TextView = view!!.findViewById(R.id.view_user_name)
            userNameTextView.text = user.username

            val userEmailTextView: TextView = view!!.findViewById(R.id.view_user_email)
            userEmailTextView.text = user.email

            view.setOnClickListener {
                Toast.makeText(mContext, user.username, Toast.LENGTH_SHORT).show()
                val intent = Intent(mContext, ProfileActivity::class.java)
                // FIXME: send intent with friend's userID that can identify the user
                intent.putExtra("userId", user.id)
                mContext.startActivity(intent)
            }
            return view
        }
}
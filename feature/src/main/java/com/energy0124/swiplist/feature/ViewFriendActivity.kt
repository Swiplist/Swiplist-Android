package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_view_friend.*

class ViewFriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_friend)
        fillUserInfo()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun fillUserInfo() {
        val userId: String? = intent.getStringExtra("userId")
        if (userId != null) {
            // TODO: query user info from server
            Log.d("userId", userId)
            friend_name_textview.text = userId
            friend_gender_textview.text = "??"
        } else {
            Log.d("userId", "cannot find user id")
        }
    }
}

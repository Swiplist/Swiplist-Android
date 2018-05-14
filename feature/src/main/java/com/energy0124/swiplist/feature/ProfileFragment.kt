package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.energy0124.swiplist.feature.model.User

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setUp(view)
        return view
    }

    private fun setUp(view: View) {
        val currentUserId = arguments!!.get("currentUserId") as String
        val currentUser = if ((this.activity!!.application as SwiplistApplication).user!!.id == currentUserId) {
            (this.activity!!.application as SwiplistApplication).user
        } else {
            (this.activity!!.application as SwiplistApplication).friend
        }
        if (null != currentUser!!.iconUrl) {
            // view.findViewById<ImageView>(R.id.profile_profile_image).setImageResource(R.drawable.ic_account_box_black_48dp)
        } else {
            view.findViewById<ImageView>(R.id.profile_profile_image).setImageResource(R.drawable.ic_account_box_black_48dp)
        }
        view.findViewById<TextView>(R.id.profile_username).text = currentUser.username
        view.findViewById<TextView>(R.id.profile_email).text = currentUser.email
        if ("" != currentUser.description) {
            view.findViewById<TextView>(R.id.profile_description).text = currentUser.description
        } else {
            view.findViewById<TextView>(R.id.profile_description).text = getString(R.string.profile_description)
        }
    }
}

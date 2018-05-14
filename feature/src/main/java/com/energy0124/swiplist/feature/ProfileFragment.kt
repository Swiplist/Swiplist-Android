package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setUp(view)
        return view
    }

    private fun setUp(view: View) {
        if ("" != (this.activity!!.application as SwiplistApplication).user!!.iconUrl) {
            // view.findViewById<ImageView>(R.id.profile_profile_image).setImageResource(R.drawable.ic_account_box_black_48dp)
        } else {
            view.findViewById<ImageView>(R.id.profile_profile_image).setImageResource(R.drawable.ic_account_box_black_48dp)
        }
        view.findViewById<TextView>(R.id.profile_username).text = (this.activity!!.application as SwiplistApplication).user!!.username
        view.findViewById<TextView>(R.id.profile_email).text = (this.activity!!.application as SwiplistApplication).user!!.email
        if ("" != (this.activity!!.application as SwiplistApplication).user!!.description) {
            view.findViewById<TextView>(R.id.profile_description).text = (this.activity!!.application as SwiplistApplication).user!!.description
        } else {
            view.findViewById<TextView>(R.id.profile_description).text = getString(R.string.profile_description)
        }
    }
}

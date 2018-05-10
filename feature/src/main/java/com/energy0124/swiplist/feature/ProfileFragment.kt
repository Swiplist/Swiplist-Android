package com.energy0124.swiplist.feature

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONArray
import org.json.JSONObject

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setUp(view)
        return view
    }

    private fun setUp(view: View) {
        val sharePref = this.activity!!.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val userObject = sharePref.getString(getString(R.string.user_info_key), "null")
        val username: String = JSONObject(userObject).getString("username")
        val email: String = JSONObject(userObject).getString("email")
        val iconUrl: String? = JSONObject(userObject).optString("iconUrl")
        val description: String? = JSONObject(userObject).optString("description")
        if ("" != iconUrl) {
            // view.findViewById<ImageView>(R.id.profile_profile_image).setImageResource(R.drawable.ic_account_box_black_48dp)
        } else {
            view.findViewById<ImageView>(R.id.profile_profile_image).setImageResource(R.drawable.ic_account_box_black_48dp)
        }
        view.findViewById<TextView>(R.id.profile_username).text = username
        view.findViewById<TextView>(R.id.profile_email).text = email
        if ("" != iconUrl) {
            view.findViewById<TextView>(R.id.profile_description).text = description
        } else {
            view.findViewById<TextView>(R.id.profile_description).text = getString(R.string.profile_description)
        }
    }


}

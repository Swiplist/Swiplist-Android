package com.energy0124.swiplist.feature

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.energy0124.swiplist.feature.model.MinifiedUser
import com.energy0124.swiplist.feature.model.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var currentUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // hide the button if not login user
        currentUserId = intent.extras.getString("userId")
        if (currentUserId != (application as SwiplistApplication).user!!.id) {
            add_friend_button.visibility = View.VISIBLE
            add_friend_button.setOnClickListener { view ->
                Fuel.post(getString(R.string.server_base_url) + "/api/users/add/friend")
                        .body("{ \"friend\": \"$currentUserId\" }")
                        .header("Content-Type" to "application/json")
                        .response { request, response, result ->
                            when (result) {
                                is Result.Success -> {
                                    if (response.statusCode == 200) {
                                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                        val jsonAdapter = moshi.adapter(MinifiedUser::class.java)
                                        val friendAdding = jsonAdapter.fromJson(String(response.data))
                                        (application as SwiplistApplication).user!!.friends.add(friendAdding!!)
                                        Snackbar.make(view, "Add Friend Success", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show()
                                    }
                                }
                                is Result.Failure -> {
                                    Toast.makeText(this, "Add Friend Fail", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
            }
            Fuel.get(getString(R.string.server_base_url) + "/api/users/" + currentUserId).response { request, response, result ->
                when (result) {
                    is Result.Success -> {
                        if (response.statusCode == 200) {
                            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                            val jsonAdapter = moshi.adapter(User::class.java)
                            (application as SwiplistApplication).friend = jsonAdapter.fromJson(String(response.data))

                            // Create the adapter that will return a fragment for each of the three
                            // primary sections of the activity.
                            mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

                            // Set up the ViewPager with the sections adapter.
                            container.adapter = mSectionsPagerAdapter
                            configureTabLayout(container)
                        }
                    }
                    is Result.Failure -> {
                        Toast.makeText(this, "Cannot access this user",
                                Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        } else {
            add_friend_button.visibility = View.GONE

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

            // Set up the ViewPager with the sections adapter.
            container.adapter = mSectionsPagerAdapter
            configureTabLayout(container)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentUserId == (application as SwiplistApplication).user!!.id) {
            menuInflater.inflate(R.menu.menu_profile, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.profile_action_edit -> {
                //Toast.makeText(applicationContext, "Edit", Toast.LENGTH_SHORT).show()
                val currentPosition = container.currentItem
                when (currentPosition) {
                    0 -> {
                        // Edit Profile
                    }
                    1 -> {
                        // Edit Item List
                        val gamesArray = (application as SwiplistApplication).user!!.games
                        val animeArray = (application as SwiplistApplication).user!!.anime
                        val mangaArray = (application as SwiplistApplication).user!!.manga

                        var rbGame: RadioButton? = null
                        var rbAnime: RadioButton? = null
                        var rbManga: RadioButton? = null
                        val frags = this.supportFragmentManager.fragments
                        for (f in frags) {
                            if ("com.energy0124.swiplist.feature.ProfileViewItemFragment" == f.javaClass.name) {
                                rbGame = f.view!!.findViewById(R.id.game_filter_button)
                                rbAnime = f.view!!.findViewById(R.id.anime_filter_button)
                                rbManga = f.view!!.findViewById(R.id.manga_filter_button)
                            }
                        }
                        val intent = Intent(this, ProfileEditItemActivity::class.java)
                        //Log.d("radio button", "game")
                        //Log.d("radio button", "anime")
                        //Log.d("radio button", "manga")
                        when {
                            rbGame!!.isChecked -> {
                                if (gamesArray.count() > 0) {
                                    intent.putExtra("category", "game")
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "No games can be edited", Toast.LENGTH_SHORT).show()
                                }
                            }
                            rbAnime!!.isChecked -> {
                                if (animeArray.count() > 0) {
                                    intent.putExtra("category", "anime")
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "No anime can be edited", Toast.LENGTH_SHORT).show()
                                }
                            }
                            rbManga!!.isChecked -> {
                                if (mangaArray.count() > 0) {
                                    intent.putExtra("category", "manga")
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "No manga can be edited", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    2 -> {
                        // Edit Friend List
                        val intent = Intent(this, ProfileEditFriendActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {

                    }
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun configureTabLayout(container: ViewPager) {
        tabs.addTab(tabs.newTab().setText("Profile"))
        tabs.addTab(tabs.newTab().setText("Item"))
        tabs.addTab(tabs.newTab().setText("Friend"))
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                container.currentItem = tab.position
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
        })
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putString("currentUserId", currentUserId)
            when (position) {
                0 -> {
                    val fragObj = ProfileFragment()
                    fragObj.arguments = bundle
                    return fragObj
                }
                1 -> {
                    val fragObj = ProfileViewItemFragment()
                    fragObj.arguments = bundle
                    return fragObj
                }
                2 -> {
                    val fragObj = ProfileViewFriendFragment()
                    fragObj.arguments = bundle
                    return fragObj
                }
                else -> {
                    val fragObj = ProfileFragment()
                    fragObj.arguments = bundle
                    return fragObj
                }
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }
}

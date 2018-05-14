package com.energy0124.swiplist.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //    private lateinit var mSectionsPageAdapter: SectionsPageAdapter
    private lateinit var mViewPager: ViewPager
    private var userMap = mapOf<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        userSetUp()

        val tabLayout = main_tabs
        mViewPager = main_viewpager
        setupViewPager(mViewPager)
        tabLayout.setupWithViewPager(mViewPager)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val navHeader = nav_view.getHeaderView(0)

        val navHeaderProfileButton = navHeader.findViewById<ImageButton>(R.id.nav_profile_picture)
        navHeaderProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPageAdapter(supportFragmentManager)
        adapter.addFragment(OverviewFragment(), getString(R.string.tab_overview))
        adapter.addFragment(ViewItemFragment(), getString(R.string.tab_view_items))
        adapter.addFragment(ViewFriendFragment(), getString(R.string.tab_view_friends))
        viewPager.adapter = adapter
    }

    private fun userSetUp() {
        val sharePref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val token = sharePref.getString(getString(R.string.authorization_key), "null")
        //Log.d("token", token)
        FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer $token")
        Fuel.get(getString(R.string.server_base_url) + "/api/users/me").response { request, response, result ->
            //make request to https://httpbin.org/get because Fuel.{get|post|put|delete} use FuelManager.instance to make HTTP request
            //Log.d("response", response.toString())
            //Log.d("result", result.toString())
            when (result) {
                is Result.Success -> {
                    if (response.statusCode == 200) {     // HTTP OK
                        val responseBody = String(response.data)
                        val username: String = JSONObject(responseBody).getString("username")
                        val email: String = JSONObject(responseBody).getString("email")
                        val items: JSONArray? = JSONObject(responseBody).optJSONArray("items")
                        val friends: JSONArray? = JSONObject(responseBody).optJSONArray("friends")
                        val games: JSONArray? = JSONObject(responseBody).optJSONArray("games")
                        val anime: JSONArray? = JSONObject(responseBody).optJSONArray("anime")
                        val manga: JSONArray? = JSONObject(responseBody).optJSONArray("manga")
                        val iconUrl: String? = JSONObject(responseBody).optString("iconUrl")
                        val description: String? = JSONObject(responseBody).optString("description")
                        val mobileNumber: String? = JSONObject(responseBody).optString("mobileNumber")
                        /*userMap = mapOf("username" to username,
                                "email" to email,
                                "items" to items,
                                "friends" to friends,
                                "iconUrl" to iconUrl)
                        Log.d("map", userMap.toString())*/
                        val userInfoJson = JSONObject()
                        userInfoJson.put("username", username)
                        userInfoJson.put("email", email)
                        userInfoJson.put("items", items)
                        userInfoJson.put("friends", friends)
                        userInfoJson.put("games", games)
                        userInfoJson.put("anime", anime)
                        userInfoJson.put("manga", manga)
                        userInfoJson.put("iconUrl", iconUrl)
                        userInfoJson.put("description", description)
                        userInfoJson.put("mobileNumber", mobileNumber)
                        //Log.d("jsonObj", userInfoJson.toString())
                        with(sharePref.edit()) {
                            putString(getString(R.string.user_info_key), userInfoJson.toString())
                            commit()
                        }

                        // set the user info in the navigation bar
                        val navHeader = nav_view.getHeaderView(0)
                        val navHeaderUserTextView = navHeader.findViewById<TextView>(R.id.nav_username)
                        navHeaderUserTextView.text = username
                        //Log.d("text", navHeaderUserTextView.text.toString())
                        val navHeaderProfileButton = navHeader.findViewById<ImageButton>(R.id.nav_profile_picture)
                        if ("" != iconUrl) {
                            //  navHeaderProfileButton.setImageResource(R.drawable.ic_exit_to_app_black_18dp)
                        } else {
                            navHeaderProfileButton.setImageResource(R.drawable.ic_account_box_black_48dp)
                        }

                        // val user = sharePref.getString(getString(R.string.user_info_key), "null")
                        // Log.d("user", user)
                    }
                }
                is Result.Failure -> {
                    val ex = result.getException()
                    Log.d("re-exception", ex.toString())
                    Toast.makeText(applicationContext, ex.toString(),
                            Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(applicationContext, "Settings", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_ranking -> {
                Toast.makeText(applicationContext, "Ranking", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RankingActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_search -> {
                Toast.makeText(applicationContext, "Search", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {

            }
            R.id.nav_import_data -> {
                val intent = Intent(this, ImportDataActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_setting -> {

            }
            R.id.nav_logout -> {
                val sharePref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                sharePref.edit().clear().commit()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}

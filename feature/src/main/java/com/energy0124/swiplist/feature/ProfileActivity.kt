package com.energy0124.swiplist.feature

import android.content.Intent
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
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_profile_view_item.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        configureTabLayout(container)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_profile, menu)
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
                when(currentPosition){
                    0 -> {
                        // Edit Profile
                    }
                    1 -> {
                        // Edit Item List
                        var rbGame: RadioButton? = null
                        var rbAnime: RadioButton? = null
                        var rbManga: RadioButton? = null
                        val frags = this.supportFragmentManager.fragments
                        for(f in frags){
                            if("com.energy0124.swiplist.feature.ProfileViewItemFragment" == f.javaClass.name){
                                rbGame = f.view!!.findViewById<RadioButton>(R.id.game_filter_button)
                                rbAnime = f.view!!.findViewById<RadioButton>(R.id.anime_filter_button)
                                rbManga = f.view!!.findViewById<RadioButton>(R.id.manga_filter_button)
                            }
                        }
                        val intent = Intent(this, ProfileEditItemActivity::class.java)
                        //Log.d("radio button", "game")
                        //Log.d("radio button", "anime")
                        //Log.d("radio button", "manga")
                        when {
                            rbGame!!.isChecked -> intent.putExtra("category", "game")
                            rbAnime!!.isChecked -> intent.putExtra("category", "anime")
                            rbManga!!.isChecked -> intent.putExtra("category", "manga")
                        }
                        startActivity(intent)
                    }
                    2 -> {
                        // Edit Friend List
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
            return when (position) {
                0 -> ProfileFragment()
                1 -> ProfileViewItemFragment()
                2 -> ProfileViewFriendFragment()
                else -> ProfileFragment()
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

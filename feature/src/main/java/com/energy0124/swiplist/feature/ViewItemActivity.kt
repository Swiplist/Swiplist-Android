package com.energy0124.swiplist.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.MenuItem
import com.energy0124.swiplist.feature.model.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_item.*

class ViewItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)
        fillItemInfo()
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

    private fun fillItemInfo() {
        val item: Item? = intent.getSerializableExtra("item") as Item
        if (item != null) {
            if (item.imageUrl != null) {
                val metrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(metrics)
                Picasso.with(this).load(item.imageUrl)
                        .resize(metrics.heightPixels, metrics.widthPixels)
                        .centerInside()
                        .into(item_detail_image)
            }
            item_detail_name_text.text = item.name
            item_detail_category_text.text = item.category
            if (item.description != null) {
                item_detail_description_text.text = item.description
            }
            if (item.src != null) {
                item_detail_source_text.text = item.src
            }
        }
    }
}

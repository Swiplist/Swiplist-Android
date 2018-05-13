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
import com.energy0124.swiplist.feature.model.Item
import com.squareup.picasso.Picasso
import java.io.Serializable

class ViewItemAdapter(context: Context, data: List<Item>) : BaseAdapter(){
    private val mContext = context
    private val itemList = data
    private val inflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
        return itemList.count()
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        val item = itemList[position]
        if (view == null) {
            view = inflater.inflate(R.layout.view_item_row, null)
        }

        val itemRankingTextView: TextView = view!!.findViewById(R.id.view_item_ranking)
        itemRankingTextView.text = (position + 1).toString()

        val itemImageView: ImageView = view!!.findViewById(R.id.view_item_image)
        if (item.imageUrl != null) {
            Picasso.with(mContext).load(item.imageUrl)
                    .fit()
                    .centerCrop()
                    .into(itemImageView)
        }

        val itemNameTextView: TextView = view!!.findViewById(R.id.view_item_name)
        itemNameTextView.text = item.name

        val itemCategoryTextView: TextView = view!!.findViewById(R.id.view_item_category)
        itemCategoryTextView.text = item.category

        view.setOnClickListener {
            Toast.makeText(mContext, itemList[position].name, Toast.LENGTH_SHORT).show()
            val intent = Intent(mContext, ViewItemActivity::class.java)
            intent.putExtra("item", itemList[position] as Serializable)
            mContext.startActivity(intent)
        }
        return view
    }

}
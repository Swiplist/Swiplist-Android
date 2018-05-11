package com.energy0124.swiplist.feature

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast

class ViewItemAdapter(context: Context, data: ArrayList<String>) : BaseAdapter(){
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
        if (view == null) {
            view = inflater.inflate(R.layout.view_item_row, null)
        }

        // TODO: change other detail info and onClickListener
        val itemRankingTextView: TextView = view!!.findViewById(R.id.view_item_ranking)
        itemRankingTextView.text = (position + 1).toString()

        val itemTitleTextView: TextView = view!!.findViewById(R.id.view_item_title)
        itemTitleTextView.text = itemList[position]

        view.setOnClickListener {
            Toast.makeText(mContext, itemList[position], Toast.LENGTH_SHORT).show()
        }
        return view
    }

}
package com.energy0124.swiplist.feature

import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.yydcdut.sdlv.SlideAndDragListView

class ProfileEditItemActivity : AppCompatActivity(), AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener,
        SlideAndDragListView.OnDragDropListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {

    //private val TAG = ProfileEditItemActivity::class.java.simpleName

    private lateinit var mMenu: com.yydcdut.sdlv.Menu
    //private lateinit var mAppList: MutableList<ApplicationInfo>
    private lateinit var mAppList: ArrayList<String>
    private lateinit var mListView: SlideAndDragListView
    //private lateinit var mToast: Toast
    private lateinit var mDraggedEntity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit_item)
        initData()
        initMenu()
        initUiAndListener()
        //mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_profile_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initData() {
        val category = intent.extras.getString("category")
        when (category) {
            "game" -> {
                mAppList = arrayListOf("game1", "game2", "game3", "game4")
            }
            "anime" -> {
                mAppList = arrayListOf("anime1", "anime2", "anime3", "anime4")
            }
            "manga" -> {
                mAppList = arrayListOf("manga1", "manga2", "manga3", "manga4")
            }
        }
        //mAppList = packageManager.getInstalledApplications(0)
    }

    private fun initMenu() {
        mMenu = com.yydcdut.sdlv.Menu(true)
        mMenu.addItem(com.yydcdut.sdlv.MenuItem.Builder().setWidth(resources.getDimension(R.dimen.profile_list_cell_delete_btn_width).toInt() * 2)
                .setBackground(getDrawable(R.drawable.profile_item_delete_btn_width))
                .setText("Delete")
                .setDirection(com.yydcdut.sdlv.MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.BLACK)
                .setTextSize(14)
                .build())
    }

    private fun initUiAndListener() {
        mListView = findViewById(R.id.profile_edit_item)
        mListView.setMenu(mMenu)
        mListView.adapter = mAdapter
        mListView.onItemClickListener = this
        mListView.setOnDragDropListener(this)
//        mListView.setOnItemLongClickListener(this);
        mListView.setOnSlideListener(this)
        mListView.setOnMenuItemClickListener(this)
        mListView.setOnItemDeleteListener(this)
        mListView.setOnScrollListener(this)
    }

    private val mAdapter = object : BaseAdapter() {

        override fun getCount(): Int {
            return mAppList.size
        }

        override fun getItem(position: Int): Any {
            return mAppList[position]
        }

        override fun getItemId(position: Int): Long {
            return mAppList[position].hashCode().toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var listCellView = convertView
            val cvh: CustomViewHolder
            if (listCellView == null) {
                cvh = CustomViewHolder()
                listCellView = LayoutInflater.from(this@ProfileEditItemActivity).inflate(R.layout.item_custom_btn, null)
                cvh.imgLogo = listCellView.findViewById(R.id.img_item_edit) as ImageView
                cvh.txtName = listCellView.findViewById(R.id.txt_item_edit) as TextView
                cvh.imgLogo2 = listCellView.findViewById(R.id.img_item_edit2) as ImageView
                cvh.imgLogo2!!.setOnTouchListener(mOnTouchListener)
                cvh.number = listCellView.findViewById(R.id.number_item_edit) as TextView
                listCellView.tag = cvh
            } else {
                cvh = listCellView.tag as CustomViewHolder
            }
            cvh.txtName!!.text = mAppList[position]
            cvh.imgLogo!!.setImageDrawable(getDrawable(R.drawable.ic_account_box_black_48dp))
            cvh.imgLogo2!!.setImageDrawable(getDrawable(R.drawable.ic_reorder_grey_500_24dp))
            cvh.imgLogo2!!.tag = Integer.parseInt(position.toString())
            cvh.number!!.text = (position + 1).toString()
            return listCellView!!
        }

        internal inner class CustomViewHolder {
            var imgLogo: ImageView? = null
            var txtName: TextView? = null
            var imgLogo2: ImageView? = null
            var number: TextView? = null
        }

        private val mOnTouchListener = View.OnTouchListener { v, _ ->
            val o = v.tag
            if (o != null && o is Int) {
                mListView.startDrag(o.toInt())
            }
            false
        }
    }

    override fun onDragViewStart(beginPosition: Int) {
        mDraggedEntity = mAppList[beginPosition]
        //toast("onDragViewStart   beginPosition--->$beginPosition")
    }

    override fun onDragDropViewMoved(fromPosition: Int, toPosition: Int) {
        val applicationInfo = mAppList.removeAt(fromPosition)
        mAppList.add(toPosition, applicationInfo)
        //toast("onDragDropViewMoved   fromPosition--->$fromPosition  toPosition-->$toPosition")
    }

    override fun onDragViewDown(finalPosition: Int) {
        mAppList.set(finalPosition, mDraggedEntity)
        //toast("onDragViewDown   finalPosition--->$finalPosition")
    }

    override fun onSlideOpen(view: View, parentView: View, position: Int, direction: Int) {
        //toast("onSlideOpen   position--->$position  direction--->$direction")
    }

    override fun onSlideClose(view: View, parentView: View, position: Int, direction: Int) {
        //toast("onSlideClose   position--->$position  direction--->$direction")
    }

    override fun onMenuItemClick(v: View, itemPosition: Int, buttonPosition: Int, direction: Int): Int {
        //toast("onMenuItemClick   itemPosition--->$itemPosition  buttonPosition-->$buttonPosition  direction-->$direction")
        return com.yydcdut.sdlv.Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP
    }

    override fun onItemDeleteAnimationFinished(view: View, position: Int) {
        mAppList.removeAt(position - mListView.headerViewsCount)
        mAdapter.notifyDataSetChanged()
        //toast("onItemDeleteAnimationFinished   position--->$position")
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        when (scrollState) {
            AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
            }
            AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
            }
            AbsListView.OnScrollListener.SCROLL_STATE_FLING -> {
            }
        }
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

    }

    override fun onItemLongClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
        //toast("onItemLongClick   position--->$position")
        return true
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        //toast("onScrollBackAnimationFinished   position--->$position")
    }

    /*private fun toast(toast: String) {
        mToast.setText(toast)
        mToast.show()
    }*/
}



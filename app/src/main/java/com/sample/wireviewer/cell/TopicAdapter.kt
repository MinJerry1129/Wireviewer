package com.sample.wireviewer.cell

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.koushikdutta.ion.Ion
import com.sample.wireviewer.R

internal class TopicAdapter(private val mContext: Context, list: List<Topic>) :
    BaseAdapter() {
    private var allTopicList: List<Topic> = ArrayList<Topic>()

    init {
        allTopicList = list
    }

    override fun getCount(): Int {
        return allTopicList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var listItem = convertView
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_topic, parent, false)
        val currentTopic: Topic = allTopicList[position]
        val list_txt = listItem!!.findViewById<View>(R.id.txt_text) as TextView


        val htmlText = HtmlCompat.fromHtml(currentTopic.result, HtmlCompat.FROM_HTML_MODE_LEGACY)
        list_txt.text = htmlText
        return listItem
    }
}
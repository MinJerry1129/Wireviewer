package com.sample.wireviewer

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import coil.Coil
import coil.load

class TopicdetailActivity : AppCompatActivity() {

    lateinit var _txtTopic: TextView
    lateinit var _imgTopic : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topicdetail)
        setReady()
    }

    fun setReady(){
        _txtTopic = findViewById(R.id.txt_topic)
        _imgTopic = findViewById(R.id.img_topic)
        val currentTopic = Common.oneTopic
        val htmlText = HtmlCompat.fromHtml(currentTopic.result , HtmlCompat.FROM_HTML_MODE_LEGACY)
        _txtTopic.movementMethod = LinkMovementMethod.getInstance()
        _txtTopic.text = htmlText

        _imgTopic.load(Common.getBaseURL() + currentTopic.iconURL){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_placeholder)
        }
    }
}
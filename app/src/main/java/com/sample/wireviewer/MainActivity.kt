package com.sample.wireviewer

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import coil.load
import com.koushikdutta.ion.Ion
import com.sample.wireviewer.cell.Topic
import com.sample.wireviewer.cell.TopicAdapter
import org.json.JSONObject
import java.util.Locale
import java.util.Objects

class MainActivity : AppCompatActivity() {
    lateinit var _txtHeader: TextView
    lateinit var _gridView : GridView
    lateinit var _txtSearch : TextView
    lateinit var _imgCloseSearch : ImageView

    lateinit var _layoutTopicDetail: LinearLayout
    lateinit var _txtTopic: TextView
    lateinit var _imgTopic : ImageView


    var mAllTopics :ArrayList<Topic> =arrayListOf()
    var mSearchTopics :ArrayList<Topic> =arrayListOf()

    var strHeader = ""
    var isTablet = false
    lateinit var selTopic : Topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setReady()
        getData()
    }

    private fun setReady() {

        val configuration = resources.configuration
        if (configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            isTablet = true
            _layoutTopicDetail = findViewById(R.id.layout_topic_detail)
            _txtTopic = findViewById(R.id.txt_topic)
            _imgTopic = findViewById(R.id.img_topic)
        } else {
            isTablet = false
        }

        _txtHeader = findViewById(R.id.txt_heading)
        _txtSearch = findViewById(R.id.txt_Search)
        _imgCloseSearch = findViewById(R.id.img_closesearch)
        _gridView = findViewById(R.id.grid_list)

        _txtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val pattern: String = _txtSearch.getText().toString().trim { it <= ' ' }
                searchTopic(pattern)
            }
        })
        _imgCloseSearch.setOnClickListener {
            _txtSearch.setText("")
            mSearchTopics.clear()
            for(onetopic in mAllTopics){
                mSearchTopics.add(onetopic)
            }
            _imgCloseSearch.visibility = View.INVISIBLE
            initView()
        }

        _gridView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            selTopic = mSearchTopics.get(position)
            if(isTablet){
                setTopicDetail()
            }else{
                Common.oneTopic = selTopic
                val intent: Intent = Intent(applicationContext, TopicdetailActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun getData() {
        val progressDialog = ProgressDialog(this, R.style.AppTheme_Bright_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Getting Data...")
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setCancelable(false)
        progressDialog.show()

        try {
            Ion.with(this)
                .load(Common.getApiURL())
                .asString()
                .setCallback { error, result ->
                    progressDialog.dismiss()
                    if (error != null) {
                        Toast.makeText(baseContext, error.message, Toast.LENGTH_LONG).show()
                    } else {

                        var mbuffAllTopics :ArrayList<Topic> =arrayListOf()
                        val json = JSONObject(result)
                        strHeader = json.getString("Heading")
                        val RelatedTopics = json.getJSONArray("RelatedTopics")

                        for(i in 0 .. RelatedTopics.length()-1){
                            val one_topic = RelatedTopics.getJSONObject(i)
                            val firsturl = one_topic.getString("FirstURL")
                            val icon_url = one_topic.getJSONObject("Icon").getString("URL")
                            val result = one_topic.getString("Result")
                            val text = one_topic.getString("Text")
                            mbuffAllTopics.add(Topic(firsturl, icon_url, result, text))
                        }
                        setValue(mbuffAllTopics)
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(baseContext, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun setValue(mbuffAllTopics: ArrayList<Topic>) {
        _txtHeader.setText(strHeader)

        for(onetopic in mbuffAllTopics){
            mAllTopics.add(onetopic)
            mSearchTopics.add(onetopic)
        }
        Log.d("restut::", mAllTopics.toString())
        initView()
    }

    private fun searchTopic(pattern: String) {
        mSearchTopics.clear()
        if (pattern.isEmpty()) {
            for(onetopic in mAllTopics){
                mSearchTopics.add(onetopic)
            }
            _imgCloseSearch.visibility = View.INVISIBLE
        } else {
            for (theTopic in mAllTopics) {
                if (theTopic.text.toLowerCase().contains(pattern.lowercase(Locale.getDefault())))
                {
                    mSearchTopics.add(theTopic)
                }
            }
            _imgCloseSearch.visibility = View.VISIBLE
        }
        initView()
    }

    fun initView() {
        val adapter_clinic = TopicAdapter(baseContext, mSearchTopics)
        _gridView.setAdapter(adapter_clinic)
    }

    fun setTopicDetail() {
        _layoutTopicDetail.visibility = View.VISIBLE
        val htmlText = HtmlCompat.fromHtml(selTopic.result , HtmlCompat.FROM_HTML_MODE_LEGACY)
        _txtTopic.movementMethod = LinkMovementMethod.getInstance()
        _txtTopic.text = htmlText

        _imgTopic.load(Common.getBaseURL() + selTopic.iconURL){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_placeholder)
        }
    }
}
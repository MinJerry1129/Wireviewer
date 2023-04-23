package com.sample.wireviewer

import com.sample.wireviewer.cell.Topic

object Common {
    private const val apiURL = "http://api.duckduckgo.com/?q=the+wire+characters&format=json"
    private const val baseURL = "https://duckduckgo.com"

    lateinit var oneTopic : Topic

    fun getApiURL(): String {
        return apiURL
    }
    fun getBaseURL(): String {
        return baseURL
    }
}
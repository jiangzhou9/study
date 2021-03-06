package com.yq.kotlindemo

import android.util.Log
import com.google.gson.Gson
import java.net.URL

class ForecastRequest(val zipCode: String) {
    //一个类里面一些静态属性、常亮或者函数。此对象被所有大类的对象共享，就像java中静态属性或者方法
    companion object {
        private val APP_ID = "15646a06818f61f7b8d7823ca833e1ce"
        private val URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&cnt=7"
        private val COMPLETE_URL = "$URL&APPID=$APP_ID&q="
    }
    fun execute(): ForecastRequest {
        val forecastJsonStr = URL(COMPLETE_URL + zipCode).readText()
        Log.d(javaClass.simpleName, forecastJsonStr)
        return Gson().fromJson(forecastJsonStr, ForecastRequest::class.java)
    }
}                                
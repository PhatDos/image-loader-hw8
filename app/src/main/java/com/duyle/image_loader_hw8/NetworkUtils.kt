package com.duyle.image_loader_hw8

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    fun isNetworkAvailable(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val active = cm.activeNetworkInfo
        return active != null && active.isConnected
    }
}

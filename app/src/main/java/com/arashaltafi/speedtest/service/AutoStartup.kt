package com.arashaltafi.speedtest.service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AutoStartup : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(p0: Context?, p1: Intent?) {
        val sharedPreferences = p0?.getSharedPreferences("serviceCheck", Context.MODE_PRIVATE)
        if (sharedPreferences!!.getBoolean("serviceRunning", false)) {
            val intent = Intent(p0, InternetSpeedMeter::class.java)
            p0.startService(intent)
        }
    }
}
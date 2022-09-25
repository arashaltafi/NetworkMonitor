package com.arashaltafi.speedtest.service

import android.content.Context
import android.net.TrafficStats
import android.os.AsyncTask
import android.view.View
import android.widget.Toast
import com.arashaltafi.speedtest.R
import com.arashaltafi.speedtest.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import java.net.URL

class DownloadSpeedTest(private val contextReference: WeakReference<Context>) : AsyncTask<Void, Void, Int>() {
    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Int?) {
        val activity = contextReference.get() as MainActivity
        activity.textView5.visibility = View.VISIBLE
        activity.imageButton.visibility = View.VISIBLE
        activity.progressBar.visibility = View.INVISIBLE
        activity.textView3.text = activity.getString(R.string.your_download_speed_is)
        var speed = ""
        if (result != null) {
            val context = contextReference.get()
            val sharedPreferences = context?.getSharedPreferences("speedUnit", Context.MODE_PRIVATE)
            val str = sharedPreferences?.getString("2", context.getString(R.string.bps))

            if (str == context?.getString(R.string.Bps)) {
                speed = when {
                    result < 0 -> "0 KB/s"
                    result in 0..999 -> "$result KB/s"
                    else -> String.format("%.2f", result / 1024f) + " MB/s"
                }
            }

            else if (str == context?.getString(R.string.bps)) {
                var r = result
                r *= 8
                speed = when {
                    r < 0 -> "0 Kb/s"
                    r in 0..999 -> "$r Kb/s"
                    else -> String.format("%.2f", r / 1024f) + " Mb/s"
                }
            }
        }
        activity.textView5.text = speed
    }

    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        val activity = contextReference.get() as MainActivity
        activity.textView5.visibility = View.INVISIBLE
        activity.imageButton.visibility = View.INVISIBLE
        activity.progressBar.visibility = View.VISIBLE
        activity.textView3.text = activity.getString(R.string.testing)
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg p0: Void?): Int {
        try {
            Toast.makeText(contextReference.get() as MainActivity, "test" , Toast.LENGTH_SHORT).show()
            val inputStream =
                URL("https://dl.google.com/dl/android/studio/install/3.4.1.0/android-studio-ide-183.5522156-windows.exe").openStream()
            val buf = ByteArray(5000)
            var startBytes: Long = 0
            var startTime: Long = 0
            for (i in 0..500) {
                inputStream.read(buf)
                if (i == 200) {
                    startBytes = TrafficStats.getTotalRxBytes()
                    startTime = System.nanoTime()
                }
            }
            val endTime = System.nanoTime()
            val endBytes = TrafficStats.getTotalRxBytes()
            val totalTimeInSec = (endTime - startTime) / 1000000000.0
            val totalDataInKB = (endBytes - startBytes) / 1024.0
            val speedDecimal = totalDataInKB / totalTimeInSec
            return speedDecimal.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }
}

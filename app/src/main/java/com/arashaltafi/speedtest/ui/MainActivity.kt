package com.arashaltafi.speedtest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arashaltafi.speedtest.service.DownloadSpeedTest
import com.arashaltafi.speedtest.model.Items
import com.arashaltafi.speedtest.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: MyAdapter
    private var items: ArrayList<Items> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = getColor(R.color.colorPrimaryDark)

        DownloadSpeedTest(WeakReference(this)).execute()

        imageButton.setOnClickListener {
            DownloadSpeedTest(WeakReference(this)).execute()
        }

        items.add(Items("Enable download speed meter", "Show download speed in status bar"))

        mAdapter = MyAdapter(items)

        list.apply {
            adapter = mAdapter
        }
    }
}
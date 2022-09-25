package com.arashaltafi.speedtest.ui

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arashaltafi.speedtest.R
import com.arashaltafi.speedtest.model.Items
import com.arashaltafi.speedtest.service.InternetSpeedMeter
import kotlinx.android.synthetic.main.row_item.view.*


class MyAdapter(private val list: ArrayList<Items>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val items = list[position]
        val context = holder.itemView.context
        if (position == 0) {
            holder.itemView.switch1.visibility = View.VISIBLE
            val sharedPreferences = context.getSharedPreferences("serviceCheck", Context.MODE_PRIVATE)
            if (sharedPreferences.getBoolean("serviceRunning", false))
                holder.itemView.switch1?.isChecked = isNotificationVisible(context)
            holder.itemView.switch1?.setOnCheckedChangeListener { _, b ->
                val intent = Intent(context, InternetSpeedMeter::class.java)
                if (b) {
                    context.startService(intent)
                    saveServiceStatus(true, context)
                }
                if (!b) {
                    context.stopService(intent)
                    saveServiceStatus(false, context)
                }
            }
        }
        holder.itemView.heading?.text = items.headings
        holder.itemView.description?.text = items.description
    }

    private fun isNotificationVisible(context: Context): Boolean {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val test = PendingIntent.getActivity(
            context, 1, notificationIntent,
            (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_IMMUTABLE
            else
                0x0)
                    or
                    PendingIntent.FLAG_CANCEL_CURRENT
        )
        return test != null
    }
    private fun saveServiceStatus(x: Boolean, context: Context) {
        val sharedPreferences = context.getSharedPreferences("serviceCheck", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("serviceRunning", x)
        editor.apply()
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
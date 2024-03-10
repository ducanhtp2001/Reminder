package com.example.workreminder.usecase.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.workreminder.usecase.service.ReminderService

class EventReceive: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("broad", "onReceive")
        val mIntent = Intent(context, ReminderService::class.java)
        mIntent.action = intent!!.action
        context!!.startForegroundService(mIntent)
    }
}
package com.mehmettas.familytrack.utils.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.os.Build

class ListenerServiceRestarter : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(
            ListenerServiceRestarter::class.java.simpleName,
            "Service Stoped"
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, LocationMonitoringService::class.java))
        } else {
            context.startService(Intent(context, LocationMonitoringService::class.java))
        }

        //context.startService(Intent(context, TrackService::class.java))
    }


}
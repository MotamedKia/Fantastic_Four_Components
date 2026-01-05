package com.example.fantasticfourcomponents

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class myDynamicReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BATTERY_LOW -> {
                logger("myDynamicReceiver says: Battery is Low 🪫")
            }

            Intent.ACTION_BATTERY_OKAY -> {
                logger("myDynamicReceiver says: Battery is Okay 🔋")
            }
        }
    }
}

class myStaticReceiver(): BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            logger("myStaticReceiver says: Boot completed 👍")
        }
    }
}
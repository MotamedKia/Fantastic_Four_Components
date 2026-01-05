package com.example.fantasticfourcomponents

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.fantasticfourcomponents.ui.theme.FantasticFourComponentsTheme

class MainActivity : ComponentActivity() {
    private val myDynamicReceiver = myDynamicReceiver()
    var isRegistered = false

    var servStat by mutableStateOf("...")
    var lastWork by mutableStateOf("...")
    var lastBroad by mutableStateOf("...")

    override fun onCreate(savedInstanceState: Bundle?) {
        fun registerBatteryReceiver() {
            if (!isRegistered) {
                val filter = IntentFilter().apply {
                    addAction(Intent.ACTION_BATTERY_LOW)
                    addAction(Intent.ACTION_BATTERY_OKAY)
                }
                registerReceiver(myDynamicReceiver, filter)
                isRegistered = true
                logger("registerBatteryReceiver says: Battery Registered ✅")
            }
        }

        fun unregisterBatteryReceiver(myDynamicReceiver: myDynamicReceiver) {
            if (isRegistered) {
                unregisterReceiver(myDynamicReceiver)
                isRegistered = false
                logger("unregisterBatteryReceiver says: Battery Unregistered ✅")
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FantasticFourComponentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        lastBroad = lastBroad,
                        onRegisterReceiver = {
                            registerBatteryReceiver()
                            lastBroad = "Battery receiver registered"
                        },
                        onUnregisterReceiver = {
                            unregisterBatteryReceiver(myDynamicReceiver)
                            lastBroad = "Battery receiver unregistered"
                        }
                    )
                }
            }
        }
    }
}
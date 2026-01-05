package com.example.fantasticfourcomponents

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
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
import androidx.core.app.ActivityCompat
import com.example.fantasticfourcomponents.ui.theme.FantasticFourComponentsTheme
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {
    private val myDynamicReceiver = myDynamicReceiver()
    var isRegistered = false

    var servStat by mutableStateOf("...")
    var lastWork by mutableStateOf("...")
    var lastBroad by mutableStateOf("...")

    override fun onCreate(savedInstanceState: Bundle?) {

        //BROADCAST HANDLING start
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
        //BROADCAST HANDLING end

        //SERVICE HANDLING start
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        fun startService() {
            val intent = Intent(this, MyForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            logger("startService says: Service started ✅")
        }

        fun stopService() {
            val intent = Intent(this, MyForegroundService::class.java)
            stopService(intent)
            logger("stopService says: Service stopped ✅")
        }
        //SERVICE HANDLING end

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
                        },
                        onStartService = {
                            startService()
                        },
                        onStopService = {
                            stopService()
                        }
                    )
                }
            }
        }
    }
}
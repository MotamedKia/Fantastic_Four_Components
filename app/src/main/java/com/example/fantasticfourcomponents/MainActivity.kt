package com.example.fantasticfourcomponents

import android.R.attr.label
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.BatteryState
import android.os.BatteryManager
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
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.constraints.trackers.BatteryNotLowTracker
import androidx.work.workDataOf
import com.example.fantasticfourcomponents.ui.theme.FantasticFourComponentsTheme
import java.util.concurrent.TimeUnit
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

            if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                logger("startService says: Notification permission denied ❌")
                servStat = "PERMISSION DENIED"
                return
            }

            val intent = Intent(this, MyForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                this@MainActivity.startService(intent)
            }
            logger("startService says: Service started ✅")
            servStat = "RUNNING"
        }

        fun stopService() {
            val intent = Intent(this, MyForegroundService::class.java)
            this@MainActivity.stopService(intent)
            logger("stopService says: Service stopped ✅")
            servStat = "STOPPED"
        }
        //SERVICE HANDLING end

        //WORKER HANDLING start
        val workManager = WorkManager.getInstance(this)
        fun enqueueOneTime(label: String, withConstraints: Boolean) {
            val constraints = if (withConstraints) {
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            } else {
                Constraints.NONE
            }
            val request = OneTimeWorkRequestBuilder<MyWorker>()
                .setInitialDelay(5, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .setInputData(workDataOf("label" to label))
                .build()
            workManager.enqueue(request)
            workManager
                .getWorkInfoByIdLiveData(request.id)
                .observe(this) { workInfo ->
                    when (workInfo?.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            lastWork = "Worker Executed"
                        }

                        WorkInfo.State.RUNNING -> {
                            lastWork = "Worker running…"
                        }

                        WorkInfo.State.ENQUEUED -> {
                            lastWork = "Worker enqueued (waiting)…"
                        }

                        WorkInfo.State.FAILED -> {
                            lastWork = "Worker failed!"
                        }

                        else -> Unit
                    }
                }
        }
        //WORKER HANDLING end

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FantasticFourComponentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        lastBroad = lastBroad, servStat = servStat, lastWork = lastWork,
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
                        },
                        onEnqueueWork = {
                            enqueueOneTime(label = "oneTimeWorker", withConstraints = false)
                        }
                    )
                }
            }
        }
    }
}
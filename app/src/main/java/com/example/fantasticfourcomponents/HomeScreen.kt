package com.example.fantasticfourcomponents

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onRegisterReceiver: () -> Unit,
    onUnregisterReceiver: () -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    servStat: String,
    lastWork: String,
    lastBroad: String
) {

    Column(
        modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        Button(onClick = onStartService) {
            Text("Start Player Service")
        }
        Button(onClick = onStopService) {
            Text("Stop Player Service")
        }
        Button(onClick = {}) {
            Text("Enqueue Worker (5s delay)")
        }
        Button(onClick = onRegisterReceiver) {
            Text("Register Battery Receiver")
        }
        Button(onClick = onUnregisterReceiver) {
            Text("Unregister Battery Receiver")
        }
    }
    Column(
        modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Service Status: " +
                    if (servStat != "...") {
                        "\n     $servStat"
                    } else {
                        servStat
                    }
        )
        Spacer(Modifier.height(5.dp))
        Text(
            "Last Worker: " +
                    if (lastWork != "...") {
                        "\n     $lastWork"
                    } else {
                        lastWork
                    }
        )
        Spacer(Modifier.height(5.dp))
        Text(
            "Last Broadcast: " +
                    if (lastBroad != "...") {
                        "\n     $lastBroad"
                    } else {
                        lastBroad
                    }
        )
    }
}

fun logger(msg: String, tag: String = "myTest") {
    Log.d(tag, msg)
}
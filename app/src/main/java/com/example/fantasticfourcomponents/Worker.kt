package com.example.fantasticfourcomponents

import android.content.Context
import androidx.work.*
import androidx.work.WorkerParameters

class MyWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        logger("MyWorker says: WorkManager executed after delay 🚶‍♂️‍➡️")
        return Result.success()
    }
}
package com.example.chronos.notification

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val reminderText = workerParams.inputData.getString("reminderText").orEmpty()
        NotificationUtils.showNotification(context, reminderText)
        return Result.success()
    }
}
package com.example.workreminder.usecase.workmanaget

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workreminder.usecase.service.ReminderService

class ExcuteWorkRequest (context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val workTitle = inputData.getString("workTitle")
        val notificationId = inputData.getInt("notificationId", 0)

        val intent = Intent(applicationContext, ReminderService::class.java)
        intent.putExtra("workTitle", workTitle)
        intent.putExtra("notificationId", notificationId)
        intent.action = "ALARM"
        applicationContext.startForegroundService(intent)

        return Result.success()
    }
}
package com.example.carewave1

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class MedicationReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // Check if context is null
        context ?: return

        // Create and show notification
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        // Create notification builder
        val builder = NotificationCompat.Builder(context, "medicationReminderChannelId")
            .setSmallIcon(R.drawable.icon_menu)
            .setContentTitle("Medication Reminder")
            .setContentText("It's time to take your medication!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }
}

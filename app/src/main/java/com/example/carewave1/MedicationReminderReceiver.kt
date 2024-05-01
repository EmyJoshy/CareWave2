package com.example.carewave1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

class MedicationReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        val medicineName = intent?.getStringExtra("medicineName") ?: ""
        val dose = intent?.getStringExtra("dose") ?: ""
        val time = intent?.getStringExtra("time") ?: ""

        showNotification(context, medicineName, dose, time)
    }


    private fun showNotification(context: Context, medicineName: String, dose: String, time: String) {
        val notificationMessage = "\nMedicine: $medicineName\nDose: $dose\nTime: $time"

        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "medicationReminderChannelId"
            val channelName = "Medication Reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), audioAttributes)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create a notification builder with high priority
        val builder = NotificationCompat.Builder(context, "medicationReminderChannelId")
            .setSmallIcon(R.drawable.icon_notification)
            .setContentTitle("Medication Reminder")
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set high priority
            .setCategory(NotificationCompat.CATEGORY_ALARM) // Set category to alarm
            //.setAutoCancel(true) // Automatically dismiss the notification when clicked
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)) // Set notification sound

        // Create an intent to launch the app when notification is clicked
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        // Generate a unique notification ID based on medicineName, dose, and time
        val notificationId = generateNotificationId(medicineName, dose, time)

        // Show the notification using the generated notification ID
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    // Function to generate a unique notification ID based on medicineName, dose, and time
    private fun generateNotificationId(medicineName: String, dose: String, time: String): Int {
        return (medicineName.hashCode() + dose.hashCode() + time.hashCode()) and 0xfffffff // Ensure it's positive and fits within the int range
    }
}

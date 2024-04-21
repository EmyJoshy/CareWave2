package com.example.carewave1

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class EditMedicationActivity : AppCompatActivity() {

    // Medication data class
    data class Medication(
        val medicineName: String = "",
        val dose: String = "",
        val time: String = ""
    )

    private lateinit var database: FirebaseDatabase
    private lateinit var medicationsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medication)

        database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        medicationsRef = database.getReference("medications").child(userId.toString())

        val editTextMedicineName = findViewById<EditText>(R.id.editTextMedicineName)
        val editTextDose = findViewById<EditText>(R.id.editTextDose)
        val editTextTime = findViewById<TimePicker>(R.id.editTextTime)
        val saveMedicationButton = findViewById<Button>(R.id.buttonSaveMedication)

        // Create a Notification Channel
        createNotificationChannel()

        saveMedicationButton.setOnClickListener {
            val enteredMedicineName = editTextMedicineName.text.toString()
            val enteredDose = editTextDose.text.toString()
            val enteredHour = editTextTime.hour // Get the hour from TimePicker
            val enteredMinute = editTextTime.minute // Get the minute from TimePicker
            val enteredTime = String.format("%02d:%02d", enteredHour, enteredMinute) // Format time as "HH:MM"

            val medicationId = medicationsRef.push().key
            val medication = Medication(enteredMedicineName, enteredDose, enteredTime)

            medicationId?.let {
                medicationsRef.child(it)
                    .setValue(medication)
                    .addOnSuccessListener {
                        // Schedule alarm for medication reminder
                        scheduleMedicationReminder(enteredTime) // Call scheduleMedicationReminder() with enteredTime
                        // Navigate to ViewMedicationActivity
                        val intent = Intent(this, ViewMedicationActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        // Error handling
                    }
            }
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Medication Reminder"
            val descriptionText = "Reminders for taking medication"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("medicationReminderChannelId", name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleMedicationReminder(time: String) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()

        if (currentHour < hour || (currentHour == hour && currentMinute < minute)) {
            // Schedule alarm for today
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
        } else {
            // Schedule alarm for tomorrow
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
        }

        val intent = Intent(this, MedicationReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}

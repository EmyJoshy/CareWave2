package com.example.carewave1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.carewave1.R.layout.activity_dashboard

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_dashboard)
        val buttonNavigate=findViewById<Button>(R.id.buttonHeartRate)
        buttonNavigate.setOnClickListener {
            val intent = Intent(this, HeartRateActivity::class.java)
            startActivity(intent)
        }

    }
}
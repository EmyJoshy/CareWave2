package com.example.carewave1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.appcompat.widget.AppCompatButton
import com.example.carewave1.R.id.buttonPressure

import com.example.carewave1.R.id.buttonSpO2
import com.example.carewave1.R.id.buttonSteps
import com.example.carewave1.R.layout.activity_login
import com.example.carewave1.R.layout.activity_main

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_login)

        // LoginPage >>> Dashboard
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////// NavBar to other pages //////////////////////////////////////

// Dashboard(nav bar icons) >> Respective Page
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_item1 -> {
                    // Navigate to View Medication 2 page
                    val intent = Intent(this, ViewMedicationActivity2::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_item2 -> {
                    // Navigate to Home page
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_item3 -> {
                    // Navigate to Profile page
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

}

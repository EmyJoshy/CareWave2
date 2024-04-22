package com.example.carewave1

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.type.Color
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ViewMedicationActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_medication2)

        // Get the current user's ID from Firebase Authentication
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // If the user is not logged in, return
        if (userId == null) {
            // Handle the case where the user is not logged in
            return
        }

        // Get a reference to the Firebase Realtime Database for the user's medications
        val database = FirebaseDatabase.getInstance()
        val medicationsRef = database.getReference("medications/$userId")

        // Reference to the LinearLayout to which cards will be added
        val medicationsLayout = findViewById<LinearLayout>(R.id.medicationsLayout)

        // Read medication data from Firebase Realtime Database
        medicationsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Loop through all medications for the current user
                for (medicationSnapshot in dataSnapshot.children) {
                    // Get medication data
                    val medication = medicationSnapshot.getValue(object : GenericTypeIndicator<Map<String, String>>() {})

                    // Create a new MaterialCardView for each medication
                    val cardView = MaterialCardView(this@ViewMedicationActivity2)
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.setMargins(70, 35, 70, 35)
                    cardView.layoutParams = layoutParams
                    layoutParams.height = 380 // Adjusted height in pixels
                    cardView.cardElevation = 10f
                    cardView.radius = 18f // Larger radius
                    cardView.isClickable = true
                    cardView.isFocusable = true

                    // Create TextView for medicine name
                    val textViewMedicineName = TextView(this@ViewMedicationActivity2)
                    textViewMedicineName.text = "Medicine Name: ${medication?.get("medicineName")}"
                    textViewMedicineName.textSize = 20f // Larger text size
                    textViewMedicineName.setTypeface(null, Typeface.BOLD) // Make text bold
                    val medicineNameLayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Width
                        LinearLayout.LayoutParams.WRAP_CONTENT // Height
                    )
                    medicineNameLayoutParams.setMargins(25, 8, 0, 8) // Set margins if needed
                    textViewMedicineName.layoutParams = medicineNameLayoutParams

                    // Always set the text color to purple1
                    textViewMedicineName.setTextColor(ContextCompat.getColor(this@ViewMedicationActivity2, R.color.purple1))



                    // Create TextView for dose
                    val textViewDose = TextView(this@ViewMedicationActivity2)
                    textViewDose.text = "Dose: ${medication?.get("dose")}"
                    textViewDose.textSize = 18f // Larger text size
                    textViewDose.setTypeface(null, Typeface.BOLD) // Make text bold
                    val doseLayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Width
                        LinearLayout.LayoutParams.WRAP_CONTENT // Height
                    )
                    doseLayoutParams.setMargins(25, 158, 0, 12) // Set margins if needed
                    textViewDose.layoutParams = doseLayoutParams


                    // Get the current time in the format matching your Firebase time format
                    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)
                    // Create TextView for time
                    val textViewTime = TextView(this@ViewMedicationActivity2)
                    textViewTime.text = "Time: ${medication?.get("time")}"
                    textViewTime.textSize = 18f // Larger text size
                    val timeLayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Width
                        LinearLayout.LayoutParams.WRAP_CONTENT // Height
                    )
                    timeLayoutParams.setMargins(25, 300, 0, 18) // Set margins if needed
                    textViewTime.layoutParams = timeLayoutParams

                    // Set color for the time text if it matches the current time retrieved from Firebase
                    if (medication?.get("time") == currentTime) {
                        textViewTime.setTextColor(ContextCompat.getColor(this@ViewMedicationActivity2, R.color.purple1))
                    }


                    // Add TextViews to the card
                    cardView.addView(textViewMedicineName)
                    cardView.addView(textViewDose)
                    cardView.addView(textViewTime)
                    // Add the card to the medicationsLayout
                    medicationsLayout.addView(cardView)


                    //////////// Add a delete button to each card//////////////////
                    val deleteButton = Button(this@ViewMedicationActivity2)
                    deleteButton.text = "Delete"
                    deleteButton.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    /////// Create the delete button as an ImageButton
//                    val deleteButton = ImageButton(this@ViewMedicationActivity2)
//                    deleteButton.setImageResource(R.drawable.delete_icon) // Set the image resource to the delete icon

                    // Set layout parameters for the delete button
                    val deleteButtonLayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    deleteButtonLayoutParams.gravity = Gravity.END or Gravity.BOTTOM // Align to bottom-right corner
                    deleteButtonLayoutParams.setMargins(700, 220, 20, 16) // Add margin for spacing
                    deleteButton.layoutParams = deleteButtonLayoutParams
                    // Set onClickListener for the delete button
                    deleteButton.setOnClickListener {
                        // Get the ID of the medication to be deleted
                        val medicationId = medicationSnapshot.key!!

                        // Get a reference to the specific medication in the database
                        val medicationToDeleteRef = medicationsRef.child(medicationId)

                        // Delete the medication from the database
                        medicationToDeleteRef.removeValue()

                        // Remove the card from the layout
                        medicationsLayout.removeView(cardView)
                    }
                    // Add the delete button to the card
                    cardView.addView(deleteButton)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
}

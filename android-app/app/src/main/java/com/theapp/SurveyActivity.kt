package com.theapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.theapp.databinding.ActivitySurveyBinding

class SurveyActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySurveyBinding
    private var selectedRating = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRatingButtons()
        setupSubmitButton()
        setupLogoutDeleteButtons()
    }
    
    private fun setupRatingButtons() {
        val ratingButtons = listOf(
            binding.btnRating1,
            binding.btnRating2,
            binding.btnRating3,
            binding.btnRating4,
            binding.btnRating5
        )
        
        ratingButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                selectedRating = index + 1
                updateRatingUI(ratingButtons, index)
            }
        }
    }
    
    private fun updateRatingUI(buttons: List<Button>, selectedIndex: Int) {
        buttons.forEachIndexed { index, button ->
            if (index <= selectedIndex) {
                button.setBackgroundColor(getColor(R.color.purple_500))
                button.setTextColor(getColor(android.R.color.white))
            } else {
                button.setBackgroundColor(getColor(R.color.lighter_gray))
                button.setTextColor(getColor(android.R.color.black))
            }
        }
    }
    
    private fun setupSubmitButton() {
        binding.btnSubmitSurvey.setOnClickListener {
            val recommendation = binding.etRecommendation.text.toString()
            val experience = binding.etExperience.text.toString()
            
            if (selectedRating == 0) {
                Toast.makeText(this, "Please rate your experience", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (recommendation.isEmpty() || experience.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Save survey data
            saveSurveyData(recommendation, experience, selectedRating)
            
            Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
            
            // Go back to main screen
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }
    }
    
    private fun saveSurveyData(recommendation: String, experience: String, rating: Int) {
        val prefs = getSharedPreferences("survey_data", MODE_PRIVATE)
        prefs.edit().apply {
            putString("recommendation", recommendation)
            putString("experience", experience)
            putInt("rating", rating)
            putLong("timestamp", System.currentTimeMillis())
            apply()
        }
    }
    
    private fun setupLogoutDeleteButtons() {
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
        
        binding.btnDeleteAccount.setOnClickListener {
            showDeleteConfirmation()
        }
    }
    
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this@SurveyActivity)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this@SurveyActivity)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                performDeleteAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun performLogout() {
        // Clear user data
        UserManager.clearUserData(this)
        
        // Clear test results
        val prefs = getSharedPreferences("test_results", MODE_PRIVATE)
        prefs.edit().clear().apply()
        
        // Go to login
        startActivity(Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }
    
    private fun performDeleteAccount() {
        // Clear all user data
        UserManager.clearUserData(this)
        
        // Clear test results
        val prefs = getSharedPreferences("test_results", MODE_PRIVATE)
        prefs.edit().clear().apply()
        
        // Clear survey data
        val surveyPrefs = getSharedPreferences("survey_data", MODE_PRIVATE)
        surveyPrefs.edit().clear().apply()
        
        Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
        
        // Go to login
        startActivity(Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }
}

package com.theapp

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.theapp.databinding.ActivityOccupationSelectionBinding

class OccupationSelectionActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityOccupationSelectionBinding
    private lateinit var occupationsList: ListView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityOccupationSelectionBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            // Get assessment results from intent with safe defaults
            val rScore = intent?.getIntExtra("r_score", 5) ?: 5
            val iScore = intent?.getIntExtra("i_score", 3) ?: 3
            val aScore = intent?.getIntExtra("a_score", 2) ?: 2
            val sScore = intent?.getIntExtra("s_score", 8) ?: 8
            val eScore = intent?.getIntExtra("e_score", 4) ?: 4
            val cScore = intent?.getIntExtra("c_score", 1) ?: 1
            val aptitudeScore = intent?.getIntExtra("aptitude_score", 75) ?: 75
            
            // Find dominant type safely
            val scores = mapOf("R" to rScore, "I" to iScore, "A" to aScore, "S" to sScore, "E" to eScore, "C" to cScore)
            val dominantType = scores.maxByOrNull { it.value }?.key ?: "S"
            
            // Set basic results with null checks
            binding.tvPersonalityType?.text = "Your Personality Type: ${getTypeName(dominantType)}"
            binding.tvPersonalityDescription?.text = getTypeDescription(dominantType)
            binding.tvAptitudeScore?.text = "Aptitude Score: $aptitudeScore%"
            
            // Set RIASEC scores with null checks
            binding.tvRealisticScore?.text = "R: $rScore"
            binding.tvInvestigativeScore?.text = "I: $iScore"
            binding.tvArtisticScore?.text = "A: $aScore"
            binding.tvSocialScore?.text = "S: $sScore"
            binding.tvEnterprisingScore?.text = "E: $eScore"
            binding.tvConventionalScore?.text = "C: $cScore"
            
            setupOccupationList(dominantType)
            
            // Setup button listeners
            binding.btnRestart?.setOnClickListener {
                finish()
            }
            
            binding.btnFeedback?.setOnClickListener {
                showFeedbackDialog()
            }
            
            binding.btnLogout?.setOnClickListener {
                logoutUser()
            }
            
            binding.btnDeleteAccount?.setOnClickListener {
                showDeleteConfirmation()
            }
            
            // Initialize Velly Bandaar guide on occupation selection screen
            initializeResultsGuide()
            
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error in onCreate", e)
            // Set fallback UI
            setFallbackUI()
        }
    }
    
    private fun setFallbackUI() {
        try {
            binding.tvPersonalityType?.text = "Career Assessment Results"
            binding.tvPersonalityDescription?.text = "Your assessment has been completed successfully."
            binding.tvAptitudeScore?.text = "Aptitude Score: Available"
            setupOccupationList("S") // Default to Social type
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error in fallback UI", e)
            finish() // Close activity if everything fails
        }
    }
    
    private fun setupOccupationList(dominantType: String) {
        try {
            // Get recommended occupations based on personality type
            val occupations = getRecommendedOccupations(dominantType)
            
            if (occupations.isNotEmpty()) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    occupations
                )
                
                binding.listOccupations?.adapter = adapter
                
                binding.listOccupations?.setOnItemClickListener { _, _, position, _ ->
                    try {
                        if (position >= 0 && position < occupations.size) {
                            val selectedOccupation = occupations[position]
                            openRoadmapDetail(selectedOccupation)
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("OccupationSelection", "Error in item click", e)
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error setting up occupation list", e)
        }
    }
    
    private fun openRoadmapDetail(occupation: String) {
        try {
            val newIntent = Intent(this, RoadmapDetailActivity::class.java)
            newIntent.putExtra("occupation_title", occupation)
            
            // Get scores from current intent safely
            val currentIntent = intent
            newIntent.putExtra("r_score", currentIntent?.getIntExtra("r_score", 5) ?: 5)
            newIntent.putExtra("i_score", currentIntent?.getIntExtra("i_score", 3) ?: 3)
            newIntent.putExtra("a_score", currentIntent?.getIntExtra("a_score", 2) ?: 2)
            newIntent.putExtra("s_score", currentIntent?.getIntExtra("s_score", 8) ?: 8)
            newIntent.putExtra("e_score", currentIntent?.getIntExtra("e_score", 4) ?: 4)
            newIntent.putExtra("c_score", currentIntent?.getIntExtra("c_score", 1) ?: 1)
            newIntent.putExtra("aptitude_score", currentIntent?.getIntExtra("aptitude_score", 75) ?: 75)
            
            startActivity(newIntent)
        } catch (e: Exception) {
            // Log error and show fallback
            android.util.Log.e("OccupationSelection", "Error opening roadmap detail", e)
            // Could show a toast or dialog here
        }
    }
    
    private fun getRecommendedOccupations(type: String): List<String> {
        // Use new OccupationsDatabase with 637 O*NET careers
        val allFiltered = OccupationsDatabase.getOccupationsByType(type)
        
        // Return all filtered occupations (will be 36-279 depending on type)
        return allFiltered
    }
    
    private fun getTypeName(type: String): String {
        return when (type) {
            "R" -> "Realistic"
            "I" -> "Investigative"
            "A" -> "Artistic"
            "S" -> "Social"
            "E" -> "Enterprising"
            "C" -> "Conventional"
            else -> "Social"
        }
    }
    
    private fun getTypeDescription(type: String): String {
        return when (type) {
            "R" -> "Practical, hands-on problem solvers who enjoy working with tools and machinery"
            "I" -> "Analytical, intellectual, scientific thinkers who love research and investigation"
            "A" -> "Creative, expressive, original individuals who value artistic expression"
            "S" -> "Helpful, caring, people-oriented individuals who enjoy helping others"
            "E" -> "Persuasive, ambitious, leadership-oriented people who enjoy business and management"
            "C" -> "Organized, detail-oriented, systematic workers who prefer structured environments"
            else -> "Helpful, caring, people-oriented individuals who enjoy helping others"
        }
    }
    
    private fun showFeedbackDialog() {
        try {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("📝 Share Your Feedback")
            
            val input = android.widget.EditText(this).apply {
                hint = "Tell us what you think..."
                minLines = 4
                gravity = android.view.Gravity.TOP
                setPadding(16, 16, 16, 16)
            }
            
            builder.setView(input)
            builder.setPositiveButton("Send") { _, _ ->
                val feedback = input.text.toString()
                if (feedback.isNotEmpty()) {
                    saveFeedback(feedback)
                    android.widget.Toast.makeText(this, "✅ Thank you for your feedback!", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            builder.show()
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error showing feedback dialog", e)
        }
    }
    
    private fun saveFeedback(feedback: String) {
        try {
            // Save to SharedPreferences
            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            prefs.edit().putString("user_feedback", feedback).apply()
            android.util.Log.d("OccupationSelection", "Feedback saved: $feedback")
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error saving feedback", e)
        }
    }
    
    private fun logoutUser() {
        try {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("🚪 Logout")
            builder.setMessage("Are you sure you want to logout?")
            builder.setPositiveButton("Yes") { _, _ ->
                // Clear only login status, NOT test results
                // This allows users to see their results when they log back in
                val prefs = getSharedPreferences("theapp_prefs", MODE_PRIVATE)
                with(prefs.edit()) {
                    putBoolean("is_logged_in", false)
                    remove("user_email")
                    remove("login_date")
                    apply()
                }
                
                // Also clear any other SharedPreferences
                val appPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                appPrefs.edit().clear().apply()
                
                android.util.Log.d("OccupationSelection", "✅ User logged out successfully (test results preserved)")
                
                // Navigate to auth
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            builder.show()
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error logging out", e)
        }
    }
    
    private fun showDeleteConfirmation() {
        try {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("🗑️ Delete Account")
            builder.setMessage("Are you sure? This action cannot be undone. All your data will be permanently deleted.")
            builder.setPositiveButton("Delete") { _, _ ->
                deleteAccount()
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            builder.show()
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error showing delete confirmation", e)
        }
    }
    
    private fun deleteAccount() {
        try {
            // Get user email before clearing data
            val userEmail = UserManager.getUserEmail(this)
            
            android.util.Log.d("OccupationSelection", "🗑️ Deleting account for email: $userEmail")
            
            // Clear all UserManager data
            UserManager.clearUserData(this)
            
            // Also clear any other SharedPreferences
            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            prefs.edit().clear().apply()
            
            // Remove email from approved emails list and Firebase if it exists
            if (!userEmail.isNullOrEmpty()) {
                ApprovedEmailManager.removeApprovedEmail(this, userEmail)
                android.util.Log.d("OccupationSelection", "✅ Removed email from approved list: $userEmail")
                
                // Delete payment request from Firebase (cross-platform deletion)
                deletePaymentRequestFromFirebase(userEmail)
                android.util.Log.d("OccupationSelection", "✅ Deleted payment request from Firebase: $userEmail")
                
                // Also delete test results from Firebase
                TestResultsSync.deleteTestResultsFromFirebase(userEmail)
                android.util.Log.d("OccupationSelection", "✅ Deleted test results from Firebase: $userEmail")
            }
            
            android.widget.Toast.makeText(this, "✅ Account deleted successfully", android.widget.Toast.LENGTH_SHORT).show()
            
            // Navigate to auth
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error deleting account", e)
            android.widget.Toast.makeText(this, "❌ Error deleting account", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun deletePaymentRequestFromFirebase(email: String) {
        try {
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
            db.collection("paymentRequests")
                .whereEqualTo("email", email.lowercase())
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.delete()
                            .addOnSuccessListener {
                                android.util.Log.d("OccupationSelection", "✅ Deleted payment request: ${document.id}")
                            }
                            .addOnFailureListener { e ->
                                android.util.Log.e("OccupationSelection", "❌ Error deleting payment request: ${e.message}")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    android.util.Log.e("OccupationSelection", "❌ Error querying payment requests: ${e.message}")
                }
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "❌ Error deleting payment request: ${e.message}")
        }
    }
    
    private fun initializeResultsGuide() {
        try {
            android.util.Log.d("OccupationSelectionActivity", "🐵 Attempting to initialize guide...")
            // Get the root view from the activity's content view
            val rootView = window.decorView.findViewById<ViewGroup>(android.R.id.content)
            android.util.Log.d("OccupationSelectionActivity", "🐵 Root view obtained: ${rootView?.javaClass?.simpleName}")
            
            if (rootView != null) {
                val resultsGuide = ResultsGuideSystem(this, rootView)
                resultsGuide.init()
                android.util.Log.d("OccupationSelectionActivity", "🐵 Guide initialized successfully")
            } else {
                android.util.Log.e("OccupationSelectionActivity", "❌ Root view is null")
            }
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelectionActivity", "❌ Error initializing guide: ${e.message}", e)
        }
    }
}

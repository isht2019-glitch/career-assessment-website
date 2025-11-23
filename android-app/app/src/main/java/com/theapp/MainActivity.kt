package com.theapp

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.theapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupUI()
        
        // Guide disabled for now
        // initializeMainGuide()
    }
    
    private fun setupUI() {
        // Show splash screen for 2 seconds before navigating
        binding.root.postDelayed({
            try {
                // Check if user is already logged in
                val isLoggedIn = checkLoginStatus()
                val testCompleted = UserManager.hasCompletedTest(this)
                val userEmail = UserManager.getUserEmail(this)
                
                android.util.Log.d("MainActivity", "🔍 Login Status Check:")
                android.util.Log.d("MainActivity", "  - isLoggedIn: $isLoggedIn")
                android.util.Log.d("MainActivity", "  - testCompleted: $testCompleted")
                android.util.Log.d("MainActivity", "  - userEmail: $userEmail")
                
                if (isLoggedIn) {
                    // Check if test is completed
                    if (testCompleted) {
                        // Test completed, show results
                        android.util.Log.d("MainActivity", "✅ Navigating to OccupationSelection (test completed)")
                        navigateToOccupationSelection()
                    } else {
                        // Test not completed, show test (payment screen will appear after personality test)
                        android.util.Log.d("MainActivity", "📝 Navigating to Test (test not completed)")
                        navigateToTest()
                    }
                } else {
                    android.util.Log.d("MainActivity", "🔐 Not logged in, navigating to Auth")
                    navigateToAuth()
                }
            } catch (e: Exception) {
                // If there's an error, stay on MainActivity and show error
                android.util.Log.e("MainActivity", "❌ Error in setupUI", e)
                android.widget.Toast.makeText(this, "Error: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
            }
        }, 2000)
    }
    
    private fun checkLoginStatus(): Boolean {
        // Use centralized UserManager to check login status
        val isLoggedIn = UserManager.isLoggedIn(this)
        android.util.Log.d("MainActivity", "checkLoginStatus: $isLoggedIn")
        return isLoggedIn
    }
    
    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun navigateToPayment() {
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun navigateToTest() {
        // Navigate to test for approved users
        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun navigateToOccupationSelection() {
        val stored = UserManager.getStoredTestResults(this)
        if (stored != null) {
            val intent = Intent(this, OccupationSelectionActivity::class.java)
            intent.putExtra("dominant_type", stored.dominantType)
            intent.putExtra("aptitude_score", stored.aptitudeScore)
            intent.putExtra("r_score", stored.rScore)
            intent.putExtra("i_score", stored.iScore)
            intent.putExtra("a_score", stored.aScore)
            intent.putExtra("s_score", stored.sScore)
            intent.putExtra("e_score", stored.eScore)
            intent.putExtra("c_score", stored.cScore)
            startActivity(intent)
            finish()
        } else {
            navigateToTest()
        }
    }
    
    private fun initializeMainGuide() {
        // Guide disabled for now
        // Create Velly Bandaar guide for main page
        // val rootView = findViewById<FrameLayout>(android.R.id.content)
        // val mainGuide = MainGuideSystem(this, rootView)
        // mainGuide.init()
    }
}

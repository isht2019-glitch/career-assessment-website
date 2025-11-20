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
                
                if (isLoggedIn) {
                    // Check if payment is approved
                    val isPaymentApproved = UserManager.isPaymentApproved(this)
                    
                    if (isPaymentApproved) {
                        // Payment approved, check if test completed
                        if (UserManager.hasCompletedTest(this)) {
                            navigateToOccupationSelection()
                        } else {
                            navigateToTest()
                        }
                    } else {
                        // Payment not approved, show payment screen
                        navigateToPayment()
                    }
                } else {
                    navigateToAuth()
                }
            } catch (e: Exception) {
                // If there's an error, stay on MainActivity and show error
                android.widget.Toast.makeText(this, "Error: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
            }
        }, 2000)
    }
    
    private fun checkLoginStatus(): Boolean {
        // Use centralized UserManager to check login status
        return UserManager.isLoggedIn(this)
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
        // Create Velly Bandaar guide for main page
        val rootView = findViewById<FrameLayout>(android.R.id.content)
        val mainGuide = MainGuideSystem(this, rootView)
        mainGuide.init()
    }
}

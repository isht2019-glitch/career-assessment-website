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
        
        // Initialize Velly Bandaar guide for main page
        initializeMainGuide()
    }
    
    private fun setupUI() {
        // Show splash screen for 2 seconds before navigating
        binding.root.postDelayed({
            try {
                // Check if user is already logged in
                val isLoggedIn = checkLoginStatus()
                
                if (isLoggedIn) {
                    // Skip payment and go directly to test
                    navigateToTest()
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
    
    private fun navigateToTest() {
        // Navigate to payment screen for logged-in users
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun initializeMainGuide() {
        // Create Velly Bandaar guide for main page
        val rootView = findViewById<FrameLayout>(android.R.id.content)
        val mainGuide = MainGuideSystem(this, rootView)
        mainGuide.init()
    }
}

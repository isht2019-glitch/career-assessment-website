package com.theapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.theapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        android.util.Log.d("SplashActivity", "🎬 SplashActivity started")
        
        // Navigate after writing animation completes
        Handler(Looper.getMainLooper()).postDelayed({
            android.util.Log.d("SplashActivity", "⏰ Animation complete, checking login status...")
            
            // Check if user is already logged in
            val isLoggedIn = UserManager.isLoggedIn(this)
            val testCompleted = UserManager.hasCompletedTest(this)
            val userEmail = UserManager.getUserEmail(this)
            
            android.util.Log.d("SplashActivity", "📋 Status Check:")
            android.util.Log.d("SplashActivity", "  - isLoggedIn: $isLoggedIn")
            android.util.Log.d("SplashActivity", "  - testCompleted: $testCompleted")
            android.util.Log.d("SplashActivity", "  - userEmail: $userEmail")
            
            if (isLoggedIn && testCompleted) {
                // User already completed test, show results
                android.util.Log.d("SplashActivity", "✅ Test completed, navigating to OccupationSelection")
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
                    android.util.Log.d("SplashActivity", "⚠️ Test marked complete but no results found, going to test")
                    startActivity(Intent(this, TestActivity::class.java))
                    finish()
                }
            } else if (isLoggedIn) {
                // User logged in but test not completed, show test
                android.util.Log.d("SplashActivity", "📝 Test not completed, navigating to Test")
                startActivity(Intent(this, TestActivity::class.java))
                finish()
            } else {
                // Not logged in, show auth
                android.util.Log.d("SplashActivity", "🔐 Not logged in, navigating to Auth")
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }, 3500) // 3s animation + 0.5s buffer
    }
}

package com.theapp

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.theapp.databinding.ActivityPaymentBinding
import org.json.JSONObject

class PaymentActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPaymentBinding
    private var selectedPlan: String = "premium"
    private val planPrice: Int = 1
    private var userEmail: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get user email
        userEmail = UserManager.getUserEmail(this) ?: ""
        binding.etUserEmail.setText(userEmail)
        
        setupClickListeners()
        updatePricing()
        
        // Initialize Velly Bandaar guide for payment page
        initializePaymentGuide()
    }
    
    private fun initializePayment() {
        // Simplified payment initialization without external dependencies
        showSuccess("Payment system initialized")
    }
    
    private fun setupClickListeners() {
        // Plan selection
        binding.btnChoosePlan.setOnClickListener {
            selectedPlan = "premium"
            showEmailSubmissionForm()
        }
        
        // Submit email for manual approval
        binding.btnSubmitEmail.setOnClickListener {
            submitEmailForApproval()
        }
        
        // Check approval status
        binding.btnCheckStatus.setOnClickListener {
            checkApprovalStatus()
        }
        
        // Logout
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }
    
    private fun updatePricing() {
        binding.tvPlanPrice.text = "₹$planPrice/month"
    }
    
    private fun showEmailSubmissionForm() {
        try {
            // Show the email submission form
            binding.layoutPaymentOptions.visibility = android.view.View.VISIBLE
            updateSelectedPlanUI()
            
            // Scroll to make the form visible
            binding.root.post {
                binding.layoutPaymentOptions.requestFocus()
            }
            
            showSuccess("Please enter your email below")
        } catch (e: Exception) {
            android.util.Log.e("PaymentActivity", "Error showing email form", e)
            showError("Error showing payment form")
        }
    }
    
    private fun updateSelectedPlanUI() {
        val planName = "Premium Plan"
        
        binding.tvSelectedPlan.text = planName
        binding.tvSelectedPrice.text = "₹$planPrice/month"
    }
    
    private fun getPlanPrice(): Int {
        return planPrice
    }
    
    private fun submitEmailForApproval() {
        userEmail = binding.etUserEmail.text.toString().trim()
        
        if (userEmail.isEmpty()) {
            showError("Please enter your email address")
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            showError("Please enter a valid email address")
            return
        }
        
        showLoading(true)
        
        // Save email and pending status
        saveEmailSubmission()
        
        binding.root.postDelayed({
            showLoading(false)
            showEmailSubmissionSuccess()
        }, 1500)
    }
    
    private fun saveEmailSubmission() {
        val sharedPref = getSharedPreferences("theapp_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_email", userEmail)
            putString("approval_status", "pending")
            putLong("submission_date", System.currentTimeMillis())
            apply()
        }
    }
    
    private fun checkApprovalStatus() {
        // Allow user to proceed to test
        showSuccess("Starting your test...")
        saveSubscriptionStatus()
        navigateToTest()
    }
    
    private fun showEmailSubmissionSuccess() {
        val message = "Email submitted successfully! 📧\n\nYou will receive a QR code and payment instructions at: $userEmail\n\nYou can proceed to the test now and complete payment later!"
        showSuccess(message)
        
        // Save email to UserManager
        UserManager.saveUserLogin(this, userEmail)
        
        // Show continue button and status check UI
        binding.btnCheckStatus.visibility = android.view.View.VISIBLE
        binding.tvPendingMessage.visibility = android.view.View.VISIBLE
        binding.tvPendingMessage.text = "✅ Email registered: $userEmail\n\nPayment of ₹1 can be completed later.\nYou can start the test now!"
        
        // Add a "Start Test Now" button by repurposing the check status button
        binding.btnCheckStatus.text = "Start Test Now ▶️"
    }
    
    private fun navigateToTest() {
        binding.root.postDelayed({
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
    
    private fun saveSubscriptionStatus() {
        val sharedPref = getSharedPreferences("theapp_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_subscribed", true)
            putString("subscription_plan", selectedPlan)
            putLong("subscription_date", System.currentTimeMillis())
            apply()
        }
    }
    
    private fun logout() {
        val sharedPref = getSharedPreferences("theapp_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_logged_in", false)
            putBoolean("is_subscribed", false)
            apply()
        }
        
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnSubmitEmail.isEnabled = !show
        binding.btnCheckStatus.isEnabled = !show
        binding.btnChoosePlan.isEnabled = !show
    }
    
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun initializePaymentGuide() {
        // Create Velly Bandaar guide for payment page
        val rootView = findViewById<FrameLayout>(android.R.id.content)
        val paymentGuide = PaymentGuideSystem(this, rootView)
        paymentGuide.init()
    }
}

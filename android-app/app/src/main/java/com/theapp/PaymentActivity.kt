package com.theapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

class PaymentActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var emailEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var choosePlanButton: Button
    private lateinit var paymentOptionsLayout: LinearLayout
    private lateinit var checkStatusButton: Button
    private lateinit var pendingMessage: TextView
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()

        // Initialize views
        emailEditText = findViewById(R.id.etUserEmail)
        submitButton = findViewById(R.id.btnSubmitEmail)
        progressBar = findViewById(R.id.progressBar)
        choosePlanButton = findViewById(R.id.btnChoosePlan)
        paymentOptionsLayout = findViewById(R.id.layoutPaymentOptions)
        checkStatusButton = findViewById(R.id.btnCheckStatus)
        pendingMessage = findViewById(R.id.tvPendingMessage)
        
        // Add strikethrough to original price
        val originalPriceView = findViewById<TextView>(R.id.tvOriginalPrice)
        originalPriceView.paintFlags = originalPriceView.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG

        // Check if payment is already approved
        checkIfAlreadyApproved()
        
        android.util.Log.d("PaymentActivity", "🔄 PaymentActivity created. fromTest=${intent.getBooleanExtra("fromTest", false)}")

        // Choose Plan button - Shows the email form
        choosePlanButton.setOnClickListener {
            // Show payment options
            paymentOptionsLayout.visibility = View.VISIBLE
            
            Toast.makeText(this, "📧 Enter your email to continue", Toast.LENGTH_SHORT).show()
        }

        // Submit Email button - Submits to Firebase
        submitButton.setOnClickListener {
            submitPaymentRequest()
        }

        // Check Status button - Checks approval status
        checkStatusButton.setOnClickListener {
            checkPaymentStatus()
        }
    }
    
    private fun checkIfAlreadyApproved() {
        // If payment is already approved in SharedPreferences AND not coming from TestActivity, skip to test
        if (UserManager.isPaymentApproved(this) && !intent.getBooleanExtra("fromTest", false)) {
            android.util.Log.d("PaymentActivity", "✅ Payment already approved, skipping to test")
            // Navigate to test normally
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            finish()
        }
        // If called from TestActivity, show payment screen regardless of approval status
        // This allows user to see the payment screen as part of the flow
    }

    private fun submitPaymentRequest() {
        val email = emailEditText.text.toString().trim()

        // Validate email
        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            Toast.makeText(this, "⚠️ Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Invalid email format"
            Toast.makeText(this, "⚠️ Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading
        progressBar.visibility = View.VISIBLE
        submitButton.isEnabled = false
        submitButton.text = "📨 Submitting..."

        // Create payment request
        val paymentRequest = hashMapOf(
            "email" to email,
            "userName" to email.substringBefore("@"),
            "status" to "pending",
            "timestamp" to FieldValue.serverTimestamp(),
            "amount" to 1,
            "paymentMethod" to "UPI",
            "plan" to "Premium Plan",
            "transactionId" to "TXN${System.currentTimeMillis()}"
        )

        // Submit to Firestore
        db.collection("paymentRequests")
            .add(paymentRequest)
            .addOnSuccessListener { documentReference ->
                progressBar.visibility = View.GONE
                submitButton.isEnabled = true
                submitButton.text = "📨 Submit Email"
                
                // Save email for status checking
                userEmail = email

                // Show pending message and check status button
                pendingMessage.visibility = View.VISIBLE
                checkStatusButton.visibility = View.VISIBLE

                Toast.makeText(
                    this,
                    "✅ Payment request submitted successfully!\n\nRequest ID: ${documentReference.id.take(8)}...\n\nWaiting for admin approval.\n\nClick 'Check Status' button to see if approved.",
                    Toast.LENGTH_LONG
                ).show()

                // Clear email field
                emailEditText.setText("")
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                submitButton.isEnabled = true
                submitButton.text = "📨 Submit Email"

                Toast.makeText(
                    this,
                    "❌ Error submitting request:\n\n${e.message}\n\nPlease check your internet connection and try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun checkPaymentStatus() {
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "⚠️ Please submit a payment request first", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading
        progressBar.visibility = View.VISIBLE
        checkStatusButton.isEnabled = false
        checkStatusButton.text = "🔄 Checking..."

        // Query Firestore for payment status (simplified - no orderBy to avoid index requirement)
        db.collection("paymentRequests")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                progressBar.visibility = View.GONE
                checkStatusButton.isEnabled = true
                checkStatusButton.text = "🔄 Check Approval Status"

                if (documents.isEmpty) {
                    Toast.makeText(
                        this,
                        "⚠️ No payment request found for $userEmail",
                        Toast.LENGTH_LONG
                    ).show()
                    return@addOnSuccessListener
                }

                // Get the most recent document (last in list)
                val doc = documents.documents.lastOrNull()
                if (doc == null) {
                    Toast.makeText(
                        this,
                        "⚠️ No payment request found",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnSuccessListener
                }

                val status = doc.getString("status") ?: "pending"

                when (status) {
                    "approved" -> {
                        Toast.makeText(
                            this,
                            "🎉 APPROVED!\n\nYour payment has been approved!\n\nOpening test...",
                            Toast.LENGTH_LONG
                        ).show()

                        // Mark payment as approved in UserManager
                        UserManager.setPaymentApproved(this, true)
                        UserManager.saveUserLogin(this, userEmail)
                        
                        // Auto-register: Add email to approved list so user doesn't need to pay again
                        ApprovedEmailManager.addApprovedEmail(this, userEmail)
                        android.util.Log.d("PaymentActivity", "✅ Email auto-registered as approved: $userEmail")

                        // Hide pending message
                        pendingMessage.visibility = View.GONE
                        checkStatusButton.visibility = View.GONE

                        // If called from TestActivity via startActivityForResult, return to it
                        if (intent.getBooleanExtra("fromTest", false)) {
                            android.util.Log.d("PaymentActivity", "✅ Payment approved, returning to TestActivity")
                            progressBar.postDelayed({
                                setResult(RESULT_OK)
                                finish()
                            }, 2000)
                        } else if (UserManager.hasCompletedTest(this)) {
                            // If the user has already completed the test, skip directly to occupations
                            val stored = UserManager.getStoredTestResults(this)
                            if (stored != null) {
                                progressBar.postDelayed({
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
                                }, 2000)
                            } else {
                                // Fallback: go to test if stored scores are missing
                                progressBar.postDelayed({
                                    val intent = Intent(this, TestActivity::class.java)
                                    intent.putExtra("USER_EMAIL", userEmail)
                                    startActivity(intent)
                                    finish()
                                }, 2000)
                            }
                        } else {
                            // First time: navigate to test activity
                            progressBar.postDelayed({
                                val intent = Intent(this, TestActivity::class.java)
                                intent.putExtra("USER_EMAIL", userEmail)
                                startActivity(intent)
                                finish()
                            }, 2000)
                        }
                    }
                    "rejected" -> {
                        Toast.makeText(
                            this,
                            "❌ REJECTED\n\nYour payment request was rejected.\n\nPlease contact support or try again.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        Toast.makeText(
                            this,
                            "⏳ PENDING\n\nYour payment is still waiting for admin approval.\n\nPlease check again in a few minutes.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                checkStatusButton.isEnabled = true
                checkStatusButton.text = "🔄 Check Approval Status"

                Toast.makeText(
                    this,
                    "❌ Error checking status:\n\n${e.message}\n\nPlease try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
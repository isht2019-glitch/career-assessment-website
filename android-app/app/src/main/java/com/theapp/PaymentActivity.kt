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

                        // Hide pending message
                        pendingMessage.visibility = View.GONE
                        checkStatusButton.visibility = View.GONE

                        // Navigate to test activity
                        progressBar.postDelayed({
                            val intent = Intent(this, TestActivity::class.java)
                            intent.putExtra("USER_EMAIL", userEmail)
                            startActivity(intent)
                            finish()
                        }, 2000)
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
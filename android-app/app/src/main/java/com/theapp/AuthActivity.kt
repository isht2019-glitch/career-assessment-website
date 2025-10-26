package com.theapp

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// Google Play Services and Firebase imports commented out due to missing dependencies
// import com.google.android.gms.auth.api.signin.GoogleSignIn
// import com.google.android.gms.auth.api.signin.GoogleSignInAccount
// import com.google.android.gms.auth.api.signin.GoogleSignInClient
// import com.google.android.gms.auth.api.signin.GoogleSignInOptions
// import com.google.android.gms.common.api.ApiException
// import com.google.firebase.auth.FirebaseAuth
// import com.google.firebase.auth.GoogleAuthProvider
import com.theapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAuthBinding
    // Firebase and Google Sign-In variables commented out due to missing dependencies
    // private lateinit var auth: FirebaseAuth
    // private lateinit var googleSignInClient: GoogleSignInClient
    // private val RC_SIGN_IN = 9001
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initializeAuth()
        setupClickListeners()
        
        // Initialize Velly Bandaar guide system with consistent behavior
        initializeAuthGuide()
    }
    
    private fun initializeAuth() {
        // Firebase Auth and Google Sign-In initialization commented out due to missing dependencies
        // auth = FirebaseAuth.getInstance()
        // val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        //     .requestIdToken("24433567597-lq2b2947kgbegh2cmrln30eieik99grq.apps.googleusercontent.com")
        //     .requestEmail()
        //     .build()
        // googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    
    private fun setupClickListeners() {
        // Email/Password Sign Up - simplified without Firebase
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            
            if (validateSignUpForm(email, password, confirmPassword)) {
                // Simulate successful signup
                showSuccess("Account created for $email")
                saveLoginStatus(email)
                navigateToPayment()
            }
        }
        
        // Email/Password Sign In - simplified without Firebase
        binding.btnSignIn.setOnClickListener {
            val email = binding.etSignInEmail.text.toString().trim()
            val password = binding.etSignInPassword.text.toString().trim()
            
            if (validateSignInForm(email, password)) {
                // Simulate successful signin
                showSuccess("Signed in as $email")
                saveLoginStatus(email)
                navigateToPayment()
            }
        }
        
        // Google Sign-In - simplified without Google Play Services
        binding.btnGoogleSignIn.setOnClickListener {
            // Simulate Google sign-in with placeholder email
            val email = "google.user@gmail.com" // Placeholder for Google sign-in
            showSuccess("Google Sign-In successful")
            saveLoginStatus(email)
            navigateToPayment()
        }
        
        // Phone OTP - simplified
        binding.btnPhoneAuth.setOnClickListener {
            val phone = binding.etPhone.text.toString().trim()
            if (phone.isNotEmpty()) {
                showSuccess("OTP sent to $phone")
                // Save phone as email placeholder
                saveLoginStatus("$phone@phone.user")
                navigateToPayment()
            } else {
                showError("Please enter your phone number")
            }
        }
        
        // Toggle between Sign Up and Sign In
        binding.tvSwitchToSignIn.setOnClickListener {
            toggleAuthMode(false)
        }
        
        binding.tvSwitchToSignUp.setOnClickListener {
            toggleAuthMode(true)
        }
    }
    
    private fun validateSignUpForm(email: String, password: String, confirmPassword: String): Boolean {
        when {
            email.isEmpty() -> {
                showError("Please enter your email")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showError("Please enter a valid email")
                return false
            }
            password.isEmpty() -> {
                showError("Please enter a password")
                return false
            }
            password.length < 8 -> {
                showError("Password must be at least 8 characters")
                return false
            }
            password != confirmPassword -> {
                showError("Passwords do not match")
                return false
            }
        }
        return true
    }
    
    private fun validateSignInForm(email: String, password: String): Boolean {
        when {
            email.isEmpty() -> {
                showError("Please enter your email")
                return false
            }
            password.isEmpty() -> {
                showError("Please enter your password")
                return false
            }
        }
        return true
    }
    
    // Firebase email signup commented out due to missing dependencies
    // private fun signUpWithEmail(email: String, password: String) {
    //     auth.createUserWithEmailAndPassword(email, password)
    //         .addOnCompleteListener(this) { task ->
    //             if (task.isSuccessful) {
    //                 showSuccess("Account created for $email")
    //                 saveLoginStatus()
    //                 navigateToPayment()
    //             } else {
    //                 showError("Sign up failed: ${task.exception?.message}")
    //             }
    //         }
    // }
    
    // Firebase email signin commented out due to missing dependencies
    // private fun signInWithEmail(email: String, password: String) {
    //     auth.signInWithEmailAndPassword(email, password)
    //         .addOnCompleteListener(this) { task ->
    //             if (task.isSuccessful) {
    //                 showSuccess("Signed in as $email")
    //                 saveLoginStatus()
    //                 navigateToPayment()
    //             } else {
    //                 showError("Sign in failed: ${task.exception?.message}")
    //             }
    //         }
    // }
    
    // Google Sign-In methods commented out due to missing Google Play Services dependencies
    // private fun signInWithGoogle() {
    //     val signInIntent = googleSignInClient.signInIntent
    //     startActivityForResult(signInIntent, RC_SIGN_IN)
    // }
    // 
    // override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    //     super.onActivityResult(requestCode, resultCode, data)
    //     
    //     if (requestCode == RC_SIGN_IN) {
    //         val task = GoogleSignIn.getSignedInAccountFromIntent(data)
    //         try {
    //             val account = task.getResult(ApiException::class.java)!!
    //             firebaseAuthWithGoogle(account.idToken!!)
    //         } catch (e: ApiException) {
    //             showError("Google sign in failed: Code ${e.statusCode}, ${e.message}")
    //         }
    //     }
    // }
    // 
    // private fun firebaseAuthWithGoogle(idToken: String) {
    //     val credential = GoogleAuthProvider.getCredential(idToken, null)
    //     auth.signInWithCredential(credential)
    //         .addOnCompleteListener(this) { task ->
    //             if (task.isSuccessful) {
    //                 val user = auth.currentUser
    //                 showSuccess("Google Sign-In successful: ${user?.email}")
    //                 saveLoginStatus()
    //                 navigateToPayment()
    //             } else {
    //                 showError("Authentication failed: ${task.exception?.message}")
    //             }
    //         }
    // }
    
    private fun toggleAuthMode(isSignUp: Boolean) {
        if (isSignUp) {
            binding.layoutSignUp.visibility = android.view.View.VISIBLE
            binding.layoutSignIn.visibility = android.view.View.GONE
        } else {
            binding.layoutSignUp.visibility = android.view.View.GONE
            binding.layoutSignIn.visibility = android.view.View.VISIBLE
        }
    }
    
    private fun saveLoginStatus(email: String = "") {
        // Use centralized UserManager for cleaner code
        UserManager.saveUserLogin(this, email)
    }
    
    private fun navigateToPayment() {
        // Navigate to payment screen
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnSignUp.isEnabled = !show
        binding.btnSignIn.isEnabled = !show
        binding.btnGoogleSignIn.isEnabled = !show
    }
    
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun initializeAuthGuide() {
        // Create Velly Bandaar guide for auth/sign-up page
        val rootView = findViewById<FrameLayout>(android.R.id.content)
        val authGuide = AuthGuideSystem(this, rootView)
        authGuide.init()
    }
}

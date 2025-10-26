package com.theapp

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

class PaymentGuideSystem(private val context: Context, private val parentView: ViewGroup) {
    
    private var currentStep = 0
    private var isActive = false
    
    private lateinit var overlay: View
    private lateinit var characterView: ImageView
    private lateinit var speechBubble: LinearLayout
    private lateinit var speechContent: LinearLayout
    private lateinit var typingIndicator: LinearLayout
    
    private val handler = Handler(Looper.getMainLooper())
    
    fun init() {
        createGuideElements()
        
        // Start guide automatically and immediately for payment page
        handler.postDelayed({
            startGuide()
        }, 100)
    }
    
    private fun createGuideElements() {
        // Create overlay
        overlay = View(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#B3000000"))
            visibility = View.GONE
            setOnClickListener { hideGuide() }
        }
        parentView.addView(overlay)
        
        // Create character - consistent positioning
        characterView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                dpToPx(90),
                dpToPx(90)
            ).apply {
                gravity = Gravity.TOP or Gravity.END
                setMargins(0, dpToPx(60), dpToPx(15), 0)
            }
            setImageResource(R.drawable.velly_bandaar_jpg)
            scaleType = ImageView.ScaleType.CENTER_CROP
            
            // Add border and shadow effect
            background = ContextCompat.getDrawable(context, R.drawable.character_background)
            
            visibility = View.GONE
            setOnClickListener { 
                if (!isActive) startGuide()
            }
        }
        parentView.addView(characterView)
        
        // Create speech bubble - consistent positioning
        createSpeechBubble()
        
        // Start bounce animation for character
        startBounceAnimation()
    }
    
    private fun createSpeechBubble() {
        speechBubble = LinearLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                dpToPx(300),
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                setMargins(dpToPx(15), dpToPx(170), dpToPx(15), 0)
            }
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20))
            
            // Create gradient background
            background = createSpeechBubbleBackground()
            visibility = View.GONE
        }
        
        // Close button
        val closeButton = TextView(context).apply {
            text = "×"
            textSize = 20f
            setTextColor(Color.WHITE)
            gravity = Gravity.END
            setOnClickListener { hideGuide() }
        }
        speechBubble.addView(closeButton)
        
        // Content container
        speechContent = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        speechBubble.addView(speechContent)
        
        // Typing indicator
        createTypingIndicator()
        speechBubble.addView(typingIndicator)
        
        parentView.addView(speechBubble)
    }
    
    private fun createSpeechBubbleBackground(): GradientDrawable {
        return GradientDrawable().apply {
            colors = intArrayOf(
                Color.parseColor("#667eea"),
                Color.parseColor("#764ba2")
            )
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.TL_BR
            cornerRadius = dpToPx(20).toFloat()
            setStroke(dpToPx(2), Color.parseColor("#764ba2"))
        }
    }
    
    private fun createTypingIndicator() {
        typingIndicator = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            visibility = View.GONE
            
            // Add three dots
            repeat(3) { index ->
                val dot = View(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        dpToPx(8),
                        dpToPx(8)
                    ).apply {
                        setMargins(dpToPx(2), 0, dpToPx(2), 0)
                    }
                    background = GradientDrawable().apply {
                        shape = GradientDrawable.OVAL
                        setColor(Color.parseColor("#FFD700"))
                    }
                }
                addView(dot)
                
                // Animate dots
                val animator = ObjectAnimator.ofFloat(dot, "translationY", 0f, -dpToPx(10).toFloat(), 0f)
                animator.duration = 1400
                animator.repeatCount = ObjectAnimator.INFINITE
                animator.startDelay = (index * 200).toLong()
                animator.start()
            }
        }
    }
    
    private fun startGuide() {
        isActive = true
        currentStep = 0
        showGuide()
        showStep(0)
    }
    
    private fun showGuide() {
        overlay.visibility = View.VISIBLE
        characterView.visibility = View.VISIBLE
        speechBubble.visibility = View.VISIBLE
    }
    
    private fun hideGuide() {
        overlay.visibility = View.GONE
        speechBubble.visibility = View.GONE
        isActive = false
    }
    
    private fun showCharacter() {
        characterView.visibility = View.VISIBLE
    }
    
    private fun showTyping(duration: Long = 800, callback: () -> Unit) {
        speechContent.visibility = View.GONE
        typingIndicator.visibility = View.VISIBLE
        
        handler.postDelayed({
            typingIndicator.visibility = View.GONE
            speechContent.visibility = View.VISIBLE
            callback()
        }, duration)
    }
    
    private fun showStep(step: Int) {
        showTyping(800) {
            speechContent.removeAllViews()
            
            when (step) {
                0 -> showPaymentIntroStep()
                1 -> showPlanSelectionStep()
            }
        }
    }
    
    private fun showPaymentIntroStep() {
        // Payment introduction text
        addTextView("👋 Hello! I'm Velly Bandaar, your career guide!", "#FF6B35", true)
        addTextView("To access the career assessment test, there's a small fee of just ₹1! 💰", "#FFFFFF")
        addTextView("Don't worry - the payment process is super simple! 😊", "#FFD700")
        addTextView("Let me explain how it works...", "#FFFFFF")
        
        // Continue button
        val continueBtn = createButton("Yes, tell me! ➡️", "#4CAF50") {
            currentStep = 1
            showStep(1)
        }
        speechContent.addView(continueBtn)
    }
    
    private fun showPlanSelectionStep() {
        showTyping(1000) {
            speechContent.removeAllViews()
            
            addTextView("💸 Payment Process - Only ₹1! 💸", "#FF6B35", true)
            addTextView("", "#FFFFFF") // Spacer
            
            addTextView("🔹 Step 1: Enter your email below", "#FFD700", true)
            addTextView("   We need this to send you payment details", "#FFFFFF")
            addTextView("", "#FFFFFF") // Spacer
            
            addTextView("🔹 Step 2: Check your email inbox", "#FFD700", true)
            addTextView("   You'll receive a QR code & UPI ID from our team", "#FFFFFF")
            addTextView("", "#FFFFFF") // Spacer
            
            addTextView("🔹 Step 3: Pay ₹1 using any UPI app", "#FFD700", true)
            addTextView("   Google Pay, PhonePe, Paytm - any app works!", "#FFFFFF")
            addTextView("", "#FFFFFF") // Spacer
            
            addTextView("🔹 Step 4: Start your test immediately!", "#FFD700", true)
            addTextView("   No need to wait - begin your assessment now", "#FFFFFF")
            addTextView("   Payment can be completed within 24 hours", "#FFFFFF")
            addTextView("", "#FFFFFF") // Spacer
            
            addTextView("✨ That's it! Simple, safe, and secure! ✨", "#FF6B35", true)
            
            val startBtn = createButton("🚀 Got it! Let's proceed", "#4CAF50") {
                completeGuide()
            }
            speechContent.addView(startBtn)
        }
    }
    
    private fun addTextView(text: String, color: String = "#FFFFFF", bold: Boolean = false) {
        val textView = TextView(context).apply {
            this.text = text
            textSize = 14f
            setTextColor(Color.parseColor(color))
            if (bold) {
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(10)
            }
        }
        speechContent.addView(textView)
    }
    
    private fun createButton(text: String, color: String, onClick: () -> Unit): Button {
        return Button(context).apply {
            this.text = text
            setTextColor(Color.WHITE)
            background = GradientDrawable().apply {
                setColor(Color.parseColor(color))
                cornerRadius = dpToPx(8).toFloat()
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dpToPx(10)
            }
            setOnClickListener { onClick() }
        }
    }
    
    private fun startBounceAnimation() {
        val bounceAnimator = ObjectAnimator.ofFloat(characterView, "translationY", 0f, -dpToPx(10).toFloat(), 0f)
        bounceAnimator.duration = 2000
        bounceAnimator.repeatCount = ObjectAnimator.INFINITE
        bounceAnimator.start()
    }
    
    private fun completeGuide() {
        hideGuide()
        
        // Hide character after completion
        handler.postDelayed({
            characterView.visibility = View.GONE
        }, 500)
        
        // Store completion status
        val prefs = context.getSharedPreferences("guide_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("payment_guide_completed", true)
            .apply()
    }
    
    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}

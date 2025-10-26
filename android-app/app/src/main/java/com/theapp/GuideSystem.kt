package com.theapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

class GuideSystem(private val context: Context, private val parentView: ViewGroup) {
    
    private var currentStep = 0
    private var userName = ""
    private var userChoice = ""
    private var isActive = false
    
    private lateinit var overlay: View
    private lateinit var characterView: ImageView
    private lateinit var speechBubble: LinearLayout
    private lateinit var speechContent: LinearLayout
    private lateinit var typingIndicator: LinearLayout
    
    private val handler = Handler(Looper.getMainLooper())
    
    fun init() {
        createGuideElements()
        
        // Start guide immediately if not completed
        handler.postDelayed({
            if (shouldShowGuide()) {
                startGuide()
            } else {
                showCharacter()
            }
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
        
        // Create character
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
        
        // Create speech bubble
        createSpeechBubble()
        
        // Start bounce animation for character
        startBounceAnimation()
    }
    
    private fun createSpeechBubble() {
        speechBubble = LinearLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                dpToPx(280),
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
    
    private fun showTyping(duration: Long = 2000, callback: () -> Unit) {
        speechContent.visibility = View.GONE
        typingIndicator.visibility = View.VISIBLE
        
        handler.postDelayed({
            typingIndicator.visibility = View.GONE
            speechContent.visibility = View.VISIBLE
            callback()
        }, duration)
    }
    
    private fun showStep(step: Int) {
        showTyping(1500) {
            speechContent.removeAllViews()
            
            when (step) {
                0 -> showIntroStep()
                1 -> showPaymentStep()
                2 -> showFinalStep()
            }
        }
    }
    
    private fun showIntroStep() {
        // Introduction text
        addTextView("Hi! My name is \"Velly Bandaar\"! 🐵", "#FF6B35", true)
        addTextView("I work for the gang called \"Badmash Patandar Samajvadi Party\" 💀", "#FFD700", true)
        addTextView("This group helps in illumination of the world. ✨")
        addTextView("What's your name? 🤔")
        
        // Name input
        val nameInput = EditText(context).apply {
            hint = "Enter your name..."
            setHintTextColor(Color.GRAY)
            setTextColor(Color.BLACK)
            background = createInputBackground()
            setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10))
            filters = arrayOf(InputFilter.LengthFilter(30))
        }
        speechContent.addView(nameInput)
        
        // Continue button
        val continueBtn = createButton("Tell Velly", "#4CAF50") {
            val name = nameInput.text.toString().trim()
            if (name.length >= 2) {
                userName = name
                currentStep = 1
                showStep(1)
            } else {
                nameInput.error = "Please enter a valid name!"
            }
        }
        speechContent.addView(continueBtn)
        
        nameInput.requestFocus()
    }
    
    private fun showPaymentStep() {
        showTyping(2000) {
            speechContent.removeAllViews()
            
            addTextView("I don't care who you are. 😤")
            addTextView("But you will be someone because you have come on \"TheApp\" - a byproduct of my gang! 🚀", "#FF6B35")
            addTextView("First you need to pay a small amount. Then I will instruct you more. 💰")
            
            // Choice buttons
            val buttonContainer = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = dpToPx(15)
                }
            }
            
            val yesBtn = createButton("Yes", "#FF6B35") {
                userChoice = "yes"
                currentStep = 2
                showStep(2)
            }
            yesBtn.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                rightMargin = dpToPx(10)
            }
            
            val obeyBtn = createButton("I will obey", "#FFD700") {
                userChoice = "obey"
                currentStep = 2
                showStep(2)
            }
            obeyBtn.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            obeyBtn.setTextColor(Color.parseColor("#333333"))
            
            buttonContainer.addView(yesBtn)
            buttonContainer.addView(obeyBtn)
            speechContent.addView(buttonContainer)
        }
    }
    
    private fun showFinalStep() {
        showTyping(1500) {
            speechContent.removeAllViews()
            
            val response = when (userChoice) {
                "yes" -> "You need to work hard! 🔥"
                "obey" -> "You are on the right path! ✅"
                else -> "Good choice!"
            }
            
            addTextView("$response 💪")
            addTextView("Now proceed with your registration and remember - Velly Bandaar is watching! 👁️")
            
            val startBtn = createButton("Start Registration", "#4CAF50") {
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
    
    private fun createInputBackground(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor("#F0FFFFFF"))
            cornerRadius = dpToPx(8).toFloat()
            setStroke(dpToPx(1), Color.parseColor("#CCCCCC"))
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
            .putBoolean("velly_guide_completed", true)
            .putString("user_name", userName)
            .apply()
    }
    
    private fun shouldShowGuide(): Boolean {
        val prefs = context.getSharedPreferences("guide_prefs", Context.MODE_PRIVATE)
        return !prefs.getBoolean("velly_guide_completed", false)
    }
    
    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}

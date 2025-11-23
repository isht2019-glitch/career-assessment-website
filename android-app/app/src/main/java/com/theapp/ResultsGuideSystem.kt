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

class ResultsGuideSystem(private val context: Context, private val parentView: ViewGroup) {
    
    private var currentStep = 0
    private var isActive = false
    
    private lateinit var overlay: View
    private lateinit var characterView: ImageView
    private lateinit var speechBubble: LinearLayout
    private lateinit var speechContent: LinearLayout
    private lateinit var typingIndicator: LinearLayout
    
    private val handler = Handler(Looper.getMainLooper())
    
    fun init() {
        android.util.Log.d("ResultsGuideSystem", "🐵 Initializing Results Guide System")
        createGuideElements()
        
        // Reset guide completion flag for results page (always show)
        val prefs = context.getSharedPreferences("guide_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("results_guide_completed", false).apply()
        
        // Start guide automatically and immediately for results page
        handler.postDelayed({
            android.util.Log.d("ResultsGuideSystem", "🐵 Delayed start - calling startGuide()")
            startGuide()
        }, 100)
    }
    
    private fun createGuideElements() {
        android.util.Log.d("ResultsGuideSystem", "🐵 Creating guide elements, parentView type: ${parentView.javaClass.simpleName}")
        
        // Create overlay
        overlay = View(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#B3000000"))
            visibility = View.GONE
            setOnClickListener { hideGuide() }
        }
        parentView.addView(overlay)
        android.util.Log.d("ResultsGuideSystem", "🐵 Overlay added to parent")
        
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
        android.util.Log.d("ResultsGuideSystem", "🐵 Character view added to parent")
        
        // Create speech bubble
        createSpeechBubble()
        
        // Start bounce animation for character
        startBounceAnimation()
    }
    
    private fun createSpeechBubble() {
        android.util.Log.d("ResultsGuideSystem", "🐵 Creating speech bubble")
        
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
        android.util.Log.d("ResultsGuideSystem", "🐵 Speech bubble added to parent")
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
        android.util.Log.d("ResultsGuideSystem", "🐵 Starting Results Guide")
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
                0 -> showIntroductionStep()
                1 -> showSatisfactionStep()
                2 -> showReportStep()
                3 -> showFeedbackStep()
            }
        }
    }
    
    private fun showIntroductionStep() {
        // Introduction from Velly Bandaar
        addTextView("Hi! I am Velly Bandaar 🐵", "#FF6B35", true)
        addTextView("Member of Badmaash Patandaar Samajhwadi Party 💀", "#FFD700", true)
        addTextView("Our party works on illumination of humanity ✨", "#FFFFFF")
        
        // Continue button
        val continueBtn = createButton("Continue", "#4CAF50") {
            currentStep = 1
            showStep(1)
        }
        speechContent.addView(continueBtn)
    }
    
    private fun showSatisfactionStep() {
        showTyping(800) {
            speechContent.removeAllViews()
            
            addTextView("Did your result was satisfactory? 🤔", "#FF6B35", true)
            
            // Button container for Yes/No
            val buttonContainer = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = dpToPx(15)
                }
            }
            
            val yesBtn = createButton("Yes ✅", "#4CAF50") {
                currentStep = 2
                showStep(2)
            }
            yesBtn.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                rightMargin = dpToPx(10)
            }
            
            val noBtn = createButton("No ❌", "#FF6B35") {
                // Show feedback message
                currentStep = 3
                showStep(3)
            }
            noBtn.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            
            buttonContainer.addView(yesBtn)
            buttonContainer.addView(noBtn)
            speechContent.addView(buttonContainer)
        }
    }
    
    private fun showReportStep() {
        showTyping(800) {
            speechContent.removeAllViews()
            
            addTextView("Now you can see your personalized report! 📊", "#FF6B35", true)
            addTextView("On the basis of my data, I gave you few occupation options. 💼", "#FFFFFF")
            addTextView("Click on them to see the roadmap! 🗺️", "#FFD700", true)
            
            val startBtn = createButton("Got it! Show me careers", "#4CAF50") {
                completeGuide()
            }
            speechContent.addView(startBtn)
        }
    }
    
    private fun showFeedbackStep() {
        addTextView("Thank you for your feedback! 🙏", "#FF6B35", true)
        addTextView("We appreciate your input and will work to improve! 💪", "#FFFFFF")
        addTextView("You can email us at helper@theapp.work for detailed feedback.", "#FFD700")
        
        val closeBtn = createButton("Got it!", "#4CAF50") {
            completeGuide()
        }
        speechContent.addView(closeBtn)
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
            .putBoolean("results_guide_completed", true)
            .apply()
    }
    
    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}

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

class PersonalityGuideSystem(
    private val context: Context, 
    private val parentView: ViewGroup,
    private val riasecScores: Map<String, Int>
) {
    
    private lateinit var overlay: View
    private lateinit var characterView: ImageView
    private lateinit var speechBubble: LinearLayout
    private lateinit var speechContent: LinearLayout
    private lateinit var typingIndicator: LinearLayout
    
    private val handler = Handler(Looper.getMainLooper())
    
    fun showFirstAnalysis(onComplete: () -> Unit) {
        try {
            android.util.Log.d("PersonalityGuide", "💡 STARTING showFirstAnalysis")
            android.util.Log.d("PersonalityGuide", "Scores received: $riasecScores")
            
            // Create guide elements
            createGuideElements()
            android.util.Log.d("PersonalityGuide", "Guide elements created")
            
            // Show guide immediately
            showGuide()
            android.util.Log.d("PersonalityGuide", "Guide shown (overlay, character, bubble)")
            
            // Analyze first 10 questions
            val dominantType = findDominantType()
            val personalityInsight = getPersonalityInsight(dominantType)
            val scoreBreakdown = buildScoreBreakdown()
            
            android.util.Log.d("PersonalityGuide", "Dominant type: $dominantType")
            android.util.Log.d("PersonalityGuide", "Insight: $personalityInsight")
            
            // Show typing indicator, then content
            showTyping(800) {
                android.util.Log.d("PersonalityGuide", "Building speech content...")
                speechContent.removeAllViews()
                
                addTextView("🎉 Analysis #1: First 10 Questions", "#FF6B35", true)
                addTextView("", "#FFFFFF") // Spacer
                addTextView("📊 Initial Personality Traits:", "#FFD700", true)
                addTextView(personalityInsight, "#FFFFFF")
                addTextView("", "#FFFFFF") // Spacer
                addTextView("🎯 Your RIASEC Scores So Far:", "#FFD700", true)
                addTextView(scoreBreakdown, "#FFFFFF")
                addTextView("", "#FFFFFF") // Spacer
                addTextView("Let's continue! 10 more questions will give us a clearer picture! 🚀", "#FFD700")
                
                val continueBtn = createButton("Continue to Questions 11-20 ➡️", "#4CAF50") {
                    android.util.Log.d("PersonalityGuide", "➡️ Continue button clicked")
                    hideGuide()
                    onComplete()
                }
                speechContent.addView(continueBtn)
                android.util.Log.d("PersonalityGuide", "✅ Analysis #1 content displayed")
            }
        } catch (e: Exception) {
            android.util.Log.e("PersonalityGuide", "❌ ERROR in showFirstAnalysis", e)
            e.printStackTrace()
            onComplete()
        }
    }
    
    fun showSecondAnalysis(onComplete: () -> Unit) {
        try {
            android.util.Log.d("PersonalityGuide", "Showing analysis #2 (Q1-20). Scores: $riasecScores")
            createGuideElements()
            showGuide()
            
            // Analyze first 20 questions
            val dominantType = findDominantType()
            val secondaryType = findSecondaryType()
            val personalityProfile = getDetailedPersonalityProfile(dominantType)
            val scoreBreakdown = buildScoreBreakdown()
            
            android.util.Log.d("PersonalityGuide", "Dominant: $dominantType, Secondary: $secondaryType after Q1-20")
            
            showTyping(1200) {
                speechContent.removeAllViews()
                
                addTextView("🌟 Analysis #2: First 20 Questions", "#FF6B35", true)
                addTextView("", "#FFFFFF") // Spacer
                addTextView("📊 Your Personality Profile is Emerging:", "#FFD700", true)
                addTextView(personalityProfile, "#FFFFFF")
                addTextView("", "#FFFFFF") // Spacer
                addTextView("🎯 Current RIASEC Scores:", "#FFD700", true)
                addTextView(scoreBreakdown, "#FFFFFF")
                addTextView("", "#FFFFFF") // Spacer
                addTextView("Almost there! Just 10 more questions for your complete profile! 🎯", "#FFD700")
                
                val continueBtn = createButton("Continue to Questions 21-30 ➡️", "#4CAF50") {
                    android.util.Log.d("PersonalityGuide", "Proceeding to Q21-30")
                    hideGuide()
                    onComplete()
                }
                speechContent.addView(continueBtn)
            }
        } catch (e: Exception) {
            android.util.Log.e("PersonalityGuide", "Error showing second analysis", e)
            onComplete()
        }
    }
    
    fun showCompletePersonalityAnalysis(onComplete: () -> Unit) {
        try {
            android.util.Log.d("PersonalityGuide", "Showing complete personality analysis (30 questions). Scores: $riasecScores")
            createGuideElements()
            showGuide()
            
            // Analyze all 30 personality questions
            val dominantType = findDominantType()
            val secondaryType = findSecondaryType()
            val personalityProfile = getDetailedPersonalityProfile(dominantType)
            
            android.util.Log.d("PersonalityGuide", "Dominant type: $dominantType, Secondary: $secondaryType")
            
            showTyping(1500) {
                speechContent.removeAllViews()
                
                addTextView("🎉 Fantastic! You've completed all 30 personality questions!", "#FF6B35", true)
                addTextView("", "#FFFFFF") // Spacer
                addTextView("📊 Your Complete Personality Analysis:", "#FFD700", true)
                addTextView(personalityProfile, "#FFFFFF")
                addTextView("", "#FFFFFF") // Spacer
                
                // Show score breakdown
                addTextView("🎯 Your RIASEC Scores:", "#FFD700", true)
                val scoreText = buildScoreBreakdown()
                addTextView(scoreText, "#FFFFFF")
                
                addTextView("", "#FFFFFF") // Spacer
                addTextView("🧠 Next: Let's test your aptitude with 20 questions!", "#FFD700", true)
                addTextView("This will help us match you with perfect career options! 🚀", "#FFFFFF")
                
                val continueBtn = createButton("Start Aptitude Test ➡️", "#4CAF50") {
                    android.util.Log.d("PersonalityGuide", "Aptitude test button clicked")
                    hideGuide()
                    onComplete()
                }
                speechContent.addView(continueBtn)
            }
        } catch (e: Exception) {
            android.util.Log.e("PersonalityGuide", "Error showing complete analysis", e)
            onComplete() // Continue even if guide fails
        }
    }
    
    private fun findSecondaryType(): String {
        val sortedScores = riasecScores.entries.sortedByDescending { it.value }
        return if (sortedScores.size > 1) sortedScores[1].key else "A"
    }
    
    private fun buildScoreBreakdown(): String {
        val sortedScores = riasecScores.entries.sortedByDescending { it.value }
        return sortedScores.joinToString("\n") { 
            val typeName = getTypeFullName(it.key)
            "${it.key} ($typeName): ${it.value} points"
        }
    }
    
    private fun getTypeFullName(type: String): String {
        return when (type) {
            "R" -> "Realistic"
            "I" -> "Investigative"
            "A" -> "Artistic"
            "S" -> "Social"
            "E" -> "Enterprising"
            "C" -> "Conventional"
            else -> "Unknown"
        }
    }
    
    private fun findDominantType(): String {
        return riasecScores.maxByOrNull { it.value }?.key ?: "A"
    }
    
    private fun getPersonalityInsight(type: String): String {
        return when (type) {
            "R" -> "Realistic - You prefer hands-on, practical activities! 🔧"
            "I" -> "Investigative - You love analyzing and solving complex problems! 🔬"
            "A" -> "Artistic - You're drawn to creative and expressive activities! 🎨"
            "S" -> "Social - You enjoy helping and working with people! 🤝"
            "E" -> "Enterprising - You like leading and influencing others! 💼"
            "C" -> "Conventional - You prefer organized, structured environments! 📋"
            else -> "Balanced - You show traits across multiple personality types! ⚖️"
        }
    }
    
    private fun getDetailedPersonalityProfile(type: String): String {
        return when (type) {
            "R" -> "🔧 Realistic Type: You're practical, hands-on, and prefer working with tools, machines, or in outdoor environments. You value concrete results and tangible outcomes."
            "I" -> "🔬 Investigative Type: You're analytical, curious, and enjoy research. You like to understand how things work and solve complex problems through systematic thinking."
            "A" -> "🎨 Artistic Type: You're creative, expressive, and value originality. You enjoy activities that allow for self-expression and innovation in unstructured environments."
            "S" -> "🤝 Social Type: You're people-oriented, empathetic, and enjoy helping others. You thrive in collaborative environments and value meaningful relationships."
            "E" -> "💼 Enterprising Type: You're ambitious, persuasive, and enjoy leadership roles. You like to influence others and are comfortable with risk-taking and competition."
            "C" -> "📋 Conventional Type: You're organized, detail-oriented, and prefer structured environments. You value accuracy, efficiency, and clear procedures."
            else -> "⚖️ Balanced Type: You show a mix of personality traits, making you adaptable to various environments and career paths."
        }
    }
    
    private fun createGuideElements() {
        try {
            android.util.Log.d("PersonalityGuide", "Creating overlay...")
            // Create overlay
            overlay = View(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.parseColor("#B3000000"))
                visibility = View.GONE
                elevation = 100f  // Bring to front
            }
            parentView.addView(overlay)
            android.util.Log.d("PersonalityGuide", "Overlay added to parentView")
            
            android.util.Log.d("PersonalityGuide", "Creating character...")
            // Create character
            characterView = ImageView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    dpToPx(90),
                    dpToPx(90)
                ).apply {
                    gravity = Gravity.TOP or Gravity.END
                    setMargins(0, dpToPx(60), dpToPx(15), 0)
                }
                try {
                    setImageResource(R.drawable.velly_bandaar_jpg)
                } catch (e: Exception) {
                    android.util.Log.w("PersonalityGuide", "Character image not found, using placeholder")
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
                visibility = View.GONE
                elevation = 101f  // Above overlay
            }
            parentView.addView(characterView)
            android.util.Log.d("PersonalityGuide", "Character added to parentView")
            
            // Create speech bubble
            android.util.Log.d("PersonalityGuide", "Creating speech bubble...")
            createSpeechBubble()
            android.util.Log.d("PersonalityGuide", "Speech bubble created")
            
            // Start bounce animation for character
            startBounceAnimation()
        } catch (e: Exception) {
            android.util.Log.e("PersonalityGuide", "ERROR creating guide elements", e)
            e.printStackTrace()
        }
    }
    
    private fun createSpeechBubble() {
        speechBubble = LinearLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                dpToPx(340),
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                setMargins(dpToPx(20), dpToPx(120), dpToPx(20), dpToPx(120))
            }
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20))
            
            // Create gradient background
            background = createSpeechBubbleBackground()
            visibility = View.GONE
            elevation = 102f  // Above everything
        }
        
        // Close button
        val closeButton = TextView(context).apply {
            text = "×"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.END
            setPadding(dpToPx(10), 0, 0, 0)
            setOnClickListener { 
                android.util.Log.d("PersonalityGuide", "Close button clicked")
                hideGuide() 
            }
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
        android.util.Log.d("PersonalityGuide", "Speech bubble added to parentView")
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
    
    private fun showGuide() {
        android.util.Log.d("PersonalityGuide", "Setting guide views to VISIBLE")
        overlay.visibility = View.VISIBLE
        android.util.Log.d("PersonalityGuide", "Overlay visible: ${overlay.visibility == View.VISIBLE}")
        characterView.visibility = View.VISIBLE
        android.util.Log.d("PersonalityGuide", "Character visible: ${characterView.visibility == View.VISIBLE}")
        speechBubble.visibility = View.VISIBLE
        android.util.Log.d("PersonalityGuide", "Speech bubble visible: ${speechBubble.visibility == View.VISIBLE}")
        
        // Bring views to front
        overlay.bringToFront()
        characterView.bringToFront()
        speechBubble.bringToFront()
        parentView.invalidate()
        
        android.util.Log.d("PersonalityGuide", "✅ All guide views are now VISIBLE")
    }
    
    private fun hideGuide() {
        android.util.Log.d("PersonalityGuide", "🔄 Hiding guide and cleaning up views...")
        
        try {
            // Hide views first
            overlay.visibility = View.GONE
            speechBubble.visibility = View.GONE
            characterView.visibility = View.GONE
            
            android.util.Log.d("PersonalityGuide", "Views hidden, removing from parent...")
            
            // Remove views from parent safely
            try { parentView.removeView(overlay) } catch (e: Exception) { 
                android.util.Log.w("PersonalityGuide", "Overlay already removed")
            }
            try { parentView.removeView(characterView) } catch (e: Exception) { 
                android.util.Log.w("PersonalityGuide", "Character already removed")
            }
            try { parentView.removeView(speechBubble) } catch (e: Exception) { 
                android.util.Log.w("PersonalityGuide", "Speech bubble already removed")
            }
            
            // Force parent to refresh
            parentView.invalidate()
            parentView.requestLayout()
            
            android.util.Log.d("PersonalityGuide", "✅ Guide fully removed and cleaned up")
        } catch (e: Exception) {
            android.util.Log.e("PersonalityGuide", "Error hiding guide", e)
        }
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
                topMargin = dpToPx(15)
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
    
    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}

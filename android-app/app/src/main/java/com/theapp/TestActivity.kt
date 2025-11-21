package com.theapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.theapp.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTestBinding
    private var currentQuestion = 0
    private var answers = mutableListOf<Int?>()
    private var timeRemaining = 50 * 60 * 1000L // 50 minutes for 50 questions (30 personality + 20 aptitude)
    private var timer: CountDownTimer? = null
    private var currentPhase = 0 // 0: Q1-10, 1: Analysis#1, 2: Q11-20, 3: Analysis#2, 4: Q21-30, 5: Analysis#3, 6: Aptitude, 7: Results
    private var phaseAnswers = mutableListOf<Int?>()
    private var allPersonalityAnswers = mutableListOf<Int?>() // Store all 30 personality answers
    private var aptitudeAnswers = mutableListOf<Int?>()
    private var userEmail: String = ""
    private var paymentScreenShown = false // Track if payment screen has been shown
    
    // RIASEC Questions (30 total - improved with distinct options)
    private val riasecQuestions = listOf(
        Question("At a party, you typically:", 
            listOf("Mingle and meet new people", "Stick with people you know", "Observe from the sidelines", "Leave early"),
            listOf("E", "S", "I", "C")),
        Question("When faced with a problem, you:",
            listOf("Jump in and fix it immediately", "Think it through carefully", "Ask others for advice", "Try unconventional approaches"),
            listOf("R", "I", "S", "A")),
        Question("In a team project, you're the one who:",
            listOf("Takes charge and delegates", "Ensures everyone gets along", "Does detailed technical work", "Comes up with creative ideas"),
            listOf("E", "S", "I", "A")),
        Question("Your ideal job would involve:",
            listOf("Working with machines/tools", "Analyzing data/information", "Helping/teaching people", "Creating new things"),
            listOf("R", "I", "S", "A")),
        Question("When learning something new, you prefer to:",
            listOf("Get hands-on experience", "Read detailed instructions", "Learn from others", "Experiment and discover"),
            listOf("R", "C", "S", "A")),
        Question("In conflicts, you typically:",
            listOf("Stand your ground firmly", "Look for compromise", "Avoid confrontation", "Find creative solutions"),
            listOf("E", "C", "I", "A")),
        Question("You feel most energized by:",
            listOf("Social interaction", "Quiet reflection", "Solving complex problems", "Creating something new"),
            listOf("E", "I", "I", "A")),
        Question("Your work style is:",
            listOf("Fast-paced and action-oriented", "Methodical and organized", "Collaborative and supportive", "Flexible and adaptive"),
            listOf("R", "C", "S", "A")),
        Question("When something goes wrong, you:",
            listOf("Take immediate action", "Analyze what happened", "Comfort those affected", "Think of better alternatives"),
            listOf("R", "I", "S", "A")),
        Question("Your biggest strength is:",
            listOf("Getting things done", "Attention to detail", "Understanding people", "Thinking outside the box"),
            listOf("E", "C", "S", "A")),
        Question("You'd rather spend time:",
            listOf("Leading a group activity", "Working independently", "In meaningful conversations", "Pursuing a passion project"),
            listOf("E", "I", "S", "A")),
        Question("When making decisions, you rely on:",
            listOf("Gut instinct", "Logical analysis", "What others think", "Creative intuition"),
            listOf("E", "I", "S", "A")),
        Question("Your ideal work environment is:",
            listOf("Bustling and social", "Quiet and structured", "Collaborative and open", "Inspiring and unconventional"),
            listOf("E", "C", "S", "A")),
        Question("You're most motivated by:",
            listOf("Recognition and status", "Mastery and competence", "Making a difference", "Freedom and autonomy"),
            listOf("E", "I", "S", "A")),
        Question("When faced with rules, you:",
            listOf("Follow them precisely", "Question if they make sense", "Adapt as needed", "Ignore if they limit you"),
            listOf("C", "I", "S", "A")),
        Question("Your communication style is:",
            listOf("Outgoing and persuasive", "Precise and logical", "Empathetic and supportive", "Creative and expressive"),
            listOf("E", "I", "S", "A")),
        Question("You handle stress by:",
            listOf("Talking it out with others", "Working through it alone", "Taking a break", "Channeling it productively"),
            listOf("S", "I", "C", "A")),
        Question("Your natural talent is:",
            listOf("Organizing people", "Understanding systems", "Connecting with others", "Generating ideas"),
            listOf("E", "I", "S", "A")),
        Question("When learning fails, you:",
            listOf("Try again with more effort", "Study the problem deeper", "Ask for help", "Try a completely different approach"),
            listOf("E", "I", "S", "A")),
        Question("Your ideal vacation involves:",
            listOf("Adventure and activities", "Relaxation and peace", "Visiting cultural sites", "Exploring new places"),
            listOf("R", "C", "S", "A")),
        Question("In a meeting, you're likely to:",
            listOf("Speak up first", "Listen carefully", "Build consensus", "Suggest innovations"),
            listOf("E", "I", "S", "A")),
        Question("You value in others:",
            listOf("Confidence and drive", "Intelligence and knowledge", "Kindness and loyalty", "Creativity and originality"),
            listOf("E", "I", "S", "A")),
        Question("Your biggest fear is:",
            listOf("Being ignored", "Making mistakes", "Hurting others", "Being trapped"),
            listOf("E", "I", "S", "A")),
        Question("You're most productive when:",
            listOf("Deadline is near", "Everything is organized", "Working with a team", "Doing something you love"),
            listOf("R", "C", "S", "A")),
        Question("Your approach to life is:",
            listOf("Seize opportunities", "Plan carefully", "Help others", "Embrace possibilities"),
            listOf("E", "C", "S", "A")),
        Question("When given a choice, you'd rather:",
            listOf("Lead the group", "Work independently", "Support the team", "Do something unique"),
            listOf("E", "I", "S", "A")),
        Question("Your ideal Friday night is:",
            listOf("Out with friends", "Quiet at home", "Helping someone", "Pursuing a hobby"),
            listOf("E", "I", "S", "A")),
        Question("You handle criticism by:",
            listOf("Defending yourself", "Analyzing it objectively", "Feeling hurt but accepting", "Using it to improve"),
            listOf("E", "I", "S", "A")),
        Question("Your work motivation comes from:",
            listOf("Advancement and success", "Solving interesting problems", "Making people happy", "Personal fulfillment"),
            listOf("E", "I", "S", "A")),
        Question("When things are uncertain, you:",
            listOf("Take charge and decide", "Wait for more information", "Seek others' input", "Trust your instincts"),
            listOf("E", "I", "S", "A"))
    )
    
    // Aptitude questions (exactly 20 questions - improved with distinct options)
    private val aptitudeQuestions = listOf(
        Question("If 8 workers build a wall in 12 days, how many workers are needed to build it in 6 days?",
            listOf("8 workers", "16 workers", "24 workers", "32 workers"), correctAnswer = 1),
        Question("A train travels 240 km in 3 hours. What is its speed?",
            listOf("60 km/h", "80 km/h", "100 km/h", "120 km/h"), correctAnswer = 1),
        Question("If 25% of a number is 60, what is the full number?",
            listOf("120", "180", "240", "300"), correctAnswer = 2),
        Question("The ratio of boys to girls is 3:2. If there are 15 boys, how many girls?",
            listOf("5 girls", "10 girls", "15 girls", "20 girls"), correctAnswer = 1),
        Question("A shopkeeper sells an item for ₹450 at 10% profit. Cost price was?",
            listOf("₹350", "₹409", "₹450", "₹495"), correctAnswer = 1),
        Question("Find the next number: 2, 6, 12, 20, 30, ?",
            listOf("36", "40", "42", "48"), correctAnswer = 2),
        Question("Complete the series: A, D, G, J, ?",
            listOf("K", "M", "N", "P"), correctAnswer = 1),
        Question("Find the missing number: 5, 11, 23, 47, ?",
            listOf("71", "85", "95", "109"), correctAnswer = 3),
        Question("What comes next: 1, 4, 9, 16, 25, ?",
            listOf("30", "35", "36", "49"), correctAnswer = 2),
        Question("Complete: 3, 7, 15, 31, ?",
            listOf("47", "59", "63", "79"), correctAnswer = 2),
        Question("All roses are flowers. Some flowers are red. What can we conclude?",
            listOf("All roses are red", "Some roses might be red", "No roses are red", "Roses are not flowers"), correctAnswer = 1),
        Question("If BOOK is coded as CPPL, how is WORD coded?",
            listOf("XPSE", "XQSE", "YPSE", "YQSE"), correctAnswer = 1),
        Question("In code: RAIN = 1234. How is GAIN written?",
            listOf("1234", "5234", "6234", "7234"), correctAnswer = 1),
        Question("If Monday is day 1, what day is the 15th?",
            listOf("Friday", "Monday", "Sunday", "Wednesday"), correctAnswer = 2),
        Question("A is taller than B. C is shorter than B. Who is shortest?",
            listOf("A", "B", "C", "Cannot say"), correctAnswer = 2),
        Question("Synonym of 'Abundant':",
            listOf("Scarce", "Plentiful", "Minimal", "Rare"), correctAnswer = 1),
        Question("Antonym of 'Optimistic':",
            listOf("Happy", "Hopeful", "Pessimistic", "Confident"), correctAnswer = 2),
        Question("Book : Author :: Painting : ?",
            listOf("Canvas", "Brush", "Artist", "Gallery"), correctAnswer = 2),
        Question("Correctly spelled word:",
            listOf("Recieve", "Receive", "Receve", "Receiv"), correctAnswer = 1),
        Question("5 machines make 5 widgets in 5 minutes. 100 machines make 100 widgets in?",
            listOf("5 minutes", "20 minutes", "100 minutes", "500 minutes"), correctAnswer = 0)
    )
    
    private val allQuestions = riasecQuestions + aptitudeQuestions
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityTestBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            // Initialize answers list safely
            answers = MutableList(allQuestions.size) { null }
            
            initializeTest()
            setupClickListeners()
            
            // Initialize Velly Bandaar guide for test page with error handling
            try {
                initializeTestGuide()
            } catch (e: Exception) {
                android.util.Log.e("TestActivity", "Error initializing guide", e)
            }
            
            // Delay heavy operations to prevent ANR
            binding.root.postDelayed({
                startTimer()
                // displayCurrentPhaseQuestions() will be called by startPhase(0) in initializeTest()
            }, 100)
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "Error in onCreate", e)
            Toast.makeText(this, "Error initializing test. Please try again.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun initializeTest() {
        try {
            // Get user email from UserManager (already collected in PaymentActivity)
            userEmail = UserManager.getUserEmail(this) ?: "guest@theapp.com"
            
            // Initialize for phase-based testing
            currentPhase = 0
            phaseAnswers = mutableListOf()
            allPersonalityAnswers = MutableList(30) { null } // 30 personality questions
            
            android.util.Log.d("TestActivity", "Test initialized for user: $userEmail")
            
            // Start with first 10 personality questions
            startPhase(0)
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "Error in initializeTest", e)
        }
    }
    
    private fun setupClickListeners() {
        try {
            // Make submit button visible and clickable
            binding.btnSubmit?.visibility = View.VISIBLE
            binding.btnSubmit?.setOnClickListener { 
                try {
                    val answered = phaseAnswers.count { it != null }
                    val total = getCurrentPhaseQuestions().size
                    
                    android.util.Log.d("TestActivity", "🖱️ BUTTON CLICKED!")
                    android.util.Log.d("TestActivity", "  Phase: $currentPhase")
                    android.util.Log.d("TestActivity", "  Answered: $answered/$total")
                    android.util.Log.d("TestActivity", "  Button enabled: ${binding.btnSubmit?.isEnabled}")
                    android.util.Log.d("TestActivity", "  Button clickable: ${binding.btnSubmit?.isClickable}")
                    
                    handlePhaseSubmission()
                } catch (e: Exception) {
                    android.util.Log.e("TestActivity", "❌ ERROR in phase submission", e)
                    Toast.makeText(this, "Error: ${e.message}. Tap button again.", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
            
            android.util.Log.d("TestActivity", "✅ Click listeners set up successfully")
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "❌ ERROR setting up click listeners", e)
            e.printStackTrace()
        }
    }
    
    private fun startTimer() {
        timer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                updateTimerDisplay()
            }
            
            override fun onFinish() {
                // Auto-submit current phase when time runs out
                if (currentPhase == 4) {
                    // If in aptitude phase, submit final test
                    storeAptitudeAnswers()
                    startPhase(5)
                } else {
                    // Otherwise, force advance to next phase
                    handlePhaseSubmission()
                }
            }
        }.start()
    }
    
    private fun updateTimerDisplay() {
        val minutes = (timeRemaining / 1000) / 60
        val seconds = (timeRemaining / 1000) % 60
        binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
        
        // Change color when time is running low
        when {
            timeRemaining <= 5 * 60 * 1000 -> binding.tvTimer.setTextColor(getColor(android.R.color.holo_red_dark))
            timeRemaining <= 15 * 60 * 1000 -> binding.tvTimer.setTextColor(getColor(android.R.color.holo_orange_dark))
        }
    }
    
    private fun displayCurrentPhaseQuestions() {
        binding.questionsContainer.removeAllViews()
        
        val questions = getCurrentPhaseQuestions()
        android.util.Log.d("TestActivity", "Displaying ${questions.size} questions for phase $currentPhase")
        
        if (questions.isEmpty()) {
            android.util.Log.e("TestActivity", "ERROR: No questions for phase $currentPhase!")
            Toast.makeText(this, "Error: No questions available", Toast.LENGTH_LONG).show()
            return
        }
        
        questions.forEachIndexed { index, question ->
            val questionCard = createQuestionCard(index, question)
            binding.questionsContainer.addView(questionCard)
        }
        
        updatePhaseProgress()
    }
    
    private fun getCurrentPhaseQuestions(): List<Question> {
        val questions = when (currentPhase) {
            0 -> riasecQuestions.take(10) // Questions 1-10
            2 -> riasecQuestions.drop(10).take(10) // Questions 11-20
            4 -> riasecQuestions.drop(20).take(10) // Questions 21-30
            6 -> aptitudeQuestions // 20 aptitude questions
            else -> emptyList()
        }
        android.util.Log.d("TestActivity", "getCurrentPhaseQuestions for phase $currentPhase: ${questions.size} questions")
        android.util.Log.d("TestActivity", "Total riasecQuestions available: ${riasecQuestions.size}")
        if (currentPhase == 2 && questions.isNotEmpty()) {
            android.util.Log.d("TestActivity", "Phase 2 first question: ${questions[0].text}")
        }
        return questions
    }
    
    private fun startPhase(phase: Int) {
        android.util.Log.d("TestActivity", "⚡ startPhase called with phase: $phase")
        android.util.Log.d("TestActivity", "Current phase before change: $currentPhase")
        
        currentPhase = phase
        
        android.util.Log.d("TestActivity", "Current phase after change: $currentPhase")
        
        val questions = getCurrentPhaseQuestions()
        phaseAnswers = MutableList(questions.size) { null }
        
        android.util.Log.d("TestActivity", "✅ Starting Phase: $phase with ${questions.size} questions")
        android.util.Log.d("TestActivity", "PhaseAnswers initialized with size: ${phaseAnswers.size}")
        
        when (phase) {
            0 -> {
                android.util.Log.d("TestActivity", "➡️ Loading Questions 1-10")
                binding.tvProgress.text = "Personality Set 1: Questions (1-10)"
                binding.btnSubmit?.visibility = View.VISIBLE
                binding.questionsContainer.visibility = View.VISIBLE
                displayCurrentPhaseQuestions()
                android.util.Log.d("TestActivity", "✅ Questions 1-10 loaded")
            }
            1 -> {
                android.util.Log.d("TestActivity", "Showing analysis after first 10")
                binding.btnSubmit?.visibility = View.GONE
                
                // Show analysis in a dialog instead of overlay to avoid view conflicts
                showFirstAnalysisDialog()
            }
            2 -> {
                android.util.Log.d("TestActivity", "➡️ PHASE 2: Loading Questions 11-20")
                
                runOnUiThread {
                    Toast.makeText(this, "Loading Questions 11-20...", Toast.LENGTH_LONG).show()
                    
                    // Force all UI elements visible
                    binding.tvProgress.text = "Personality Set 2: Questions (11-20)"
                    binding.tvProgress.visibility = View.VISIBLE
                    binding.tvTimer.visibility = View.VISIBLE
                    binding.progressBar?.visibility = View.VISIBLE
                    binding.btnSubmit?.visibility = View.VISIBLE
                    
                    // Clear and prepare questions container
                    binding.questionsContainer.removeAllViews()
                    binding.questionsContainer.visibility = View.VISIBLE
                    
                    // Force parent layout visible
                    binding.root.visibility = View.VISIBLE
                    binding.root.requestLayout()
                    
                    android.util.Log.d("TestActivity", "Loading phase 2 questions...")
                    
                    displayCurrentPhaseQuestions()
                    
                    android.util.Log.d("TestActivity", "Phase 2 questions displayed: ${binding.questionsContainer.childCount} views")
                    
                    // Force UI refresh
                    binding.root.invalidate()
                    binding.questionsContainer.invalidate()
                    
                    android.util.Log.d("TestActivity", "✅ Questions 11-20 loaded: ${getCurrentPhaseQuestions().size} questions")
                    
                    if (binding.questionsContainer.childCount == 0) {
                        android.util.Log.e("TestActivity", "❌ ERROR: NO VIEWS IN CONTAINER!")
                        Toast.makeText(this, "ERROR: Questions not loading. Please restart test.", Toast.LENGTH_LONG).show()
                    } else {
                        android.util.Log.d("TestActivity", "✅ SUCCESS: ${binding.questionsContainer.childCount} views in container")
                    }
                }
            }
            3 -> {
                android.util.Log.d("TestActivity", "Showing analysis after second 10")
                binding.btnSubmit?.visibility = View.GONE
                showSecondAnalysisDialog()
            }
            4 -> {
                android.util.Log.d("TestActivity", "➡️ Loading Questions 21-30")
                Toast.makeText(this, "Loading Questions 21-30...", Toast.LENGTH_SHORT).show()
                binding.tvProgress.text = "Personality Set 3: Questions (21-30)"
                binding.btnSubmit?.visibility = View.VISIBLE
                binding.questionsContainer.removeAllViews() // Clear any previous content
                binding.questionsContainer.visibility = View.VISIBLE // Ensure container is visible
                displayCurrentPhaseQuestions()
                android.util.Log.d("TestActivity", "✅ Questions 21-30 loaded: ${getCurrentPhaseQuestions().size} questions")
            }
            5 -> {
                android.util.Log.d("TestActivity", "Showing complete personality analysis")
                binding.btnSubmit?.visibility = View.GONE
                showCompleteAnalysisDialog()
            }
            6 -> {
                android.util.Log.d("TestActivity", "➡️ Loading Aptitude Questions 1-20")
                Toast.makeText(this, "Loading Aptitude Test...", Toast.LENGTH_SHORT).show()
                binding.tvProgress.text = "Aptitude Test: Questions (1-20)"
                binding.btnSubmit?.visibility = View.VISIBLE
                binding.questionsContainer.removeAllViews() // Clear any previous content
                binding.questionsContainer.visibility = View.VISIBLE // Ensure container is visible
                displayCurrentPhaseQuestions()
                android.util.Log.d("TestActivity", "✅ Aptitude questions loaded: ${getCurrentPhaseQuestions().size} questions")
            }
            7 -> {
                android.util.Log.d("TestActivity", "Submitting final test")
                submitFinalTest()
            }
        }
    }
    
    private fun createQuestionCard(index: Int, question: Question): View {
        val cardView = androidx.cardview.widget.CardView(this)
        cardView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 32)
        }
        cardView.radius = 48f
        cardView.cardElevation = 24f
        
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(64, 64, 64, 64)
        
        // Question number and text
        val questionNumber = TextView(this)
        questionNumber.text = "Question ${index + 1}"
        questionNumber.textSize = 14f
        questionNumber.setTextColor(getColor(R.color.purple_500))
        questionNumber.setTypeface(null, android.graphics.Typeface.BOLD)
        
        val questionText = TextView(this)
        questionText.text = question.text
        questionText.textSize = 18f
        questionText.setTypeface(null, android.graphics.Typeface.BOLD)
        questionText.setPadding(0, 32, 0, 48)
        
        linearLayout.addView(questionNumber)
        linearLayout.addView(questionText)
        
        // Options with gun emoji
        question.options.forEachIndexed { optionIndex, optionText ->
            val optionButton = TextView(this)
            optionButton.text = optionText
            optionButton.textSize = 16f
            optionButton.setPadding(48, 32, 48, 32)
            optionButton.setTextColor(getColor(android.R.color.black))
            optionButton.setBackgroundResource(R.drawable.option_background)
            optionButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
            
            optionButton.setOnClickListener {
                selectOption(index, optionIndex)
                updateOptionDisplay(linearLayout, optionIndex)
            }
            
            linearLayout.addView(optionButton)
        }
        
        cardView.addView(linearLayout)
        return cardView
    }
    
    private fun updateOptionDisplay(parentLayout: LinearLayout, selectedIndex: Int) {
        // Update all option buttons in this question
        for (i in 2 until parentLayout.childCount) { // Skip question number and text
            val optionButton = parentLayout.getChildAt(i) as TextView
            val originalText = optionButton.text.toString().replace(" 🔫", "")
            
            if (i - 2 == selectedIndex) {
                optionButton.text = "$originalText 🔫"
                optionButton.setBackgroundColor(getColor(R.color.purple_200))
            } else {
                optionButton.text = originalText
                optionButton.setBackgroundResource(R.drawable.option_background)
            }
        }
    }
    
    private fun selectOption(questionIndex: Int, optionIndex: Int) {
        phaseAnswers[questionIndex] = optionIndex
        android.util.Log.d("TestActivity", "Selected option $optionIndex for question $questionIndex. Phase: $currentPhase")
        android.util.Log.d("TestActivity", "PhaseAnswers: ${phaseAnswers.count { it != null }}/${phaseAnswers.size}")
        updatePhaseProgress()
    }
    
    private fun updatePhaseProgress() {
        val answeredQuestions = phaseAnswers.count { it != null }
        val totalQuestions = getCurrentPhaseQuestions().size
        
        android.util.Log.d("TestActivity", "updatePhaseProgress: Phase=$currentPhase, Answered=$answeredQuestions, Total=$totalQuestions")
        
        // Safety check
        if (totalQuestions == 0) {
            android.util.Log.e("TestActivity", "ERROR: totalQuestions is 0! Phase: $currentPhase")
            return
        }
        
        binding.progressBar?.max = totalQuestions
        binding.progressBar?.progress = answeredQuestions
        
        val phaseText = when (currentPhase) {
            0 -> "Personality Set 1: $answeredQuestions/10 answered"
            2 -> "Personality Set 2: $answeredQuestions/10 answered"
            4 -> "Personality Set 3: $answeredQuestions/10 answered"
            6 -> "Aptitude Test: $answeredQuestions/20 answered"
            else -> "$answeredQuestions/$totalQuestions answered"
        }
        
        binding.tvProgress.text = phaseText
        
        // Check if phase is complete
        if (answeredQuestions >= totalQuestions && totalQuestions > 0) {
            // Enable submit button and update text
            binding.btnSubmit?.text = when (currentPhase) {
                0 -> "See Analysis #1 🎯"
                2 -> "See Analysis #2 🎯"
                4 -> "See Complete Analysis 🎯"
                6 -> "Submit Test 🚀"
                else -> "Continue"
            }
            binding.btnSubmit?.isEnabled = true
            binding.btnSubmit?.isClickable = true
            binding.btnSubmit?.isFocusable = true
            binding.btnSubmit?.visibility = View.VISIBLE
            binding.btnSubmit?.alpha = 1.0f
            
            android.util.Log.d("TestActivity", "✅ Phase $currentPhase complete! Button enabled and clickable.")
            Toast.makeText(this, "All questions answered! Tap the button below.", Toast.LENGTH_SHORT).show()
        } else {
            // Show button but keep it semi-transparent when disabled
            binding.btnSubmit?.text = when (currentPhase) {
                0 -> "Answer all 10 for analysis (${answeredQuestions}/10)"
                2 -> "Answer all 10 for analysis (${answeredQuestions}/10)"
                4 -> "Answer all 10 for analysis (${answeredQuestions}/10)"
                6 -> "Answer all 20 to submit (${answeredQuestions}/20)"
                else -> "Answer all questions"
            }
            binding.btnSubmit?.isEnabled = false
            binding.btnSubmit?.isClickable = false
            binding.btnSubmit?.visibility = View.VISIBLE
            binding.btnSubmit?.alpha = 0.5f
        }
    }
    
    
    private fun handlePhaseSubmission() {
        val answeredQuestions = phaseAnswers.count { it != null }
        val totalQuestions = getCurrentPhaseQuestions().size
        
        android.util.Log.d("TestActivity", "Handling submission for phase $currentPhase: $answeredQuestions/$totalQuestions answered")
        
        // Validate all questions are answered
        if (answeredQuestions < totalQuestions) {
            Toast.makeText(this, "Please answer all questions before continuing", Toast.LENGTH_SHORT).show()
            return
        }
        
        when (currentPhase) {
            0 -> {
                // Store first 10 answers
                storePhaseAnswers(0, 10)
                Toast.makeText(this, "Great! Let's see your initial personality traits...", Toast.LENGTH_SHORT).show()
                startPhase(1) // Show first analysis
            }
            2 -> {
                // Store second 10 answers
                storePhaseAnswers(10, 20)
                Toast.makeText(this, "Excellent! Your personality is becoming clearer...", Toast.LENGTH_SHORT).show()
                startPhase(3) // Show second analysis
            }
            4 -> {
                // Store third 10 answers
                storePhaseAnswers(20, 30)
                Toast.makeText(this, "Perfect! Here's your complete personality profile...", Toast.LENGTH_SHORT).show()
                android.util.Log.d("TestActivity", "🔴 PHASE 4 COMPLETE: Moving to phase 5 (complete analysis dialog)")
                startPhase(5) // Show complete analysis dialog
            }
            6 -> {
                // Store aptitude answers and submit final test
                storeAptitudeAnswers()
                Toast.makeText(this, "Test complete! Calculating your results...", Toast.LENGTH_SHORT).show()
                android.util.Log.d("TestActivity", "🔴 PHASE 6 COMPLETE: Moving to phase 7 (results)")
                startPhase(7) // Submit final test
            }
        }
    }
    
    private fun storePhaseAnswers(startIndex: Int, endIndex: Int) {
        // Store answers in the global personality answers list
        for (i in 0 until phaseAnswers.size) {
            val globalIndex = startIndex + i
            if (globalIndex < allPersonalityAnswers.size) {
                allPersonalityAnswers[globalIndex] = phaseAnswers[i]
            }
        }
        android.util.Log.d("TestActivity", "Stored answers from $startIndex to $endIndex")
    }
    
    private fun storeAptitudeAnswers() {
        // Store aptitude answers for final calculation
        aptitudeAnswers = phaseAnswers.toMutableList()
    }
    
    private fun showFirstAnalysisDialog() {
        try {
            val riasecScores = calculatePartialScores(0, 10)
            android.util.Log.d("TestActivity", "💡 First analysis dialog (Q1-10). RIASEC: $riasecScores")
            
            val dominantType = riasecScores.maxByOrNull { it.value }?.key ?: "A"
            val dominantScore = riasecScores[dominantType] ?: 0
            
            val message = buildString {
                append("🎉 Analysis #1: First 10 Questions\n\n")
                append("📊 Your Initial Personality Traits:\n\n")
                append("Dominant Type: $dominantType\n")
                append("Score: $dominantScore points\n\n")
                append("🎯 RIASEC Breakdown:\n")
                riasecScores.entries.sortedByDescending { it.value }.forEach {
                    append("${it.key}: ${it.value} points\n")
                }
                append("\nLet's continue with 10 more questions! 🚀")
            }
            
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Personality Analysis #1")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Continue to Questions 11-20") { dialog, _ ->
                    dialog.dismiss()
                    android.util.Log.d("TestActivity", "Dialog dismissed, starting phase 2")
                    startPhase(2)
                }
                .create()
            
            dialog.show()
            
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "ERROR showing first analysis dialog", e)
            e.printStackTrace()
            Toast.makeText(this, "Analysis loading... Proceeding to next questions", Toast.LENGTH_SHORT).show()
            startPhase(2)
        }
    }
    
    private fun showFirstAnalysis() {
        try {
            // Hide questions and show analysis of first 10
            binding.questionsContainer.removeAllViews()
            
            // Calculate RIASEC scores from first 10 questions only
            val riasecScores = calculatePartialScores(0, 10)
            
            android.util.Log.d("TestActivity", "💡 First analysis (Q1-10). RIASEC: $riasecScores")
            android.util.Log.d("TestActivity", "allPersonalityAnswers size: ${allPersonalityAnswers.size}")
            
            if (riasecScores.values.sum() == 0) {
                android.util.Log.w("TestActivity", "Warning: No RIASEC scores calculated. Showing placeholder.")
            }
            
            // Get the root view of the activity (DecorView) for overlay
            val rootView = window.decorView.findViewById<FrameLayout>(android.R.id.content)
            android.util.Log.d("TestActivity", "Using rootView for overlay: ${rootView::class.simpleName}")
            
            val guideSystem = PersonalityGuideSystem(this, rootView, riasecScores)
            guideSystem.showFirstAnalysis {
                // Continue to second set of 10 questions
                android.util.Log.d("TestActivity", "✅ Analysis #1 complete, proceeding to phase 2 after cleanup...")
                
                // Delay to ensure proper UI cleanup and view removal
                binding.root.postDelayed({
                    android.util.Log.d("TestActivity", "🔄 Starting phase 2 now...")
                    startPhase(2)
                }, 500)
            }
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "ERROR showing first analysis", e)
            e.printStackTrace()
            Toast.makeText(this, "Analysis loading... Proceeding to next questions", Toast.LENGTH_SHORT).show()
            startPhase(2) // Continue anyway
        }
    }
    
    private fun showSecondAnalysisDialog() {
        try {
            val riasecScores = calculatePartialScores(0, 20)
            android.util.Log.d("TestActivity", "💡 Second analysis dialog (Q1-20). RIASEC: $riasecScores")
            
            val dominantType = riasecScores.maxByOrNull { it.value }?.key ?: "A"
            val secondaryType = riasecScores.entries.sortedByDescending { it.value }.getOrNull(1)?.key ?: "A"
            
            val message = buildString {
                append("🎉 Analysis #2: Questions 1-20\n\n")
                append("📊 Your Personality is Taking Shape!\n\n")
                append("Primary Type: $dominantType\n")
                append("Secondary Type: $secondaryType\n\n")
                append("🎯 Updated RIASEC Scores:\n")
                riasecScores.entries.sortedByDescending { it.value }.forEach {
                    append("${it.key}: ${it.value} points\n")
                }
                append("\n10 more questions for the complete picture! 🚀")
            }
            
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Personality Analysis #2")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Continue to Questions 21-30") { dialog, _ ->
                    dialog.dismiss()
                    android.util.Log.d("TestActivity", "Dialog dismissed, starting phase 4")
                    startPhase(4)
                }
                .create()
            
            dialog.show()
            
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "ERROR showing second analysis dialog", e)
            e.printStackTrace()
            Toast.makeText(this, "Analysis loading... Proceeding to next questions", Toast.LENGTH_SHORT).show()
            startPhase(4)
        }
    }
    
    private fun showSecondAnalysis() {
        try {
            // Hide questions and show analysis of first 20
            binding.questionsContainer.removeAllViews()
            
            // Calculate RIASEC scores from first 20 questions
            val riasecScores = calculatePartialScores(0, 20)
            
            android.util.Log.d("TestActivity", "💡 Second analysis (Q1-20). RIASEC: $riasecScores")
            
            // Use activity's content view
            val rootView = window.decorView.findViewById<FrameLayout>(android.R.id.content)
            
            val guideSystem = PersonalityGuideSystem(this, rootView, riasecScores)
            guideSystem.showSecondAnalysis {
                // Continue to third set of 10 questions
                android.util.Log.d("TestActivity", "✅ Analysis #2 complete, proceeding to phase 4 after cleanup...")
                
                // Delay to ensure proper UI cleanup
                binding.root.postDelayed({
                    android.util.Log.d("TestActivity", "🔄 Starting phase 4 now...")
                    startPhase(4)
                }, 500)
            }
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "ERROR showing second analysis", e)
            e.printStackTrace()
            Toast.makeText(this, "Analysis loading... Proceeding to next questions", Toast.LENGTH_SHORT).show()
            startPhase(4) // Continue anyway
        }
    }
    
    private fun showCompleteAnalysisDialog() {
        try {
            val riasecScores = calculatePersonalityScores()
            android.util.Log.d("TestActivity", "💡 Complete analysis dialog (Q1-30). RIASEC: $riasecScores")
            
            val dominantType = riasecScores.maxByOrNull { it.value }?.key ?: "A"
            val topThreeTypes = riasecScores.entries.sortedByDescending { it.value }.take(3)
            
            val typeNames = mapOf(
                "R" to "Realistic", "I" to "Investigative", "A" to "Artistic",
                "S" to "Social", "E" to "Enterprising", "C" to "Conventional"
            )
            
            val message = buildString {
                append("🎉 Complete Personality Analysis!\n\n")
                append("📊 Your Full RIASEC Profile:\n\n")
                append("🏆 PRIMARY TYPE: ${typeNames[dominantType]}\n\n")
                append("Top 3 Traits:\n")
                topThreeTypes.forEachIndexed { index, entry ->
                    append("${index + 1}. ${typeNames[entry.key]}: ${entry.value} points\n")
                }
                append("\n🎯 Complete Breakdown:\n")
                riasecScores.entries.sortedByDescending { it.value }.forEach {
                    append("${typeNames[it.key]}: ${it.value} points\n")
                }
                append("\nNow let's test your aptitude! 🎯")
            }
            
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("✨ Complete Personality Profile ✨")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Continue") { dialog, _ ->
                    dialog.dismiss()
                    android.util.Log.d("TestActivity", "🔴 Dialog dismissed, showing payment screen")
                    // Delay to ensure dialog is fully dismissed before showing payment screen
                    binding.root.postDelayed({
                        android.util.Log.d("TestActivity", "💳 NOW showing payment screen")
                        showPaymentScreenBeforeAptitude()
                    }, 300)
                }
                .create()
            
            dialog.show()
            
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "ERROR showing complete analysis dialog", e)
            e.printStackTrace()
            Toast.makeText(this, "Analysis loading... Proceeding to aptitude test", Toast.LENGTH_SHORT).show()
            startPhase(6)
        }
    }
    
    private fun showCompleteAnalysis() {
        try {
            // Hide questions and show complete analysis
            binding.questionsContainer.removeAllViews()
            
            // Calculate RIASEC scores from all 30 personality questions
            val riasecScores = calculatePersonalityScores()
            
            android.util.Log.d("TestActivity", "💡 Complete analysis (Q1-30). RIASEC: $riasecScores")
            android.util.Log.d("TestActivity", "Total personality answers: ${allPersonalityAnswers.count { it != null }}")
            
            // Use activity's content view
            val rootView = window.decorView.findViewById<FrameLayout>(android.R.id.content)
            
            val guideSystem = PersonalityGuideSystem(this, rootView, riasecScores)
            guideSystem.showCompletePersonalityAnalysis {
                // Continue to payment screen after guide
                android.util.Log.d("TestActivity", "✅ Complete analysis done, proceeding to payment screen after cleanup...")
                
                // Delay to ensure proper UI cleanup
                binding.root.postDelayed({
                    android.util.Log.d("TestActivity", "🔄 Starting payment screen now...")
                    showPaymentScreenBeforeAptitude()
                }, 500)
            }
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "ERROR showing complete analysis", e)
            e.printStackTrace()
            Toast.makeText(this, "Analysis loading... Proceeding to aptitude test", Toast.LENGTH_SHORT).show()
            startPhase(6) // Continue anyway
        }
    }
    
    private fun calculatePartialScores(startIndex: Int, endIndex: Int): MutableMap<String, Int> {
        val scores = mutableMapOf("R" to 0, "I" to 0, "A" to 0, "S" to 0, "E" to 0, "C" to 0)
        
        // Calculate scores from specified range of personality questions
        val personalityQuestions = riasecQuestions.take(30)
        
        for (i in startIndex until endIndex) {
            if (i < allPersonalityAnswers.size) {
                allPersonalityAnswers[i]?.let { selectedOption ->
                    val question = personalityQuestions[i]
                    if (question.riasecTypes != null && selectedOption < question.riasecTypes.size) {
                        val type = question.riasecTypes[selectedOption]
                        scores[type] = scores[type]!! + 1
                    }
                }
            }
        }
        
        return scores
    }
    
    private fun calculatePersonalityScores(): MutableMap<String, Int> {
        // Calculate scores from all 30 personality questions
        return calculatePartialScores(0, 30)
    }
    
    private fun submitFinalTest() {
        timer?.cancel()
        
        // Calculate final RIASEC scores from all 30 personality questions
        val riasecScores = calculatePersonalityScores()
        
        
        // Calculate aptitude score from stored aptitude answers
        var aptitudeCorrect = 0
        for (i in 0 until aptitudeQuestions.size) {
            if (i < aptitudeAnswers.size && aptitudeAnswers[i] == aptitudeQuestions[i].correctAnswer) {
                aptitudeCorrect++
            }
        }
        
        val aptitudePercentage = (aptitudeCorrect * 100) / aptitudeQuestions.size
        
        // Debug logging
        println("Final RIASEC Scores: $riasecScores")
        println("Aptitude Correct: $aptitudeCorrect out of ${aptitudeQuestions.size}")
        println("Aptitude Percentage: $aptitudePercentage%")
        
        // Find dominant RIASEC type
        val dominantScore = riasecScores.values.maxOrNull()!!
        val dominantType = riasecScores.entries.find { it.value == dominantScore }!!.key

        // Persist results so we can skip the test next time
        try {
            android.util.Log.d("TestActivity", "💾 Saving test results...")
            android.util.Log.d("TestActivity", "  RIASEC Scores: $riasecScores")
            android.util.Log.d("TestActivity", "  Aptitude Score: $aptitudePercentage")
            android.util.Log.d("TestActivity", "  Dominant Type: $dominantType")
            
            UserManager.saveTestResults(
                context = this,
                riasecScores = riasecScores,
                aptitudeScore = aptitudePercentage,
                dominantType = dominantType
            )
            
            // Mark payment as approved (user completed test after payment)
            UserManager.setPaymentApproved(this, true)
            android.util.Log.d("TestActivity", "✅ Payment status marked as approved")
            
            // Verify results were saved
            val saved = UserManager.getStoredTestResults(this)
            if (saved != null) {
                android.util.Log.d("TestActivity", "✅ Results saved successfully!")
                android.util.Log.d("TestActivity", "  Verified - Dominant Type: ${saved.dominantType}")
            } else {
                android.util.Log.e("TestActivity", "❌ Results not saved - verification failed")
            }
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "Error saving test results", e)
            e.printStackTrace()
        }

        // Pass results to OccupationSelectionActivity
        val intent = Intent(this, OccupationSelectionActivity::class.java)
        intent.putExtra("riasec_score", dominantScore)
        intent.putExtra("aptitude_score", aptitudePercentage)
        intent.putExtra("dominant_type", dominantType)
        // Pass individual RIASEC scores for dual personality detection
        intent.putExtra("r_score", riasecScores["R"] ?: 0)
        intent.putExtra("i_score", riasecScores["I"] ?: 0)
        intent.putExtra("a_score", riasecScores["A"] ?: 0)
        intent.putExtra("s_score", riasecScores["S"] ?: 0)
        intent.putExtra("e_score", riasecScores["E"] ?: 0)
        intent.putExtra("c_score", riasecScores["C"] ?: 0)
        startActivity(intent)
        finish()
    }
    
    private fun showPaymentScreenBeforeAptitude() {
        try {
            android.util.Log.d("TestActivity", "🔄 SHOWING PAYMENT SCREEN - Starting payment screen display")
            android.util.Log.d("TestActivity", "   binding.root is null? ${binding.root == null}")
            android.util.Log.d("TestActivity", "   binding.root visibility: ${binding.root.visibility}")
            
            // Hide all other views first
            binding.questionsContainer.visibility = View.GONE
            binding.btnSubmit?.visibility = View.GONE
            binding.tvProgress?.visibility = View.GONE
            android.util.Log.d("TestActivity", "   ✅ Hidden other views")
            
            // Create scrollable payment screen with high z-index
            val scrollView = android.widget.ScrollView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(getColor(android.R.color.white))
                elevation = 1000f // Ensure it's on top
            }
            android.util.Log.d("TestActivity", "   ✅ Created scrollView with elevation 1000f")
            
            val paymentView = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
                )
                setBackgroundColor(getColor(android.R.color.white))
                setPadding(32, 32, 32, 32)
            }
            android.util.Log.d("TestActivity", "   ✅ Created paymentView")
            
            scrollView.addView(paymentView)
            android.util.Log.d("TestActivity", "   ✅ Added paymentView to scrollView")

            // Title
            val titleView = TextView(this).apply {
                text = "🚀 Unlock Your Career Potential"
                textSize = 24f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(getColor(R.color.purple_500))
                setPadding(0, 0, 0, 24)
            }
            paymentView.addView(titleView)

            // Persuasive message card
            val messageCard = androidx.cardview.widget.CardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 24)
                }
                setCardBackgroundColor(getColor(R.color.purple_500))
                radius = 12f
            }
            
            val messageView = TextView(this).apply {
                text = "💡 This small amount can LEVITATE YOUR CAREER with key insights that transform your future!\n\n" +
                        "Get access to personalized career roadmaps, industry insights, and one-on-one conversation with our founder to guide your journey."
                textSize = 14f
                setTextColor(getColor(android.R.color.white))
                setPadding(16, 16, 16, 16)
                setLineSpacing(1.4f, 1.4f)
            }
            messageCard.addView(messageView)
            paymentView.addView(messageCard)

            // Price display
            val priceView = TextView(this).apply {
                text = "₹1"
                textSize = 48f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(getColor(R.color.purple_500))
                gravity = android.view.Gravity.CENTER
                setPadding(0, 0, 0, 32)
            }
            paymentView.addView(priceView)

            // Original price strikethrough
            val originalView = TextView(this).apply {
                text = "Originally ₹100"
                textSize = 14f
                setTextColor(getColor(android.R.color.darker_gray))
                gravity = android.view.Gravity.CENTER
                paintFlags = paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                setPadding(0, 0, 0, 48)
            }
            paymentView.addView(originalView)

            // Continue button
            val continueBtn = android.widget.Button(this).apply {
                text = "Continue to Aptitude Test"
                textSize = 16f
                setTextColor(getColor(android.R.color.white))
                setBackgroundColor(getColor(R.color.purple_500))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 16, 0, 16)
                }
                setPadding(0, 24, 0, 24)
                setOnClickListener {
                    try {
                        // Move to aptitude test (phase 6)
                        if (scrollView.parent != null) {
                            binding.root.removeView(scrollView)
                        }
                        android.util.Log.d("TestActivity", "Payment screen dismissed, starting aptitude phase 6")
                        startPhase(6)
                    } catch (e: Exception) {
                        android.util.Log.e("TestActivity", "Error removing payment view", e)
                        startPhase(6)
                    }
                }
            }
            paymentView.addView(continueBtn)

            // Add scrollView to main view
            android.util.Log.d("TestActivity", "   ✅ About to add scrollView to binding.root")
            binding.root.addView(scrollView)
            android.util.Log.d("TestActivity", "   ✅ ScrollView added to binding.root")
            android.util.Log.d("TestActivity", "✅✅✅ PAYMENT SCREEN DISPLAYED SUCCESSFULLY ✅✅✅")
        } catch (e: Exception) {
            android.util.Log.e("TestActivity", "❌❌❌ ERROR showing payment screen", e)
            android.util.Log.e("TestActivity", "Exception message: ${e.message}")
            android.util.Log.e("TestActivity", "Exception cause: ${e.cause}")
            e.printStackTrace()
            Toast.makeText(this, "ERROR: ${e.message}", Toast.LENGTH_LONG).show()
            startPhase(6)
        }
    }
    
    private fun showPaymentScreen() {
        // Deprecated - use showPaymentScreenBeforeAptitude instead
        showPaymentScreenBeforeAptitude()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
    
    private fun initializeTestGuide() {
        // Create Velly Bandaar guide for test page
        val rootView = findViewById<FrameLayout>(android.R.id.content)
        val testGuide = TestGuideSystem(this, rootView)
        testGuide.init()
    }

    data class Question(
        val text: String,
        val options: List<String>,
        val riasecTypes: List<String>? = null,
        val correctAnswer: Int? = null
    )
}

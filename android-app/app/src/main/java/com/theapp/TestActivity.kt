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
    
    // RIASEC Questions (50 total - we'll use first 30 for personality test)
    private val riasecQuestions = listOf(
        Question("When planning a group trip, what role do you take?", 
            listOf("Organize everything", "Share ideas but let others decide", "Follow the group's plan", "Question decisions if flawed"),
            listOf("E", "S", "C", "I")),
        Question("In an argument with someone unreasonable, what do you do?",
            listOf("Calmly explain my point", "Leave the conversation", "Debate strongly to win", "Use humor to diffuse tension"),
            listOf("S", "C", "E", "A")),
        Question("A team project deadline is near. How do you act?",
            listOf("Take charge and assign roles", "Help the team collaborate", "Follow instructions carefully", "Find innovative shortcuts"),
            listOf("E", "S", "C", "A")),
        Question("How do you prefer working?",
            listOf("Leading a team", "Working with others equally", "Working alone", "Taking flexible roles"),
            listOf("E", "S", "I", "A")),
        Question("Your friend is stressed about exams. What do you do?",
            listOf("Motivate with a pep talk", "Help them study", "Leave them to handle it", "Make them laugh"),
            listOf("S", "C", "I", "A")),
        Question("You are assigned a boring task. How do you handle it?",
            listOf("Try to make it interesting", "Ask for help or collaborate", "Complete it systematically", "Find ways to improve it"),
            listOf("A", "S", "C", "I")),
        Question("How do you make decisions?",
            listOf("Based on facts & data", "Based on what feels right", "Based on team consensus", "Based on established rules"),
            listOf("I", "A", "S", "C")),
        Question("When facing a tough challenge, your first thought is:",
            listOf("I'll find a new way", "I'll break it into steps", "I'll research solutions", "I'll get help from others"),
            listOf("A", "C", "I", "S")),
        Question("At a party, you are likely to:",
            listOf("Be the one telling stories", "Talk to a small group", "Observe and listen", "Help organize activities"),
            listOf("A", "S", "I", "C")),
        Question("You are given a creative assignment. You:",
            listOf("Come up with unique ideas", "Research deeply before starting", "Collaborate with others", "Follow a structured approach"),
            listOf("A", "I", "S", "C")),
        Question("How do you handle last-minute changes?",
            listOf("Stay flexible and adapt", "Try to minimize disruptions", "Analyze the impact first", "Discuss with the team"),
            listOf("A", "C", "I", "S")),
        Question("When learning something new, you prefer:",
            listOf("Experimenting hands-on", "Reading and analyzing deeply", "Learning with others", "Following step-by-step guides"),
            listOf("R", "I", "S", "C")),
        Question("Your ideal work environment is:",
            listOf("Creative and free-flowing", "Collaborative and social", "Quiet and focused", "Organized and structured"),
            listOf("A", "S", "I", "C")),
        Question("How do you react to failure?",
            listOf("Look for creative alternatives", "Analyze what went wrong", "Seek support from others", "Review and follow procedures"),
            listOf("A", "I", "S", "C")),
        Question("You get a sudden leadership opportunity. You:",
            listOf("Take charge confidently", "Support the leader", "Plan carefully before acting", "Follow established protocols"),
            listOf("E", "S", "I", "C")),
        Question("When solving problems, your approach is:",
            listOf("Analytical and step-by-step", "Creative and outside-the-box", "Collaborative with others", "Systematic and methodical"),
            listOf("I", "A", "S", "C")),
        Question("You are best at:",
            listOf("Coming up with ideas", "Following plans and rules", "Understanding complex concepts", "Working with people"),
            listOf("A", "C", "I", "S")),
        Question("How do you handle conflicts?",
            listOf("Find a compromise", "Use logic to resolve", "Think creatively for solutions", "Follow proper procedures"),
            listOf("S", "I", "A", "C")),
        Question("You prefer to spend free time:",
            listOf("Exploring hobbies", "Reading/learning", "Socializing with friends", "Organizing personal tasks"),
            listOf("A", "I", "S", "C")),
        Question("Your dream job involves:",
            listOf("Creating something new", "Leading a team", "Researching and discovering", "Helping others"),
            listOf("A", "E", "I", "S")),
        Question("When deadlines approach, you:",
            listOf("Innovate to save time", "Follow the process carefully", "Analyze priorities", "Coordinate with the team"),
            listOf("A", "C", "I", "S")),
        Question("Your preferred role in a team is:",
            listOf("Idea generator", "Organizer", "Researcher", "Facilitator"),
            listOf("A", "C", "I", "S")),
        Question("When someone disagrees with you, you:",
            listOf("Try to convince them", "Consider their viewpoint", "Analyze both perspectives", "Refer to guidelines"),
            listOf("E", "S", "I", "C")),
        Question("You learn best by:",
            listOf("Doing and experimenting", "Discussing with others", "Reading and reflecting", "Following structured lessons"),
            listOf("R", "S", "I", "C")),
        Question("You are most satisfied when:",
            listOf("Creating something", "Helping others", "Solving complex problems", "Completing tasks efficiently"),
            listOf("A", "S", "I", "C")),
        
        // Additional 25 RIASEC questions to reach 50 total
        Question("When working with tools or machines, you:",
            listOf("Enjoy hands-on problem solving", "Prefer understanding how they work", "Use them for creative projects", "Follow safety procedures carefully"),
            listOf("R", "I", "A", "C")),
        Question("Your approach to new technology is:",
            listOf("Build or fix it yourself", "Research thoroughly first", "Find creative applications", "Learn standard procedures"),
            listOf("R", "I", "A", "C")),
        Question("In a crisis situation, you would:",
            listOf("Take immediate practical action", "Analyze the situation first", "Think of creative solutions", "Follow emergency protocols"),
            listOf("R", "I", "A", "C")),
        Question("You prefer activities that are:",
            listOf("Physically engaging", "Mentally challenging", "Creatively stimulating", "Well-organized"),
            listOf("R", "I", "A", "C")),
        Question("When explaining something to others, you:",
            listOf("Show them how to do it", "Break it down logically", "Use analogies and stories", "Provide step-by-step instructions"),
            listOf("R", "I", "A", "C")),
        Question("Your ideal workspace would be:",
            listOf("A workshop or lab", "A quiet research area", "An open creative studio", "An organized office"),
            listOf("R", "I", "A", "C")),
        Question("When facing a complex problem, you:",
            listOf("Try different approaches", "Research similar cases", "Brainstorm creative solutions", "Follow proven methods"),
            listOf("R", "I", "A", "C")),
        Question("You are drawn to careers that involve:",
            listOf("Building or repairing", "Discovering or analyzing", "Creating or performing", "Organizing or managing"),
            listOf("R", "I", "A", "C")),
        Question("Your communication style is:",
            listOf("Direct and practical", "Detailed and analytical", "Expressive and engaging", "Clear and structured"),
            listOf("R", "I", "A", "C")),
        Question("When learning a new skill, you prefer:",
            listOf("Hands-on practice", "Understanding the theory", "Experimenting freely", "Following a curriculum"),
            listOf("R", "I", "A", "C")),
        Question("You work best in environments that are:",
            listOf("Action-oriented", "Intellectually stimulating", "Flexible and inspiring", "Structured and predictable"),
            listOf("R", "I", "A", "C")),
        Question("Your natural talents lie in:",
            listOf("Practical problem-solving", "Critical thinking", "Creative expression", "Organization and planning"),
            listOf("R", "I", "A", "C")),
        Question("When making decisions, you rely on:",
            listOf("Practical experience", "Logical analysis", "Intuition and creativity", "Established procedures"),
            listOf("R", "I", "A", "C")),
        Question("You are motivated by:",
            listOf("Tangible results", "Understanding complex ideas", "Self-expression", "Efficiency and order"),
            listOf("R", "I", "A", "C")),
        Question("Your approach to challenges is:",
            listOf("Roll up sleeves and tackle it", "Study and strategize", "Find innovative approaches", "Plan systematically"),
            listOf("R", "I", "A", "C")),
        Question("You prefer working with:",
            listOf("Tools and equipment", "Data and concepts", "Ideas and possibilities", "Systems and procedures"),
            listOf("R", "I", "A", "C")),
        Question("Your strength in teams is:",
            listOf("Getting things done", "Providing insights", "Generating ideas", "Keeping things organized"),
            listOf("R", "I", "A", "C")),
        Question("You are energized by:",
            listOf("Physical activity", "Mental challenges", "Creative projects", "Completing tasks"),
            listOf("R", "I", "A", "C")),
        Question("Your ideal day involves:",
            listOf("Building something", "Solving puzzles", "Creating art", "Organizing systems"),
            listOf("R", "I", "A", "C")),
        Question("When others describe you, they say you are:",
            listOf("Practical and reliable", "Analytical and thorough", "Creative and original", "Organized and dependable"),
            listOf("R", "I", "A", "C")),
        Question("You prefer feedback that is:",
            listOf("Specific and actionable", "Detailed and analytical", "Encouraging and inspiring", "Clear and structured"),
            listOf("R", "I", "A", "C")),
        Question("Your natural work pace is:",
            listOf("Steady and consistent", "Thoughtful and deliberate", "Bursts of inspiration", "Methodical and planned"),
            listOf("R", "I", "A", "C")),
        Question("You handle stress by:",
            listOf("Taking action", "Analyzing the situation", "Finding creative outlets", "Following routines"),
            listOf("R", "I", "A", "C")),
        Question("Your learning style is:",
            listOf("Learning by doing", "Reading and researching", "Exploring and experimenting", "Following structured lessons"),
            listOf("R", "I", "A", "C")),
        Question("You are most productive when:",
            listOf("Working with your hands", "Working with your mind", "Working with imagination", "Working with systems"),
            listOf("R", "I", "A", "C")),
        Question("When facing uncertainty, you:",
            listOf("Take practical action", "Gather more information", "Trust your instincts", "Follow established procedures"),
            listOf("R", "I", "A", "C")),
        Question("Your ideal weekend involves:",
            listOf("Outdoor activities", "Reading or learning", "Creative pursuits", "Organizing and planning"),
            listOf("R", "I", "A", "C")),
        Question("You handle pressure by:",
            listOf("Staying focused on tasks", "Analyzing the situation", "Finding creative solutions", "Following procedures"),
            listOf("R", "I", "A", "C")),
        Question("Your communication preference is:",
            listOf("Brief and direct", "Detailed and thorough", "Expressive and engaging", "Clear and organized"),
            listOf("R", "I", "A", "C"))
    )
    
    // Aptitude questions (exactly 20 questions)
    private val aptitudeQuestions = listOf(
        // Numerical Ability
        Question("If 8 workers build a wall in 12 days, how many workers are needed to build it in 6 days?",
            listOf("12", "16", "18", "20"), correctAnswer = 1),
        Question("A train travels 240 km in 3 hours. What is its speed in km/h?",
            listOf("70", "75", "80", "85"), correctAnswer = 2),
        Question("If 25% of a number is 60, what is 40% of that number?",
            listOf("90", "96", "100", "104"), correctAnswer = 1),
        Question("The ratio of boys to girls in a class is 3:2. If there are 15 boys, how many girls are there?",
            listOf("8", "10", "12", "15"), correctAnswer = 1),
        Question("A shopkeeper sells an item for ₹450 at a 10% profit. What was the cost price?",
            listOf("₹400", "₹405", "₹409", "₹415"), correctAnswer = 2),
        
        // Series & Pattern Recognition
        Question("Find the next number in the series: 2, 6, 12, 20, 30, ?",
            listOf("40", "42", "44", "46"), correctAnswer = 1),
        Question("Complete the series: A, D, G, J, ?",
            listOf("K", "L", "M", "N"), correctAnswer = 2),
        Question("Find the missing number: 5, 11, 23, 47, ?",
            listOf("91", "93", "95", "97"), correctAnswer = 2),
        Question("What comes next: 1, 4, 9, 16, 25, ?",
            listOf("30", "32", "36", "40"), correctAnswer = 2),
        Question("Complete: 3, 7, 15, 31, ?",
            listOf("59", "61", "63", "65"), correctAnswer = 2),
        
        // Logical Reasoning
        Question("All roses are flowers. Some flowers are red. Therefore:",
            listOf("All roses are red", "Some roses are red", "No roses are red", "Cannot be determined"), correctAnswer = 3),
        Question("If BOOK is coded as CPPL, how is WORD coded?",
            listOf("XPSE", "XQSE", "YPSE", "YQSE"), correctAnswer = 0),
        Question("In a certain code, RAIN is written as 1234. How is GAIN written?",
            listOf("5234", "6234", "7234", "8234"), correctAnswer = 0),
        Question("If Monday is the 1st, what day is the 15th?",
            listOf("Sunday", "Monday", "Tuesday", "Wednesday"), correctAnswer = 1),
        Question("A is taller than B. C is shorter than B. Who is the shortest?",
            listOf("A", "B", "C", "Cannot determine"), correctAnswer = 2),
        
        // Verbal Ability
        Question("Choose the synonym of 'Abundant':",
            listOf("Scarce", "Plentiful", "Limited", "Rare"), correctAnswer = 1),
        Question("What is the antonym of 'Optimistic'?",
            listOf("Hopeful", "Positive", "Pessimistic", "Confident"), correctAnswer = 2),
        Question("Complete the analogy: Book : Author :: Painting : ?",
            listOf("Canvas", "Brush", "Artist", "Color"), correctAnswer = 2),
        Question("Choose the correctly spelled word:",
            listOf("Recieve", "Receive", "Receve", "Receiv"), correctAnswer = 1),
        Question("What does 'Procrastinate' mean?",
            listOf("To hurry", "To delay", "To complete", "To organize"), correctAnswer = 1),
        
        // Mixed Reasoning
        Question("If it takes 5 machines 5 minutes to make 5 widgets, how long does it take 100 machines to make 100 widgets?",
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
                startPhase(5) // Show complete analysis
            }
            6 -> {
                // Store aptitude answers and submit final test
                storeAptitudeAnswers()
                Toast.makeText(this, "Test complete! Calculating your results...", Toast.LENGTH_SHORT).show()
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
                .setPositiveButton("Start Aptitude Test") { dialog, _ ->
                    dialog.dismiss()
                    android.util.Log.d("TestActivity", "Dialog dismissed, starting aptitude phase 6")
                    startPhase(6)
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
                // Continue to aptitude phase after guide
                android.util.Log.d("TestActivity", "✅ Complete analysis done, proceeding to aptitude phase 6 after cleanup...")
                
                // Delay to ensure proper UI cleanup
                binding.root.postDelayed({
                    android.util.Log.d("TestActivity", "🔄 Starting aptitude phase 6 now...")
                    startPhase(6)
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

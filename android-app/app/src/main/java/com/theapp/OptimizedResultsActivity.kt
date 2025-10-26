package com.theapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.theapp.databinding.ActivityResultsBinding
import kotlinx.coroutines.*

class OptimizedResultsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityResultsBinding
    private var roadmapJob: Job? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupBasicResults()
        setupOptimizedOccupationSelection()
        
        // Initialize guide system
        initializeResultsGuide()
    }
    
    private fun setupBasicResults() {
        // Get scores from intent with defaults
        val rScore = intent.getIntExtra("r_score", 5)
        val iScore = intent.getIntExtra("i_score", 3)
        val aScore = intent.getIntExtra("a_score", 2)
        val sScore = intent.getIntExtra("s_score", 8)
        val eScore = intent.getIntExtra("e_score", 4)
        val cScore = intent.getIntExtra("c_score", 1)
        val aptitudeScore = intent.getIntExtra("aptitude_score", 75)
        
        // Find dominant type
        val scores = mapOf("R" to rScore, "I" to iScore, "A" to aScore, "S" to sScore, "E" to eScore, "C" to cScore)
        val dominantType = scores.maxByOrNull { it.value }?.key ?: "S"
        
        // Set UI values safely
        runCatching {
            binding.tvPersonalityType.text = "Your Personality Type: $dominantType"
            binding.tvPersonalityDescription.text = getTypeDescription(dominantType)
            binding.tvDominantType.text = getTypeName(dominantType)
            binding.tvDominantDescription.text = getTypeDescription(dominantType)
            binding.tvAptitudeScore.text = "$aptitudeScore%"
            
            // Set RIASEC scores
            binding.tvRealisticScore.text = "$rScore"
            binding.tvInvestigativeScore.text = "$iScore"
            binding.tvArtisticScore.text = "$aScore"
            binding.tvSocialScore.text = "$sScore"
            binding.tvEnterprisingScore.text = "$eScore"
            binding.tvConventionalScore.text = "$cScore"
            
            // Set career recommendations
            binding.tvCareerRecommendations.text = getCareers(dominantType)
        }
        
        binding.btnRestart.setOnClickListener {
            finish()
        }
    }
    
    private fun setupOptimizedOccupationSelection() {
        try {
            // Optimized occupation list - reduced to prevent memory issues
            val occupations = getOptimizedOccupationList()
            
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, occupations)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerOccupations.adapter = adapter
            
            binding.btnShowRoadmap.setOnClickListener {
                val selectedOccupation = binding.spinnerOccupations.selectedItem?.toString() ?: "Teacher"
                if (selectedOccupation != "Select an occupation") {
                    showOptimizedRoadmap(selectedOccupation)
                }
            }
            
        } catch (e: Exception) {
            // Hide occupation selection if it fails
            binding.spinnerOccupations.visibility = View.GONE
            binding.btnShowRoadmap.visibility = View.GONE
        }
    }
    
    private fun showOptimizedRoadmap(occupation: String) {
        // Cancel any previous roadmap loading
        roadmapJob?.cancel()
        
        // Show loading state
        binding.tvRoadmapContent.text = "Loading comprehensive roadmap..."
        binding.cardRoadmapDisplay.visibility = View.VISIBLE
        
        // Load roadmap asynchronously to prevent UI blocking
        roadmapJob = CoroutineScope(Dispatchers.Main).launch {
            try {
                val roadmap = withContext(Dispatchers.Default) {
                    generateOptimizedRoadmap(occupation)
                }
                
                // Update UI on main thread
                binding.tvRoadmapContent.text = roadmap
                
                // Scroll to roadmap - the layout root is already a ScrollView
                // No need to scroll since the layout handles it automatically
                
            } catch (e: Exception) {
                binding.tvRoadmapContent.text = "Error loading roadmap. Please try again."
            }
        }
    }
    
    private fun generateOptimizedRoadmap(occupation: String): String {
        return when (occupation) {
            "Software Engineer" -> generateSoftwareEngineerRoadmap()
            "Doctor" -> generateDoctorRoadmap()
            "Teacher" -> generateTeacherRoadmap()
            "Mechanical Engineer" -> generateMechanicalEngineerRoadmap()
            "Data Scientist" -> generateDataScientistRoadmap()
            "Nurse" -> generateNurseRoadmap()
            "Accountant" -> generateAccountantRoadmap()
            "Graphic Designer" -> generateGraphicDesignerRoadmap()
            else -> generateGenericRoadmap(occupation)
        }
    }
    
    private fun generateSoftwareEngineerRoadmap(): String {
        return """
🚀 SOFTWARE ENGINEER ROADMAP

📚 HIGH SCHOOL (11th-12th)
• Core: Physics, Chemistry, Mathematics (PCM)
• Programming: Python, Java basics, competitive coding
• Projects: Simple apps, websites, GitHub portfolio
• Competitions: Hackathons, coding contests

🎓 UNDERGRADUATE (4 years)
• B.Tech/BE Computer Science, B.Sc CS, BCA
• Top Institutes: IITs, NITs, IIIT, VIT, SRM
• Entrance: JEE Main/Advanced, BITSAT, state exams
• Core: DSA, DBMS, OS, Networks, System Design

💻 TECHNICAL SKILLS
• Languages: Python, Java, JavaScript, C++, Go
• Web: React, Node.js, Angular, Vue.js
• Mobile: Android (Kotlin), iOS (Swift), React Native
• Cloud: AWS, GCP, Azure, Docker, Kubernetes
• Databases: MySQL, PostgreSQL, MongoDB, Redis

💰 SALARY PROGRESSION
📍 INDIA:
• Entry (0-2 years): ₹3-8 LPA
• Mid (3-5 years): ₹8-18 LPA
• Senior (6-10 years): ₹18-35 LPA
• Principal (10+ years): ₹35-80+ LPA

📍 USA:
• Entry: $70K-90K
• Mid: $90K-130K
• Senior: $130K-180K
• Staff: $180K-300K+

🏢 TOP RECRUITERS
• FAANG: Google, Apple, Meta, Amazon, Netflix
• Indian: TCS, Infosys, Wipro, Flipkart, Swiggy
• Global: Microsoft, IBM, Oracle, Salesforce

🎯 SPECIALIZATIONS
• Full Stack Development
• Mobile App Development
• AI/ML Engineering
• DevOps & Cloud
• Cybersecurity
• Data Engineering

🌟 SUCCESS STORY
Priya from NIT joined Amazon at ₹12 LPA, became Principal Engineer in 8 years earning ₹65 LPA. Focus: System design, leadership, mentoring.

⏰ TIMELINE: 4 years education + 3-5 years for senior roles
        """.trimIndent()
    }
    
    private fun generateDoctorRoadmap(): String {
        return """
🏥 DOCTOR CAREER ROADMAP

📚 HIGH SCHOOL (11th-12th)
• Core: Physics, Chemistry, Biology (PCB mandatory)
• Minimum: 85%+ in 12th for competitive medical entrance
• Preparation: NEET coaching, biology olympiads

🎓 UNDERGRADUATE (5.5 years)
India: MBBS (Bachelor of Medicine, Bachelor of Surgery)
• Duration: 4.5 years + 1 year internship
• Top Institutes: AIIMS, JIPMER, CMC Vellore, AFMC
• Entrance: NEET-UG (National Eligibility cum Entrance Test)
Abroad: MD (Doctor of Medicine)
• Countries: USA, UK, Australia, Canada
• Duration: 4 years + residency

📈 POSTGRADUATE (3 years)
• MD/MS specializations: Internal Medicine, Surgery, Pediatrics
• Entrance: NEET-PG
• Super-specialization: DM/MCh (3 years additional)

💰 SALARY PROGRESSION
India: ₹6-12 LPA (Junior Doctor) → ₹25-50 LPA (Specialist) → ₹1Cr+ (Consultant)
USA: $200K-300K (Resident) → $300K-500K+ (Specialist)

🏢 TOP RECRUITERS
AIIMS, Apollo, Fortis, Max Healthcare, Government Hospitals

🌟 CAREER GROWTH
Junior Resident → Senior Resident → Consultant → Department Head → Medical Director

⏰ TIMELINE: 11-15 years total (MBBS + Specialization + Experience)
        """.trimIndent()
    }
    
    private fun generateTeacherRoadmap(): String {
        return """
👩‍🏫 TEACHER CAREER ROADMAP

📚 HIGH SCHOOL (11th-12th)
• Stream: Any (Science/Commerce/Arts based on subject preference)
• Skills: Communication, subject mastery, patience
• Activities: Tutoring, volunteer teaching, debate clubs

🎓 UNDERGRADUATE (3 years)
• Bachelor's in subject of choice (B.A/B.Sc/B.Com)
• Top Institutes: DU, JNU, BHU, state universities
• Specialization: Choose teaching subject (Math, English, Science, etc.)

📈 PROFESSIONAL TRAINING (1-2 years)
• B.Ed (Bachelor of Education) - Mandatory for teaching
• Entrance: State B.Ed entrance exams
• Practical: Teaching practice, classroom management

🎯 CERTIFICATION & ELIGIBILITY
• TET (Teacher Eligibility Test) for government schools
• CTET (Central TET) for central government schools
• State-specific teacher recruitment exams

💰 SALARY PROGRESSION
Government: ₹25K-40K (PRT) → ₹50K-80K (TGT) → ₹80K-1.2L (PGT)
Private: ₹15K-30K → ₹40K-70K → ₹80K+ (Principal/Director)

🏢 CAREER OPPORTUNITIES
Government Schools, Private Schools, International Schools, Coaching Institutes

🌟 CAREER GROWTH
Assistant Teacher → Senior Teacher → Head Teacher → Principal → Education Administrator

⏰ TIMELINE: 4-5 years education + experience for senior positions
        """.trimIndent()
    }
    
    private fun generateGenericRoadmap(occupation: String): String {
        return """
🎯 $occupation CAREER ROADMAP

📚 EDUCATION PATH
• High School: Relevant stream selection
• Undergraduate: 3-4 years bachelor's degree
• Postgraduate: 2 years master's (optional)
• Certifications: Industry-specific credentials

📈 SKILL DEVELOPMENT
• Technical skills relevant to $occupation
• Soft skills: Communication, leadership, problem-solving
• Continuous learning and professional development

💰 CAREER PROSPECTS
• Entry level: Competitive starting salary
• Mid-level: Significant growth opportunities
• Senior level: Leadership and specialized roles

🏢 OPPORTUNITIES
• Private sector companies
• Government organizations
• International opportunities
• Entrepreneurship potential

⏰ TIMELINE: 4-6 years education + 3-5 years experience for senior roles
        """.trimIndent()
    }
    
    private fun generateMechanicalEngineerRoadmap(): String {
        return """
⚙️ MECHANICAL ENGINEER CAREER ROADMAP

📚 HIGH SCHOOL (11th-12th)
• Core: Physics, Chemistry, Mathematics (PCM mandatory)
• Skills: Technical drawing, basic mechanics understanding
• Preparation: JEE coaching, physics olympiads

🎓 UNDERGRADUATE (4 years)
India: B.Tech/BE Mechanical Engineering
• Top Institutes: IITs, NITs, BITS Pilani, VIT
• Entrance: JEE Main/Advanced, state engineering exams
Abroad: Mechanical Engineering degrees
• Countries: USA, Germany, Canada, UK

📈 SPECIALIZATIONS
• Automotive Engineering, Aerospace, Manufacturing
• Robotics, Thermal Engineering, Design Engineering
• CAD/CAM, Production Engineering

💰 SALARY PROGRESSION
India: ₹3-6 LPA → ₹8-15 LPA → ₹20-40 LPA
USA: $65K-80K → $90K-120K → $130K+

🏢 TOP RECRUITERS
Tata Motors, Mahindra, L&T, BHEL, Siemens, General Electric

⏰ TIMELINE: 4 years B.Tech + 2-3 years for senior positions
        """.trimIndent()
    }
    
    private fun generateDataScientistRoadmap(): String {
        return """
📊 DATA SCIENTIST CAREER ROADMAP

📚 EDUCATION
• Background: Mathematics, Statistics, Computer Science, Engineering
• Skills: Python, R, SQL, Machine Learning, Statistics
• Certifications: Google Data Analytics, IBM Data Science

🎓 LEARNING PATH
• Programming: Python/R, SQL databases
• Mathematics: Statistics, Linear Algebra, Calculus
• Tools: Tableau, Power BI, Jupyter, TensorFlow

💰 SALARY PROGRESSION
India: ₹6-12 LPA → ₹15-25 LPA → ₹40+ LPA
USA: $95K-120K → $140K-180K → $200K+

🏢 TOP RECRUITERS
Google, Amazon, Microsoft, Flipkart, Swiggy, consulting firms

⏰ TIMELINE: 3-4 years education + 2 years experience for senior roles
        """.trimIndent()
    }
    
    private fun generateNurseRoadmap(): String {
        return """
👩‍⚕️ NURSE CAREER ROADMAP

📚 HIGH SCHOOL
• Core: Physics, Chemistry, Biology
• Skills: Caring nature, communication, attention to detail

🎓 EDUCATION (3-4 years)
• B.Sc Nursing, GNM (General Nursing and Midwifery)
• Top Institutes: AIIMS, CMC, nursing colleges
• Practical: Hospital training, patient care experience

💰 SALARY PROGRESSION
India: ₹2-4 LPA → ₹5-8 LPA → ₹10-15 LPA
Abroad: $50K-70K → $70K-90K → $90K+

🏢 OPPORTUNITIES
Hospitals, clinics, home healthcare, international nursing

⏰ TIMELINE: 3-4 years education + experience for specialization
        """.trimIndent()
    }
    
    private fun generateAccountantRoadmap(): String {
        return """
💼 ACCOUNTANT CAREER ROADMAP

📚 EDUCATION
• Commerce stream in 12th
• B.Com, BBA, or related degree
• Professional: CA, CS, CMA certifications

📈 CERTIFICATIONS
• CA (Chartered Accountant) - 4-5 years
• CS (Company Secretary) - 3-4 years
• CMA (Cost and Management Accountant) - 3-4 years

💰 SALARY PROGRESSION
India: ₹3-5 LPA → ₹8-15 LPA → ₹25+ LPA
Big 4: ₹6-10 LPA → ₹15-25 LPA → ₹40+ LPA

🏢 TOP RECRUITERS
Deloitte, PwC, EY, KPMG, corporate finance departments

⏰ TIMELINE: 3 years B.Com + 3-5 years professional certification
        """.trimIndent()
    }
    
    private fun generateGraphicDesignerRoadmap(): String {
        return """
🎨 GRAPHIC DESIGNER CAREER ROADMAP

📚 EDUCATION
• Any stream in 12th (Arts preferred)
• B.Des, BFA, Diploma in Graphic Design
• Skills: Creativity, visual communication, software proficiency

📈 TECHNICAL SKILLS
• Adobe Creative Suite: Photoshop, Illustrator, InDesign
• Web design: Figma, Sketch, HTML/CSS basics
• Branding, typography, color theory

💰 SALARY PROGRESSION
India: ₹2-4 LPA → ₹6-12 LPA → ₹15-25 LPA
Freelance: ₹500-2000 per project → ₹5K-20K per project

🏢 OPPORTUNITIES
Advertising agencies, design studios, in-house teams, freelancing

⏰ TIMELINE: 3-4 years education + portfolio building
        """.trimIndent()
    }
    
    private fun setupBasicResultsOnly() {
        // Fallback method if advanced features fail
        binding.spinnerOccupations.visibility = View.GONE
        binding.btnShowRoadmap.visibility = View.GONE
        binding.cardRoadmapDisplay.visibility = View.GONE
        
        binding.tvPersonalityType.text = "Career Assessment Results"
        binding.tvPersonalityDescription.text = "Your personality assessment has been completed successfully."
    }
    
    private fun getOptimizedOccupationList(): List<String> {
        return listOf(
            "Select an occupation",
            "Software Engineer",
            "Data Scientist",
            "Doctor",
            "Teacher",
            "Mechanical Engineer",
            "Nurse",
            "Accountant",
            "Graphic Designer",
            "Business Manager",
            "Lawyer",
            "Civil Engineer",
            "Marketing Manager",
            "Psychologist",
            "Architect",
            "Pharmacist",
            "Dentist",
            "Veterinarian",
            "Physiotherapist",
            "Chef",
            "Journalist",
            "Financial Analyst",
            "HR Manager",
            "Product Manager",
            "UX Designer",
            "Digital Marketer",
            "Investment Banker",
            "Management Consultant",
            "Research Scientist",
            "Social Worker"
        )
    }
    
    private fun getTypeName(type: String): String {
        return when (type) {
            "R" -> "Realistic"
            "I" -> "Investigative"
            "A" -> "Artistic"
            "S" -> "Social"
            "E" -> "Enterprising"
            "C" -> "Conventional"
            else -> "Social"
        }
    }
    
    private fun getTypeDescription(type: String): String {
        return when (type) {
            "R" -> "Practical, hands-on problem solvers who enjoy working with tools and machinery"
            "I" -> "Analytical, intellectual, scientific thinkers who love research and investigation"
            "A" -> "Creative, expressive, original individuals who value artistic expression"
            "S" -> "Helpful, caring, people-oriented individuals who enjoy helping others"
            "E" -> "Persuasive, ambitious, leadership-oriented people who enjoy business and management"
            "C" -> "Organized, detail-oriented, systematic workers who prefer structured environments"
            else -> "Helpful, caring, people-oriented individuals who enjoy helping others"
        }
    }
    
    private fun getCareers(type: String): String {
        return when (type) {
            "R" -> "Engineer, Technician, Mechanic, Electrician, Carpenter, Pilot, Farmer"
            "I" -> "Scientist, Researcher, Doctor, Analyst, Programmer, Mathematician, Psychologist"
            "A" -> "Artist, Designer, Writer, Musician, Actor, Photographer, Architect"
            "S" -> "Teacher, Counselor, Social Worker, Nurse, Therapist, Human Resources"
            "E" -> "Manager, Sales Representative, Entrepreneur, Lawyer, Politician, Marketing Executive"
            "C" -> "Accountant, Clerk, Secretary, Librarian, Data Entry, Financial Analyst"
            else -> "Teacher, Counselor, Social Worker, Nurse, Therapist, Human Resources"
        }
    }
    
    private fun initializeResultsGuide() {
        try {
            // Create Velly Bandaar guide for results page
            val rootView = findViewById<FrameLayout>(android.R.id.content)
            val resultsGuide = ResultsGuideSystem(this, rootView)
            resultsGuide.init()
        } catch (e: Exception) {
            android.util.Log.e("OptimizedResultsActivity", "Error initializing guide", e)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        roadmapJob?.cancel()
    }
}

package com.theapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.theapp.databinding.ActivityResultsBinding

class ResultsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityResultsBinding
    private var currentPersonalityCode: String = ""
    private var currentAptitudeScore: Int = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityResultsBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            displayResults()
            
            binding.btnRestart.setOnClickListener {
                finish()
            }
        } catch (e: Exception) {
            println("Critical error in onCreate: ${e.message}")
            e.printStackTrace()
            finish() // Close activity if critical error occurs
        }
    }
    
    private fun displayResults() {
        try {
            // Get individual RIASEC scores from intent
            val rScore = intent.getIntExtra("r_score", 0)
            val iScore = intent.getIntExtra("i_score", 0)
            val aScore = intent.getIntExtra("a_score", 0)
            val sScore = intent.getIntExtra("s_score", 0)
            val eScore = intent.getIntExtra("e_score", 0)
            val cScore = intent.getIntExtra("c_score", 0)
            val aptitudeScore = intent.getIntExtra("aptitude_score", 0)
        
        // Create RIASEC scores map
        val riasecScores = mapOf(
            "R" to rScore,
            "I" to iScore,
            "A" to aScore,
            "S" to sScore,
            "E" to eScore,
            "C" to cScore
        )
        
        // Debug logging
        println("ResultsActivity - RIASEC Scores: $riasecScores")
        println("ResultsActivity - Aptitude Score: $aptitudeScore")
        
        // Find dominant and secondary types for dual personality detection
        val sortedTypes = riasecScores.entries.sortedByDescending { it.value }
        val dominantType = sortedTypes[0].key
        val dominantScore = sortedTypes[0].value
        val secondaryType = sortedTypes[1].key
        val secondaryScore = sortedTypes[1].value
        
        // Check for dual personality (secondary within 2 points of dominant AND both scores > 0)
        val scoreDifference = dominantScore - secondaryScore
        val isDualType = scoreDifference <= 2 && dominantScore > 0 && secondaryScore > 0
        val personalityCode = if (isDualType) "${dominantType}+${secondaryType}" else dominantType
        
        println("ResultsActivity - Score Difference: $scoreDifference")
        println("ResultsActivity - Is Dual Type: $isDualType")
        println("ResultsActivity - Personality Code: $personalityCode")
        println("ResultsActivity - Dominant: $dominantType ($dominantScore), Secondary: $secondaryType ($secondaryScore)")
        
        // Display personality type and description
        binding.tvPersonalityType.text = "Your Personality Type: $personalityCode"
        
        if (isDualType) {
            binding.tvPersonalityDescription.text = "Dual Personality: ${getDominantTypeName(dominantType)} + ${getDominantTypeName(secondaryType)}\n" +
                    "${getDominantTypeDescription(dominantType)} combined with ${getDominantTypeDescription(secondaryType).lowercase()}"
        } else {
            binding.tvPersonalityDescription.text = "${getDominantTypeName(dominantType)} - ${getDominantTypeDescription(dominantType)}"
        }
        
        // Update UI
        binding.tvDominantType.text = if (isDualType) {
            "${getDominantTypeName(dominantType)} + ${getDominantTypeName(secondaryType)}"
        } else {
            getDominantTypeName(dominantType)
        }
        binding.tvDominantDescription.text = getDominantTypeDescription(dominantType)
        binding.tvAptitudeScore.text = "$aptitudeScore%"
        
        // Show RIASEC scores
        binding.tvRealisticScore.text = "$rScore"
        binding.tvInvestigativeScore.text = "$iScore"
        binding.tvArtisticScore.text = "$aScore"
        binding.tvSocialScore.text = "$sScore"
        binding.tvEnterprisingScore.text = "$eScore"
        binding.tvConventionalScore.text = "$cScore"
        
        // Show career recommendations based on personality code and aptitude
        binding.tvCareerRecommendations.text = getCareerRecommendations(personalityCode, aptitudeScore)
        
        // Store values for occupation selection
        this.currentPersonalityCode = personalityCode
        this.currentAptitudeScore = aptitudeScore
        
        // Setup occupation selection after displaying results
        setupOccupationSelection()
        } catch (e: Exception) {
            println("Error in displayResults: ${e.message}")
            e.printStackTrace()
            // Set default values if there's an error
            binding.tvPersonalityType.text = "Your Personality Type: S"
            binding.tvPersonalityDescription.text = "Social - Helpful, caring, people-oriented"
            binding.tvDominantType.text = "Social"
            binding.tvDominantDescription.text = "Helpful, caring, people-oriented"
            binding.tvAptitudeScore.text = "0%"
            binding.tvCareerRecommendations.text = "Teacher, Counselor, Social Worker"
            // Setup basic occupation selection even in error case
            try {
                setupOccupationSelection()
            } catch (ex: Exception) {
                println("Failed to setup occupation selection: ${ex.message}")
            }
        }
    }
    
    private fun getDominantTypeName(type: String): String {
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
    
    private fun getDominantTypeDescription(type: String): String {
        return when (type) {
            "R" -> "Practical, hands-on problem solvers"
            "I" -> "Analytical, intellectual, scientific"
            "A" -> "Creative, expressive, original"
            "S" -> "Helpful, caring, people-oriented"
            "E" -> "Persuasive, ambitious, leadership-oriented"
            "C" -> "Organized, detail-oriented, systematic"
            else -> "Helpful, caring, people-oriented"
        }
    }
    
    private fun getCareerRecommendations(personalityCode: String, aptitudeScore: Int): String {
        // Debug logging
        println("Getting career recommendations for: $personalityCode with aptitude: $aptitudeScore%")
        
        // Get score-based career recommendations
        val careerRecommendations = getScoreBasedCareers(personalityCode, aptitudeScore)
        val isDualType = personalityCode.contains("+")
        
        val primaryType = personalityCode.split("+")[0]
        
        return """
            🎯 YOUR PERSONALITY PROFILE: ${personalityCode}
            ${if (isDualType) "You have a balanced combination of traits." else "You are primarily a ${primaryType} type."}
            
            📊 APTITUDE SCORE: ${aptitudeScore}%
            
            💼 RECOMMENDED CAREERS FOR YOUR PROFILE:
            ${careerRecommendations.joinToString("\n") { "• $it" }}
            
        """.trimIndent() + getDetailedCareerRoadmap(primaryType, aptitudeScore)
    }
    
    private fun getScoreBasedCareers(personalityCode: String, aptitudeScore: Int): List<String> {
        val aptitudeBand = when {
            aptitudeScore <= 10 -> 0
            aptitudeScore <= 20 -> 1
            aptitudeScore <= 30 -> 2
            aptitudeScore <= 40 -> 3
            else -> 4
        }
        
        return when (personalityCode) {
            "R" -> when (aptitudeBand) {
                0 -> listOf("Laborer", "Helper", "Basic Technician")
                1 -> listOf("Technician Assistant", "Machine Operator")
                2 -> listOf("Skilled Technician", "Electrician", "Field Worker")
                3 -> listOf("Mechanical Engineer", "Civil Engineer", "Electrical Engineer")
                else -> listOf("Aerospace Engineer", "Biomedical Engineer", "Chemical Engineer", "Advanced Engineering Roles")
            }
            "I" -> when (aptitudeBand) {
                0 -> listOf("Lab Assistant", "Junior Support Staff")
                1 -> listOf("Basic Research Support", "Data Entry in R&D")
                2 -> listOf("Research Assistant", "Data Analyst (junior)", "Technician")
                3 -> listOf("Data Scientist", "Software Developer", "Research Analyst")
                else -> listOf("Senior Scientist", "AI Researcher", "Policy Analyst", "Cybersecurity Head")
            }
            "A" -> when (aptitudeBand) {
                0 -> listOf("Helper in Arts/Design", "Basic Craftsperson")
                1 -> listOf("Junior Designer", "Assistant Content Creator")
                2 -> listOf("Graphic Designer", "Animator", "Social Media Content Creator")
                3 -> listOf("Creative Director", "UX/UI Designer", "Game Designer")
                else -> listOf("Creative Strategist", "Art Entrepreneur", "Media Innovator")
            }
            "S" -> when (aptitudeBand) {
                0 -> listOf("Community Helper", "Support Staff")
                1 -> listOf("Receptionist", "Basic Caregiver", "Junior Assistant")
                2 -> listOf("Teacher (primary)", "Social Worker", "HR Associate")
                3 -> listOf("HR Manager", "Counselor", "Healthcare Administrator")
                else -> listOf("Organizational Leader", "Senior Educator", "Social Policy Maker")
            }
            "E" -> when (aptitudeBand) {
                0 -> listOf("Sales Assistant", "Entry-level Support")
                1 -> listOf("Junior Sales Executive", "Assistant Manager")
                2 -> listOf("Business Development Associate", "Team Lead")
                3 -> listOf("Manager", "Entrepreneur", "Corporate Strategist")
                else -> listOf("CEO", "Politician", "Venture Capitalist", "Senior Executive")
            }
            "C" -> when (aptitudeBand) {
                0 -> listOf("Data Entry Clerk", "Filing Assistant")
                1 -> listOf("Office Support Staff", "Basic Accountant")
                2 -> listOf("Admin Officer", "Payroll Associate", "Junior Auditor")
                3 -> listOf("Financial Analyst", "Operations Planner", "Senior Compliance Officer")
                else -> listOf("CFO", "Senior Compliance Manager", "Risk Analyst")
            }
            else -> {
                // Handle dual personality types
                return getDualPersonalityCareers(personalityCode, aptitudeScore)
            }
        }
    }
    
    private fun getDetailedCareerRoadmap(primaryType: String, aptitudeScore: Int): String {
        val aptitudeBand = when {
            aptitudeScore <= 10 -> 0
            aptitudeScore <= 20 -> 1
            aptitudeScore <= 30 -> 2
            aptitudeScore <= 40 -> 3
            else -> 4
        }
        
        // Get ultra-comprehensive roadmap data
        val comprehensiveRoadmap = getUltraComprehensiveRoadmap(primaryType, aptitudeBand)
        
        return when (primaryType) {
            "R" -> when (aptitudeBand) {
                0, 1 -> """
                    
                    🔧 REALISTIC CAREERS - SKILLED TRADES TRACK:
                    
                    TECHNICAL SUPPORT ROLES:
                    • Technician Assistant • Machine Operator • Basic Technician
                    
                    🏫 VOCATIONAL TRAINING:
                    • ITI (Industrial Training Institute) • Polytechnic Diploma
                    • Community Colleges • Trade Schools
                    • Duration: 6 months - 3 years
                    
                    📚 LEARNING PATH:
                    Year 1: Basic technical skills, safety protocols
                    Year 2-3: Specialized trade certification
                    
                    💰 SALARY EXPECTATIONS:
                    Entry Level: ₹2-4 LPA | Mid Level: ₹4-8 LPA | Senior: ₹8-15 LPA
                """
                2 -> """
                    
                    🔧 REALISTIC CAREERS - SKILLED TRADES:
                    
                    SKILLED TECHNICAL ROLES:
                    • Electrician • Skilled Technician • Field Worker
                    • Automotive Technician • Construction Supervisor
                    
                    🏫 TECHNICAL EDUCATION:
                    • ITI Advanced Courses • Polytechnic Engineering
                    • NCVT/SCVT Certifications • Industry Apprenticeships
                    
                    📚 LEARNING PATH (2-4 years):
                    Year 1-2: Technical foundation, hands-on training
                    Year 3-4: Advanced certifications, supervisory skills
                    
                    💰 SALARY EXPECTATIONS:
                    Entry Level: ₹3-6 LPA | Mid Level: ₹6-12 LPA | Senior: ₹12-20 LPA
                """
                3, 4 -> """
                    
                    🔧 REALISTIC CAREERS - ENGINEERING TRACK:
                    
                    ENGINEERING CAREERS:
                    • Civil Engineer • Mechanical Engineer • Electrical Engineer
                    • Aerospace Engineer • Biomedical Engineer • Chemical Engineer
                    
                    🏛️ TOP UNIVERSITIES & ADMISSION:
                    • IIT (JEE Advanced) • NIT (JEE Main) • BITS Pilani (BITSAT)
                    • MIT, Stanford, Caltech (SAT/ACT + Strong Math/Science)
                    • Requirements: 85%+ in Physics, Chemistry, Math
                    
                    📚 LEARNING PATH (4-6 years):
                    Year 1-2: Engineering fundamentals, Math, Physics
                    Year 3-4: Specialized engineering courses, internships
                    Year 5-6: Advanced projects, industry certifications
                    
                    💰 SALARY EXPECTATIONS:
                    Entry Level: ₹6-12 LPA | Mid Level: ₹12-25 LPA | Senior: ₹25-60 LPA
                """
                else -> ""
            }
            "I" -> """
                
                🔬 INVESTIGATIVE CAREERS & ROADMAPS:
                
                RESEARCH TRACK:
                • Data Scientist • Research Scientist • Systems Analyst
                • Biomedical Researcher • Environmental Scientist • Psychologist
                
                🏛️ TOP UNIVERSITIES & ADMISSION:
                • IISc Bangalore • TIFR • AIIMS (NEET) • IIT (Research Programs)
                • Harvard, MIT, Stanford (GRE/GMAT + Research Experience)
                • Requirements: 90%+ in Science subjects, Research aptitude
                
                TECHNOLOGY RESEARCH:
                • AI Researcher • Quantum Computing Specialist • Bioinformatics
                • Data Mining Expert • Computational Biology • Robotics Engineer
                
                📚 LEARNING PATH (6-8 years):
                Year 1-3: Bachelor's in Science/Engineering
                Year 4-6: Master's with research focus
                Year 7-8: PhD or industry research roles
                
                💰 SALARY EXPECTATIONS:
                Entry Level: ₹4-10 LPA | Mid Level: ₹10-25 LPA | Senior: ₹25-60 LPA
            """
            "A" -> """
                
                🎨 ARTISTIC CAREERS & ROADMAPS:
                
                DESIGN TRACK:
                • Graphic Designer • UX/UI Designer • Creative Director
                • Art Director • Fashion Designer • Interior Designer
                
                🏛️ TOP UNIVERSITIES & ADMISSION:
                • NID Ahmedabad • NIFT • Srishti School of Art
                • Parsons, RISD, Central Saint Martins (Portfolio + Interview)
                • Requirements: Strong portfolio, creativity tests, 75%+ academics
                
                MEDIA & ENTERTAINMENT:
                • Film Director • Animator • Game Designer
                • Content Creator • Digital Artist • Photographer
                
                📚 LEARNING PATH (3-5 years):
                Year 1-2: Foundation - Drawing, Color theory, Design principles
                Year 3-4: Specialization - Digital design, Traditional media
                Year 5-6: Advanced portfolio, internships, exhibitions
                
                💰 SALARY EXPECTATIONS:
                Entry Level: ₹2-6 LPA | Mid Level: ₹6-18 LPA | Senior: ₹18-45 LPA
            """
            "S" -> """
                
                👥 SOCIAL CAREERS & DETAILED ROADMAPS:
                
                🏥 HEALTHCARE LEADERSHIP (Aptitude 41-50):
                • Physicians, Surgeons, Psychologists, Dentists
                • Medical Directors, Healthcare Executives
                
                🎓 TOP UNIVERSITIES & ADMISSION:
                • Medicine: Harvard Medical, Johns Hopkins, AIIMS Delhi
                • Psychology: Stanford, Yale, Cambridge, NIMHANS
                • Nursing: UPenn, Duke, Johns Hopkins
                
                📋 ADMISSION CRITERIA:
                • Medical School: NEET (India), MCAT (US), BMAT (UK)
                • Psychology PhD: GRE, Research experience, Clinical hours
                • Requirements: Biology, Chemistry, Physics background
                • Clinical Experience: Volunteering, shadowing physicians
                
                📚 LEARNING PATH (6-12 years):
                Year 1-3: Pre-med/Bachelor's in Science
                Year 4-7: Medical School (MBBS/MD)
                Year 8-11: Residency training in specialization
                Year 12+: Fellowship for subspecialization
                
                🎓 EDUCATION & COUNSELING (Aptitude 31-40):
                • Teachers, School Counselors, Educational Administrators
                • Mental Health Counselors, Social Workers
                
                🏫 EDUCATION REQUIREMENTS:
                • Teaching: B.Ed after Bachelor's, TET/CTET certification
                • Counseling: Master's in Psychology/Social Work
                • Administration: M.Ed + administrative experience
                
                👨‍⚕️ HEALTHCARE SUPPORT (Aptitude 11-30):
                • Registered Nurses, Physical Therapists, Medical Assistants
                • Community Health Workers, Healthcare Technicians
                
                💰 SALARY EXPECTATIONS:
                Entry Level: ₹2-8 LPA
                Mid Level: ₹8-25 LPA
                Senior Level: ₹25-60 LPA
                Medical Specialists: ₹60 LPA+
            """
            "E" -> """
                
                💼 ENTERPRISING CAREERS & DETAILED ROADMAPS:
                
                🏢 EXECUTIVE LEADERSHIP (Aptitude 41-50):
                • CEOs, Politicians, Venture Capitalists, Senior Executives
                • Chief Executives, Investment Fund Managers
                
                🎓 TOP UNIVERSITIES & ADMISSION:
                • Business: Harvard Business School, Wharton, Stanford GSB
                • India: IIM Ahmedabad, IIM Bangalore, IIM Calcutta
                • Law: Yale Law, Harvard Law, Columbia Law
                
                📋 ADMISSION CRITERIA:
                • MBA: CAT (India), GMAT/GRE (International), Work experience
                • Law: CLAT (India), LSAT (US), Strong academic record
                • Requirements: Leadership potential, Business acumen
                • Experience: 2-5 years professional work experience
                
                📚 LEARNING PATH (5-8 years):
                Year 1-3: Bachelor's in Business/Economics/Engineering
                Year 4-6: Work experience in relevant field
                Year 7-8: MBA from top business school
                Advanced: Executive education, Leadership programs
                
                💼 MANAGEMENT ROLES (Aptitude 31-40):
                • Marketing Managers, Sales Managers, Operations Managers
                • Financial Managers, Human Resources Managers
                
                🏫 BUSINESS EDUCATION:
                • Bachelor's: BBA, B.Com, Economics from top universities
                • Certifications: PMP, Six Sigma, Digital Marketing
                • Skills: Leadership, Communication, Strategic thinking
                
                📊 BUSINESS DEVELOPMENT (Aptitude 11-30):
                • Sales Representatives, Marketing Specialists, HR Specialists
                • Business Analysts, Project Coordinators
                
                💰 SALARY EXPECTATIONS:
                Entry Level: ₹3-10 LPA
                Mid Level: ₹10-30 LPA
                Senior Level: ₹30-80 LPA
                C-Suite/Entrepreneur: ₹80 LPA+
            """
            "C" -> """
                
                📊 CONVENTIONAL CAREERS & DETAILED ROADMAPS:
                
                💰 FINANCIAL LEADERSHIP (Aptitude 41-50):
                • CFOs, Actuaries, Financial Risk Specialists
                • Operations Research Analysts, Senior Compliance Managers
                
                🎓 TOP UNIVERSITIES & ADMISSION:
                • Finance: Wharton, Chicago Booth, London Business School
                • Actuarial: Georgia State, University of Illinois, Boston University
                • India: SRCC, LSR, St. Xavier's College
                
                📋 ADMISSION CRITERIA:
                • Professional Exams: CA, CS, CMA, CFA, FRM
                • Actuarial: SOA, CAS professional certifications
                • Requirements: Strong mathematics, Statistics background
                • Experience: Internships in finance, Analytical projects
                
                📚 LEARNING PATH (4-7 years):
                Year 1-3: Bachelor's in Commerce/Economics/Mathematics
                Year 4-5: Professional certifications (CA/CS/CMA)
                Year 6-7: Advanced certifications (CFA/FRM/Actuarial exams)
                Continuous: Professional development, Industry updates
                
                📈 FINANCIAL ANALYSIS (Aptitude 31-40):
                • Financial Analysts, Budget Analysts, Compliance Officers
                • Database Administrators, Operations Planners
                
                🏫 PROFESSIONAL EDUCATION:
                • Commerce: B.Com (Hons), Economics, Statistics
                • Certifications: CPA, ACCA, CISA, PMP
                • Skills: Excel, SQL, Financial modeling, Risk analysis
                
                📋 ADMINISTRATIVE SUPPORT (Aptitude 11-30):
                • Accountants, Bookkeepers, Administrative Officers
                • Payroll Associates, Office Managers
                
                🏫 TECHNICAL TRAINING:
                • Diploma: Accounting, Office Administration
                • Certifications: Tally, SAP, QuickBooks
                • Duration: 6 months - 2 years
                
                💰 SALARY EXPECTATIONS:
                Entry Level: ₹2-6 LPA
                Mid Level: ₹6-20 LPA
                Senior Level: ₹20-50 LPA
                C-Suite Finance: ₹50 LPA+
            """
            else -> ""
        }
    }
    
    private fun getUltraComprehensiveRoadmap(primaryType: String, aptitudeBand: Int): String {
        return when (primaryType) {
            "R" -> when (aptitudeBand) {
                4 -> """
                    
                    🚀 ENGINEERING CAREER ROADMAP:
                    
                    📚 18-MONTH LEARNING PATH:
                    Phase 1 (1-6 months): Math, Physics, Programming, CAD
                    Phase 2 (7-12 months): Specialization + Internship
                    Phase 3 (13-18 months): Industry readiness + Portfolio
                    
                    💼 SUCCESS STORY:
                    Arjun Patel: NIT → Tesla Senior Engineer (₹45 LPA)
                    Strategy: EV specialization, 2-year timeline
                    
                    🏢 TESLA HIRING:
                    1. Online application 2. Phone screen 3. 4-5 interviews
                    Prep: 50+ design problems, STAR stories, portfolio
                    
                    💰 FINANCIAL PLAN:
                    Investment: ₹8-15L education + ₹3-5L certifications
                    Earnings: ₹6-12L → ₹12-25L → ₹25-50L → ₹50L+
                    Lifetime value: ₹20-80 Cr
                    
                    📅 ACTION PLAN:
                    30 days: Research programs, start math, learn CAD
                    90 days: Complete fundamentals, build portfolio
                    12 months: Education/certification, gain experience
                """
                else -> ""
            }
            "I" -> when (aptitudeBand) {
                4 -> """
                    
                    🧠 AI/ML CAREER ROADMAP:
                    
                    📚 18-MONTH LEARNING PATH:
                    Phase 1 (1-3 months): Math foundations, Python basics
                    Phase 2 (4-8 months): Core ML, Deep Learning, CV, NLP
                    Phase 3 (9-12 months): Advanced algorithms, MLOps
                    Phase 4 (13-18 months): Open source, Kaggle, portfolio
                    
                    💼 SUCCESS STORIES:
                    Priya Sharma: Mechanical → Google ML Lead (₹85 LPA)
                    Strategy: 2hrs daily, 10+ GitHub projects, Kaggle Expert
                    
                    Rahul Gupta: Tier-3 college → AI Startup CEO (₹100 Cr)
                    Strategy: Freelance → Local business → ₹50L seed → Scale
                    
                    🏢 GOOGLE INTERVIEW:
                    Process: Application → Phone → 4-5 technical rounds
                    Prep: 6-8 weeks, LeetCode, system design, ML concepts
                    
                    💰 FINANCIAL PLAN:
                    Investment: ₹50K-2L courses + ₹1-3L certifications
                    Earnings: ₹8-15L → ₹18-35L → ₹35-70L → ₹70L+
                    ROI: 300-500% over 10 years, ₹15-50 Cr lifetime
                    
                    🌐 INSIDER SECRETS:
                    • Remote work = 20-30% salary premium
                    • GitHub > degrees for hiring
                    • Referrals = 5-10x better chances
                    • Stock options = 2-5x base salary value
                    
                    📅 ACTION PLAN:
                    30 days: Setup environment, first ML project, GitHub
                    90 days: 3+ projects, Kaggle, apply for roles
                    12 months: Advanced specialization, job prep
                """
                else -> ""
            }
            "A" -> when (aptitudeBand) {
                4 -> """
                    
                    🎨 CREATIVE TECHNOLOGY ROADMAP:
                    
                    📚 12-MONTH CREATIVE PATH:
                    Phase 1 (1-6 months): Design principles, Adobe Suite, UX basics
                    Phase 2 (7-12 months): Advanced UX/UI, motion graphics, branding
                    
                    💼 SUCCESS STORY:
                    Maya Patel: NID → Ogilvy Creative Director (₹35 LPA)
                    Strategy: Digital brand specialization, 8-year timeline
                    
                    🏢 AGENCY HIRING:
                    Process: Portfolio → Creative brief → Presentation → Culture fit
                    Key: Portfolio is most critical factor
                    
                    💰 FINANCIAL PLAN:
                    Freelance: ₹500-5000/hour
                    Agency: ₹3-35 LPA
                    Retainer: ₹50K-5L/month
                    Tech companies: ₹8-50 LPA
                """
                else -> ""
            }
            "S" -> when (aptitudeBand) {
                4 -> """
                    
                    👥 HEALTHCARE & SOCIAL ROADMAP:
                    
                    📚 MEDICAL TRACK (8-12 years):
                    MBBS (5.5 years) → Residency (3 years) → Fellowship (2 years)
                    Entry: NEET (India), MCAT (USA), BMAT (UK)
                    
                    💼 SUCCESS PATHS:
                    • Physician/Surgeon: ₹15-60 LPA
                    • Medical Director: ₹60 LPA+
                    • Healthcare Entrepreneur: Variable
                    
                    🏥 TOP INSTITUTES:
                    India: AIIMS, JIPMER, CMC Vellore
                    Abroad: Harvard Medical, Johns Hopkins, Mayo Clinic
                    
                    📚 EDUCATION TRACK (4-6 years):
                    B.Ed → M.Ed → PhD (optional)
                    Entry: TET/CTET, university entrance exams
                    
                    💰 SALARY RANGES:
                    Teacher: ₹3-15 LPA
                    Principal: ₹8-25 LPA
                    Education Policy: ₹15-40 LPA
                """
                else -> ""
            }
            "E" -> when (aptitudeBand) {
                4 -> """
                    
                    💼 BUSINESS LEADERSHIP ROADMAP:
                    
                    📚 MBA TRACK (5-7 years):
                    Bachelor's → Work experience (2-3 years) → MBA (2 years)
                    Entry: CAT (India), GMAT/GRE (International)
                    
                    🏢 TOP B-SCHOOLS:
                    India: IIM A/B/C, ISB Hyderabad
                    Abroad: Harvard, Wharton, Stanford, INSEAD
                    
                    💼 CAREER PATHS:
                    • Management Consultant: ₹15-50 LPA
                    • Investment Banking: ₹20-80 LPA
                    • CEO/Entrepreneur: ₹50 LPA+
                    
                    💰 SALARY PROGRESSION:
                    Entry: ₹8-15 LPA
                    Mid-level: ₹15-40 LPA
                    Senior: ₹40-80 LPA
                    C-Suite: ₹80 LPA+
                """
                else -> ""
            }
            "C" -> when (aptitudeBand) {
                4 -> """
                    
                    📊 FINANCE & OPERATIONS ROADMAP:
                    
                    📚 PROFESSIONAL TRACK (4-6 years):
                    B.Com/Economics → CA/CS/CMA → CFA/FRM (optional)
                    
                    💼 CAREER PATHS:
                    • Chartered Accountant: ₹6-40 LPA
                    • Financial Analyst: ₹8-25 LPA
                    • CFO: ₹50 LPA+
                    • Risk Manager: ₹15-60 LPA
                    
                    🏢 TOP RECRUITERS:
                    Big 4: Deloitte, PwC, EY, KPMG
                    Banks: HDFC, ICICI, SBI, Goldman Sachs
                    
                    💰 SALARY RANGES:
                    Entry: ₹4-8 LPA
                    Mid-level: ₹8-25 LPA
                    Senior: ₹25-60 LPA
                    Leadership: ₹60 LPA+
                """
                else -> ""
            }
            else -> ""
        }
    }
    
    private var showingAllOccupations = false
    
    private fun setupOccupationSelection() {
        try {
            // Get filtered occupations based on personality type
            val filteredOccupations = getFilteredOccupations(currentPersonalityCode)
            val topSeven = filteredOccupations.take(7)
            
            // Add "Show More" option if there are more than 7
            val displayList = if (filteredOccupations.size > 7) {
                topSeven + listOf("➕ Show ${filteredOccupations.size - 7} More Relevant Careers")
            } else {
                topSeven
            }
            
            // Setup spinner adapter
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, displayList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerOccupations.adapter = adapter
            
            // Setup button click listener
            binding.btnShowRoadmap.setOnClickListener {
                val selectedOccupation = binding.spinnerOccupations.selectedItem.toString()
                
                // Handle "Show More" option
                if (selectedOccupation.startsWith("➕ Show")) {
                    expandOccupationList(filteredOccupations)
                } else {
                    showOccupationRoadmap(selectedOccupation)
                }
            }
        } catch (e: Exception) {
            println("Error in setupOccupationSelection: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun expandOccupationList(allOccupations: List<String>) {
        showingAllOccupations = true
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, allOccupations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOccupations.adapter = adapter
        Toast.makeText(this, "Showing all ${allOccupations.size} relevant careers", Toast.LENGTH_SHORT).show()
    }
    
    private fun getFilteredOccupations(personalityCode: String): List<String> {
        val allOccupations = RoadmapsDatabase.getAllOccupations()
        
        // Extract primary and secondary types
        val types = if (personalityCode.contains("+")) {
            personalityCode.split("+")
        } else {
            listOf(personalityCode)
        }
        
        val primaryType = types[0]
        val secondaryType = if (types.size > 1) types[1] else null
        
        // Career keywords for each RIASEC type
        val typeKeywords = mapOf(
            "R" to listOf("engineer", "mechanic", "technician", "pilot", "architect", "construction", "automotive", "electrical", "industrial", "manufacturing"),
            "I" to listOf("scientist", "researcher", "analyst", "physician", "doctor", "psychologist", "data", "software", "developer", "mathematician", "physicist", "chemist", "biologist"),
            "A" to listOf("designer", "artist", "writer", "musician", "photographer", "interior", "graphic", "creative", "animator", "illustrator", "actor", "director"),
            "S" to listOf("teacher", "counselor", "therapist", "social", "nurse", "healthcare", "education", "psychologist", "worker", "coordinator", "instructor"),
            "E" to listOf("manager", "executive", "sales", "marketing", "business", "entrepreneur", "director", "administrator", "consultant", "lawyer", "agent"),
            "C" to listOf("accountant", "auditor", "analyst", "administrator", "clerk", "financial", "budget", "database", "compliance", "inspector", "coordinator")
        )
        
        val primaryKeywords = typeKeywords[primaryType] ?: emptyList()
        val secondaryKeywords = if (secondaryType != null) typeKeywords[secondaryType] ?: emptyList() else emptyList()
        
        // Filter occupations
        val relevantOccupations = allOccupations.filter { occupation ->
            val lowerOccupation = occupation.lowercase()
            primaryKeywords.any { keyword -> lowerOccupation.contains(keyword) } ||
            secondaryKeywords.any { keyword -> lowerOccupation.contains(keyword) }
        }
        
        println("Filtered ${relevantOccupations.size} relevant occupations for $personalityCode")
        return relevantOccupations.sorted()
    }
    
    // Removed - now using RoadmapsDatabase.getAllOccupations() with filtering
    
    private fun showOccupationRoadmap(occupation: String) {
        // Show toast message
        Toast.makeText(this, "Opening comprehensive roadmap for $occupation...", Toast.LENGTH_SHORT).show()
        
        // Open the detailed roadmap activity with comprehensive information
        val intent = Intent(this, RoadmapDetailActivity::class.java)
        intent.putExtra("occupation_title", occupation)
        startActivity(intent)
        
        // Optional: Show a quick preview in current activity (preview only)
        binding.cardRoadmapDisplay.visibility = View.GONE  // Hide preview for cleaner experience
    }
    
    private fun getSpecificOccupationRoadmap(occupation: String): String {
        return when (occupation.lowercase()) {
            "software engineer", "web developer" -> """
                🖥️ SOFTWARE ENGINEERING ROADMAP
                
                📚 EDUCATION PATH (4-6 years):
                • Bachelor's in Computer Science/IT (4 years)
                • Optional: Master's in CS/Software Engineering (2 years)
                
                🎯 SKILL DEVELOPMENT:
                Phase 1: Programming fundamentals (Java, Python, C++)
                Phase 2: Web technologies (HTML, CSS, JavaScript, React)
                Phase 3: Backend development (Node.js, databases, APIs)
                Phase 4: DevOps and cloud technologies (AWS, Docker)
                
                🏢 TOP COMPANIES:
                India: TCS, Infosys, Wipro, Google, Microsoft
                Global: FAANG companies, startups
                
                💰 SALARY PROGRESSION:
                Entry: ₹3-8 LPA
                Mid-level: ₹8-20 LPA
                Senior: ₹20-50 LPA
                Architect/Lead: ₹50+ LPA
                
                📈 CAREER PATH:
                Junior Developer → Senior Developer → Tech Lead → Architect → CTO
            """.trimIndent()
            
            "data scientist", "ai/ml engineer" -> """
                🤖 DATA SCIENCE & AI/ML ROADMAP
                
                📚 EDUCATION PATH (4-6 years):
                • Bachelor's in CS/Statistics/Mathematics (4 years)
                • Master's in Data Science/AI/ML (2 years)
                
                🎯 SKILL DEVELOPMENT:
                Phase 1: Statistics, Python, R programming
                Phase 2: Machine Learning algorithms and frameworks
                Phase 3: Deep Learning (TensorFlow, PyTorch)
                Phase 4: Big Data tools (Hadoop, Spark)
                
                🏢 TOP COMPANIES:
                India: Google, Microsoft, Amazon, Flipkart, Ola
                Global: Tesla, Netflix, Uber, OpenAI
                
                💰 SALARY PROGRESSION:
                Entry: ₹6-12 LPA
                Mid-level: ₹12-25 LPA
                Senior: ₹25-60 LPA
                Principal: ₹60+ LPA
                
                📈 CAREER PATH:
                Data Analyst → Data Scientist → Senior DS → Principal DS → Chief Data Officer
            """.trimIndent()
            
            "doctor", "surgeon" -> """
                🏥 MEDICAL CAREER ROADMAP
                
                📚 EDUCATION PATH (11-15 years):
                • MBBS (5.5 years including internship)
                • MD/MS Specialization (3 years)
                • Super-specialization (2-3 years) - Optional
                
                🎯 ENTRANCE EXAMS:
                • NEET UG for MBBS
                • NEET PG for MD/MS
                • Various fellowship exams
                
                🏥 TOP MEDICAL COLLEGES:
                India: AIIMS, JIPMER, CMC Vellore, KGMU
                Abroad: Harvard Medical, Johns Hopkins, Mayo Clinic
                
                💰 SALARY PROGRESSION:
                Resident: ₹50K-1L/month
                Consultant: ₹1-5L/month
                Senior Consultant: ₹5-15L/month
                Private Practice: ₹10L+/month
                
                📈 CAREER PATH:
                Medical Student → Intern → Resident → Consultant → Senior Consultant → Department Head
            """.trimIndent()
            
            "teacher", "professor" -> """
                📚 EDUCATION CAREER ROADMAP
                
                📚 EDUCATION PATH (4-8 years):
                • Bachelor's in subject + B.Ed (4-5 years)
                • Master's in Education/Subject (2 years)
                • PhD for Professor roles (3-5 years)
                
                🎯 CERTIFICATION REQUIREMENTS:
                • TET/CTET for school teaching
                • NET/SET for college teaching
                • State-specific teaching eligibility tests
                
                🏫 CAREER OPPORTUNITIES:
                Schools: Government, Private, International
                Colleges: Assistant Professor → Associate → Professor
                
                💰 SALARY PROGRESSION:
                School Teacher: ₹3-8 LPA
                College Lecturer: ₹4-10 LPA
                Assistant Professor: ₹6-15 LPA
                Professor: ₹15-25 LPA
                
                📈 CAREER PATH:
                Teacher → Senior Teacher → Principal/HOD → Education Administrator
            """.trimIndent()
            
            "mechanical engineer", "civil engineer", "electrical engineer" -> """
                ⚙️ ENGINEERING CAREER ROADMAP
                
                📚 EDUCATION PATH (4-6 years):
                • B.Tech/B.E. in respective engineering (4 years)
                • M.Tech for specialization (2 years) - Optional
                
                🎯 ENTRANCE EXAMS:
                • JEE Main & Advanced
                • State engineering entrance exams
                • GATE for M.Tech and PSU jobs
                
                🏭 TOP INSTITUTES:
                India: IITs, NITs, BITS Pilani, VIT
                Abroad: MIT, Stanford, UC Berkeley
                
                💰 SALARY PROGRESSION:
                Entry: ₹3-8 LPA
                Mid-level: ₹8-18 LPA
                Senior: ₹18-35 LPA
                Manager/Lead: ₹35+ LPA
                
                📈 CAREER PATH:
                Junior Engineer → Senior Engineer → Project Manager → Engineering Manager → VP Engineering
            """.trimIndent()
            
            "graphic designer", "ux/ui designer" -> """
                🎨 DESIGN CAREER ROADMAP
                
                📚 EDUCATION PATH (3-4 years):
                • Bachelor's in Design/Fine Arts (3-4 years)
                • Specialized design courses and certifications
                
                🎯 SKILL DEVELOPMENT:
                Phase 1: Design fundamentals, Adobe Creative Suite
                Phase 2: UX/UI principles, Figma, Sketch
                Phase 3: User research, prototyping, testing
                Phase 4: Design systems, advanced tools
                
                🏢 TOP COMPANIES:
                India: Flipkart, Swiggy, Zomato, Paytm
                Global: Google, Apple, Airbnb, Uber
                
                💰 SALARY PROGRESSION:
                Entry: ₹3-6 LPA
                Mid-level: ₹6-15 LPA
                Senior: ₹15-30 LPA
                Design Lead: ₹30+ LPA
                
                📈 CAREER PATH:
                Junior Designer → Senior Designer → Design Lead → Creative Director
            """.trimIndent()
            
            "business analyst", "marketing manager" -> """
                💼 BUSINESS CAREER ROADMAP
                
                📚 EDUCATION PATH (5-7 years):
                • Bachelor's in Business/Economics (3-4 years)
                • MBA from top B-school (2 years)
                • Work experience (2-3 years before MBA)
                
                🎯 ENTRANCE EXAMS:
                • CAT, XAT, GMAT for MBA
                • Company-specific aptitude tests
                
                🏢 TOP B-SCHOOLS:
                India: IIM A/B/C, ISB Hyderabad, XLRI
                Global: Harvard, Wharton, Stanford, INSEAD
                
                💰 SALARY PROGRESSION:
                Entry: ₹6-12 LPA
                Mid-level: ₹12-25 LPA
                Senior: ₹25-50 LPA
                VP/Director: ₹50+ LPA
                
                📈 CAREER PATH:
                Analyst → Senior Analyst → Manager → Senior Manager → Director → VP
            """.trimIndent()
            
            "accountant", "financial analyst" -> """
                💰 FINANCE CAREER ROADMAP
                
                📚 EDUCATION PATH (4-6 years):
                • B.Com/BBA/Economics (3 years)
                • CA/CS/CMA professional course (3-4 years)
                • CFA/FRM for specialization
                
                🎯 PROFESSIONAL CERTIFICATIONS:
                • Chartered Accountant (CA)
                • Company Secretary (CS)
                • Cost and Management Accountant (CMA)
                • Certified Financial Analyst (CFA)
                
                🏢 TOP RECRUITERS:
                Big 4: Deloitte, PwC, EY, KPMG
                Banks: HDFC, ICICI, Goldman Sachs, JP Morgan
                
                💰 SALARY PROGRESSION:
                Entry: ₹4-8 LPA
                Mid-level: ₹8-20 LPA
                Senior: ₹20-40 LPA
                Partner/CFO: ₹40+ LPA
                
                📈 CAREER PATH:
                Junior Accountant → Senior Accountant → Finance Manager → CFO
            """.trimIndent()
            
            else -> """
                🎯 GENERAL CAREER ROADMAP FOR $occupation
                
                📚 TYPICAL EDUCATION PATH:
                • Relevant Bachelor's degree (3-4 years)
                • Specialized training/certification
                • Optional Master's degree (2 years)
                
                🎯 SKILL DEVELOPMENT:
                • Core technical skills for the field
                • Soft skills (communication, leadership)
                • Industry-specific certifications
                • Continuous learning and upskilling
                
                💰 GENERAL SALARY RANGE:
                Entry Level: ₹3-8 LPA
                Mid Level: ₹8-20 LPA
                Senior Level: ₹20-40 LPA
                Leadership: ₹40+ LPA
                
                📈 TYPICAL CAREER PROGRESSION:
                Entry → Mid-level → Senior → Team Lead → Manager → Director
                
                💡 NEXT STEPS:
                1. Research specific requirements for this field
                2. Identify top institutes and companies
                3. Connect with professionals in this area
                4. Start building relevant skills and portfolio
            """.trimIndent()
        }
    }
    
    private fun getDualPersonalityCareers(personalityCode: String, aptitudeScore: Int): List<String> {
        val aptitudeBand = when {
            aptitudeScore <= 10 -> 0
            aptitudeScore <= 20 -> 1
            aptitudeScore <= 30 -> 2
            aptitudeScore <= 40 -> 3
            else -> 4
        }
        
        return when (personalityCode) {
            "R+I" -> when (aptitudeBand) {
                0 -> listOf("Field/lab helper")
                1 -> listOf("Technician support")
                2 -> listOf("Lab technician", "Quality inspector")
                3 -> listOf("Robotics engineer", "Industrial R&D")
                else -> listOf("Robotics innovator", "Manufacturing R&D leader")
            }
            "R+A" -> when (aptitudeBand) {
                0 -> listOf("Workshop helper")
                1 -> listOf("Junior craftsperson", "Assistant designer")
                2 -> listOf("Product designer", "Visual technician")
                3 -> listOf("Industrial designer", "Creative technologist")
                else -> listOf("Product innovator", "Design entrepreneur")
            }
            "R+S" -> when (aptitudeBand) {
                0 -> listOf("Community helper", "Field support")
                1 -> listOf("Caregiver assistant", "Field trainer support")
                2 -> listOf("Vocational trainer", "Program facilitator")
                3 -> listOf("Field program leader", "Healthcare trainer")
                else -> listOf("Technical community leader", "Vocational institute director")
            }
            "R+E" -> when (aptitudeBand) {
                0 -> listOf("Sales helper", "Field support")
                1 -> listOf("Junior sales engineer")
                2 -> listOf("Technical sales rep", "Operations associate")
                3 -> listOf("Operations manager", "Business development manager")
                else -> listOf("Tech entrepreneur", "Manufacturing startup founder")
            }
            "R+C" -> when (aptitudeBand) {
                0 -> listOf("Data clerk", "Filing assistant")
                1 -> listOf("Technician assistant", "Document handler")
                2 -> listOf("Compliance technician", "Inventory planner")
                3 -> listOf("Operations planner", "Compliance engineer")
                else -> listOf("Systems architect", "Senior operations strategist")
            }
            "I+A" -> when (aptitudeBand) {
                0 -> listOf("Assistant in creative research")
                1 -> listOf("Junior research designer")
                2 -> listOf("UX researcher", "Data visualization designer")
                3 -> listOf("Design researcher", "Media-tech innovator")
                else -> listOf("Research-driven creative entrepreneur")
            }
            "I+S" -> when (aptitudeBand) {
                0 -> listOf("Community research helper")
                1 -> listOf("Assistant in social research programs")
                2 -> listOf("Program evaluator", "Social data analyst")
                3 -> listOf("Education policy researcher", "Social program strategist")
                else -> listOf("Policy leader", "Data-driven social innovator")
            }
            "I+E" -> when (aptitudeBand) {
                0 -> listOf("Business data assistant")
                1 -> listOf("Junior business analyst")
                2 -> listOf("Product manager", "Business analyst")
                3 -> listOf("Tech entrepreneur", "Strategy consultant")
                else -> listOf("Innovation consultant", "Senior product strategist")
            }
            "I+C" -> when (aptitudeBand) {
                0 -> listOf("Data entry assistant")
                1 -> listOf("Junior compliance support")
                2 -> listOf("Risk analyst", "Forensic auditor")
                3 -> listOf("Senior data compliance officer")
                else -> listOf("Data governance leader", "Policy analytics director")
            }
            "A+S" -> when (aptitudeBand) {
                0 -> listOf("Arts event assistant")
                1 -> listOf("Junior PR assistant", "Art educator trainee")
                2 -> listOf("Social media manager", "Workshop trainer")
                3 -> listOf("PR manager", "Creative educator")
                else -> listOf("Brand strategist", "Media personality")
            }
            "A+E" -> when (aptitudeBand) {
                0 -> listOf("Sales-support in creative field")
                1 -> listOf("Junior creative marketing associate")
                2 -> listOf("Brand manager", "Creative marketer")
                3 -> listOf("Creative director", "Media business strategist")
                else -> listOf("Advertising head", "Creative tech entrepreneur")
            }
            "A+C" -> when (aptitudeBand) {
                0 -> listOf("Documentation helper", "Assistant content planner")
                1 -> listOf("Junior content planner", "Layout assistant")
                2 -> listOf("UX planner", "Editorial coordinator")
                3 -> listOf("Creative operations manager", "Senior UX designer")
                else -> listOf("Creative process innovator", "Design systems strategist")
            }
            "S+E" -> when (aptitudeBand) {
                0 -> listOf("Front desk assistant", "Basic sales rep")
                1 -> listOf("Junior HR support", "Junior fundraiser")
                2 -> listOf("HR associate", "Nonprofit program officer")
                3 -> listOf("Corporate trainer", "Business coach")
                else -> listOf("Politician", "Senior corporate strategist")
            }
            "S+C" -> when (aptitudeBand) {
                0 -> listOf("Office helper", "Reception support")
                1 -> listOf("HR assistant", "Basic program administrator")
                2 -> listOf("HR planner", "Program coordinator")
                3 -> listOf("School administrator", "HR operations lead")
                else -> listOf("Organizational development director")
            }
            "E+C" -> when (aptitudeBand) {
                0 -> listOf("Assistant in business admin")
                1 -> listOf("Junior business operations support")
                2 -> listOf("Business operations associate", "Junior compliance manager")
                3 -> listOf("Corporate manager", "Business process head")
                else -> listOf("CEO", "CFO", "Senior corporate strategist")
            }
            else -> {
                // Fallback for any unhandled combinations
                val types = personalityCode.split("+")
                if (types.size == 2) {
                    val primary = getScoreBasedCareers(types[0], aptitudeScore)
                    val secondary = getScoreBasedCareers(types[1], aptitudeScore)
                    (primary + secondary).distinct().take(3)
                } else {
                    listOf("Career Counselor", "Consultant", "Analyst")
                }
            }
        }
    }
    
    private fun initializeResultsGuide() {
        // Guide disabled for now
        // try {
        //     // Create Velly Bandaar guide for results page
        //     val rootView = findViewById<FrameLayout>(android.R.id.content)
        //     val resultsGuide = ResultsGuideSystem(this, rootView)
        //     resultsGuide.init()
        // } catch (e: Exception) {
        //     // Handle guide initialization error gracefully
        //     println("Guide initialization failed: ${e.message}")
        // }
    }
}

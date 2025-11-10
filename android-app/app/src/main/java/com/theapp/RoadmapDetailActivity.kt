package com.theapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.theapp.databinding.ActivityRoadmapDetailBinding
import kotlinx.coroutines.*

class RoadmapDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRoadmapDetailBinding
    private var roadmapJob: Job? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityRoadmapDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            val occupationTitle = intent?.getStringExtra("occupation_title") ?: "Unknown Occupation"
            
            // Set header with null checks
            binding.tvOccupationTitle?.text = occupationTitle
            binding.tvLoadingMessage?.text = "Loading comprehensive roadmap for $occupationTitle..."
            
            // Load roadmap
            loadRoadmapDetails(occupationTitle)
            
            // Back button with null check
            binding.btnBack?.setOnClickListener {
                finish()
            }
            
            // Share button with null check
            binding.btnShare?.setOnClickListener {
                shareRoadmap(occupationTitle)
            }
            
        } catch (e: Exception) {
            android.util.Log.e("RoadmapDetail", "Error in onCreate", e)
            setFallbackUI()
        }
    }
    
    private fun setFallbackUI() {
        try {
            binding.tvOccupationTitle?.text = "Career Roadmap"
            binding.tvLoadingMessage?.text = "Loading career information..."
            binding.progressBar?.visibility = android.view.View.GONE
            binding.tvLoadingMessage?.text = "Unable to load detailed roadmap. Please try again."
        } catch (e: Exception) {
            android.util.Log.e("RoadmapDetail", "Error in fallback UI", e)
            finish()
        }
    }
    
    private fun loadRoadmapDetails(occupation: String) {
        try {
            // Show loading state with null checks
            binding.progressBar?.visibility = android.view.View.VISIBLE
            binding.tvLoadingMessage?.visibility = android.view.View.VISIBLE
            binding.scrollViewRoadmap?.visibility = android.view.View.GONE
            
            // Cancel any existing job
            roadmapJob?.cancel()
            
            // Load roadmap asynchronously
            roadmapJob = CoroutineScope(Dispatchers.Main).launch {
                try {
                    val roadmap = withContext(Dispatchers.Default) {
                        generateDetailedRoadmap(occupation)
                    }
                    
                    // Update UI on main thread with null checks
                    if (isActive && !isDestroyed && !isFinishing) {
                        binding.progressBar?.visibility = android.view.View.GONE
                        binding.tvLoadingMessage?.visibility = android.view.View.GONE
                        binding.scrollViewRoadmap?.visibility = android.view.View.VISIBLE
                        
                        // Set roadmap content safely
                        binding.tvRoadmapContent?.text = roadmap
                    }
                    
                } catch (e: Exception) {
                    android.util.Log.e("RoadmapDetail", "Error loading roadmap", e)
                    if (isActive && !isDestroyed && !isFinishing) {
                        binding.progressBar?.visibility = android.view.View.GONE
                        binding.tvLoadingMessage?.text = "Error loading roadmap. Please try again."
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("RoadmapDetail", "Error in loadRoadmapDetails", e)
        }
    }
    
    private fun generateDetailedRoadmap(occupation: String): String {
        android.util.Log.d("RoadmapDetail", "🔍 Looking up occupation: '$occupation'")
        
        // FIRST: Try to get from 386 database using OccupationMapper
        var slug = OccupationMapper.getSlug(occupation)
        
        if (slug != null) {
            android.util.Log.d("RoadmapDetail", "✅ Found mapping: '$occupation' → '$slug'")
            val roadmap = RoadmapsDatabase.getRoadmap(slug)
            
            if (roadmap != null) {
                android.util.Log.d("RoadmapDetail", "✅ Using DATABASE roadmap (386) for: $occupation")
                return roadmap
            }
        }
        
        // SECOND: Fall back to hardcoded roadmaps
        android.util.Log.d("RoadmapDetail", "⚠️ No database roadmap, using HARDCODED for: $occupation")
        
        return when (occupation.lowercase().trim()) {
            // REALISTIC (R)
            "software engineer" -> generateSoftwareEngineerRoadmap()
            "mechanical engineer" -> generateMechanicalEngineerRoadmap()
            "civil engineer" -> generateCivilEngineerRoadmap()
            "electrical engineer" -> generateElectricalEngineerRoadmap()
            "automotive technician" -> generateAutomotiveTechnicianRoadmap()
            "carpenter" -> generateCarpenterRoadmap()
            "electrician" -> generateElectricianRoadmap()
            "pilot" -> generatePilotRoadmap()
            "architect" -> generateArchitectRoadmap()
            "industrial designer" -> generateIndustrialDesignerRoadmap()
            
            // INVESTIGATIVE (I)
            "data scientist" -> generateDataScientistRoadmap()
            "research scientist" -> generateResearchScientistRoadmap()
            "doctor" -> generateDoctorRoadmap()
            "psychologist" -> generatePsychologistRoadmap()
            "mathematician" -> generateMathematicianRoadmap()
            "physicist" -> generatePhysicistRoadmap()
            "chemist" -> generateChemistRoadmap()
            "biologist" -> generateBiologistRoadmap()
            "market research analyst" -> generateMarketResearchAnalystRoadmap()
            "software developer" -> generateSoftwareEngineerRoadmap()
            
            // ARTISTIC (A)
            "graphic designer" -> generateGraphicDesignerRoadmap()
            "ux/ui designer" -> generateUXDesignerRoadmap()
            "artist" -> generateArtistRoadmap()
            "writer" -> generateWriterRoadmap()
            "musician" -> generateMusicianRoadmap()
            "actor" -> generateActorRoadmap()
            "photographer" -> generatePhotographerRoadmap()
            "interior designer" -> generateInteriorDesignerRoadmap()
            "fashion designer" -> generateFashionDesignerRoadmap()
            "video editor" -> generateVideoEditorRoadmap()
            
            // SOCIAL (S)
            "teacher" -> generateTeacherRoadmap()
            "counselor" -> generateCounselorRoadmap()
            "social worker" -> generateSocialWorkerRoadmap()
            "nurse" -> generateNurseRoadmap()
            "therapist" -> generateTherapistRoadmap()
            "human resources manager" -> generateHRManagerRoadmap()
            "customer service representative" -> generateCustomerServiceRoadmap()
            "community outreach coordinator" -> generateCommunityOutreachRoadmap()
            "school administrator" -> generateSchoolAdministratorRoadmap()
            "healthcare administrator" -> generateHealthcareAdministratorRoadmap()
            
            // ENTERPRISING (E)
            "business manager" -> generateBusinessManagerRoadmap()
            "sales representative" -> generateSalesRepresentativeRoadmap()
            "entrepreneur" -> generateEntrepreneurRoadmap()
            "marketing manager" -> generateMarketingManagerRoadmap()
            "lawyer" -> generateLawyerRoadmap()
            "politician" -> generatePoliticianRoadmap()
            "real estate agent" -> generateRealEstateAgentRoadmap()
            "financial advisor" -> generateFinancialAdvisorRoadmap()
            "project manager" -> generateProjectManagerRoadmap()
            "operations manager" -> generateOperationsManagerRoadmap()
            
            // CONVENTIONAL (C)
            "accountant" -> generateAccountantRoadmap()
            "financial analyst" -> generateFinancialAnalystRoadmap()
            "data entry clerk" -> generateDataEntryClerkRoadmap()
            "administrative assistant" -> generateAdministrativeAssistantRoadmap()
            "librarian" -> generateLibrarianRoadmap()
            "bank teller" -> generateBankTellerRoadmap()
            "bookkeeper" -> generateBookkeeperRoadmap()
            "quality control inspector" -> generateQualityControlInspectorRoadmap()
            "office manager" -> generateOfficeManagerRoadmap()
            "records clerk" -> generateRecordsClerkRoadmap()
            
            else -> generateGenericRoadmap(occupation)
        }
    }
    
    private fun generateSoftwareEngineerRoadmap(): String {
        return """
🚀 SOFTWARE ENGINEER CAREER ROADMAP (2025 EDITION)

═══════════════════════════════════════════════
📚 PHASE 1: HIGH SCHOOL FOUNDATION (11th-12th)
═══════════════════════════════════════════════

🎯 CORE SUBJECTS:
• Mathematics: Algebra, Calculus, Discrete Math (CRITICAL)
• Physics: Logic building, problem-solving
• Computer Science: Programming fundamentals
• English: Technical communication skills

💻 PROGRAMMING JOURNEY:
Month 1-3: Python basics (most beginner-friendly)
Month 4-6: Data structures (arrays, lists, stacks)
Month 7-9: Object-oriented programming (OOP concepts)
Month 10-12: Web development basics (HTML, CSS, JavaScript)

🏆 COMPETITIONS & ACHIEVEMENTS:
• Google Code-in / Google Summer of Code
• HackerRank, CodeChef, Codeforces contests
• International Olympiad in Informatics (IOI)
• Smart India Hackathon (School Edition)

📜 CERTIFICATIONS FOR BEGINNERS:
• Python for Everybody (Coursera) - FREE
• CS50's Introduction to Computer Science (Harvard) - FREE
• Google IT Support Professional Certificate
• Microsoft Technology Associate (MTA) certifications

═══════════════════════════════════════════════
🎓 PHASE 2: UNDERGRADUATE EDUCATION (3-4 years)
═══════════════════════════════════════════════

📍 INDIA - TOP TIER:
╔══════════════════════════════════════════════╗
║ IIT Delhi, IIT Bombay, IIT Madras          ║
║ JEE Advanced Rank: Top 2000                ║
║ Package Range: ₹15-50 LPA (median ₹25L)    ║
║ International Placements: Common           ║
╚══════════════════════════════════════════════╝

• IITs (23 campuses): JEE Advanced required
• NITs (31 campuses): JEE Main rank < 10,000
• IIIT Hyderabad, Bangalore, Delhi: Elite CS programs
• BITS Pilani: BITSAT exam, excellent placements

📍 INDIA - TIER 2 (Still Excellent):
• VIT Vellore, SRM, Manipal, Thapar University
• Private universities with good placement records
• Cost: ₹2-4 LPA tuition
• Average Package: ₹6-12 LPA

📍 INTERNATIONAL OPTIONS:

🇺🇸 USA (Premier Destination):
• Top Unis: MIT, Stanford, CMU, UC Berkeley, Georgia Tech
• Cost: $50K-80K/year (tuition + living)
• Scholarships: Merit-based (up to 50% tuition)
• Work: CPT (during study) + OPT (3 years for STEM)
• Average Starting: $120K-150K

🇨🇦 CANADA (Easiest PR Path):
• Top Unis: Waterloo, Toronto, UBC, McGill
• Cost: CAD 25K-40K/year
• Work Permit: 3 years post-graduation
• PR Points: High for tech graduates
• Co-op Programs: Paid internships during study

🇩🇪 GERMANY (Tuition-FREE!):
• Top Unis: TUM, RWTH Aachen, KIT, TU Berlin
• Cost: €0-3000/year tuition (only semester fees!)
• Language: Many English programs available
• Work: 20 hours/week during study
• After Graduation: 18-month job seeker visa

🇬🇧 UK (1-Year Masters Common):
• Universities: Oxford, Cambridge, Imperial, Edinburgh
• Cost: £20K-35K/year
• Duration: 3 years bachelors
• Post-Study Work: 2-year graduate visa

🇦🇺 AUSTRALIA:
• Universities: Melbourne, UNSW, ANU
• Cost: AUD 35K-50K/year
• Work Rights: 40 hours/fortnight
• PR Pathway: Points-based system

📚 CORE CURRICULUM (Must Master):
• Data Structures & Algorithms (DSA) - CRUCIAL for interviews
• Operating Systems - Process, threads, memory
• Database Management Systems - SQL, NoSQL
• Computer Networks - TCP/IP, HTTP, networking
• Software Engineering - SDLC, Agile, design patterns
• Object-Oriented Programming - Java, C++, Python
• Web Technologies - Frontend & Backend
• System Design - Scalability, microservices

═══════════════════════════════════════════════
📈 PHASE 3: SKILL DEVELOPMENT ROADMAP
═══════════════════════════════════════════════

🗓️ YEAR 1-2 (Foundation Building):
✓ Master one programming language completely (Python/Java)
✓ Solve 200+ DSA problems on LeetCode/CodeChef
✓ Build 3-5 personal projects (GitHub portfolio)
✓ Learn Git & version control
✓ Understand basic web development (HTML/CSS/JS)
✓ First internship (startup or local company)

🗓️ YEAR 3-4 (Advanced & Specialization):
✓ Advanced DSA (graphs, dynamic programming, trees)
✓ System design fundamentals (scalability concepts)
✓ Choose specialization: Full-Stack, Mobile, ML, DevOps
✓ Contribute to open-source projects (Apache, Mozilla)
✓ Internship at product companies (FAANG/Unicorns)
✓ Build complex projects (e-commerce, SaaS app)
✓ Competitive programming (Codeforces Div 1)

🗓️ POST-GRADUATION (Career Launch):
✓ Master system design for interviews
✓ Solve 400+ LeetCode problems (all patterns)
✓ Prepare for behavioral interviews (STAR method)
✓ Network on LinkedIn (connect with engineers)
✓ Apply to 50+ companies strategically
✓ Target: FAANG, Unicorns, or high-growth startups

═══════════════════════════════════════════════
🛠️ PHASE 4: TECHNICAL SKILLS STACK (2025)
═══════════════════════════════════════════════

💻 PROGRAMMING LANGUAGES (Pick 2-3):
🔥 HOT IN 2025:
• Python - AI/ML, Backend, Scripting (Easiest to start)
• JavaScript/TypeScript - Full-stack, Frontend dominance
• Rust - Systems programming, performance (Growing fast)
• Go (Golang) - Cloud-native, microservices
• Kotlin - Android development (official language)

✓ STILL STRONG:
• Java - Enterprise, Android, Big Tech companies
• C++ - Competitive programming, systems, gaming
• Swift - iOS development
• C# - Game development (Unity), Microsoft stack

🌐 WEB DEVELOPMENT:
Frontend:
• React.js ⭐ (Most popular, high demand)
• Next.js (React framework with SSR)
• Vue.js (Easier learning curve)
• Angular (Enterprise applications)
• Tailwind CSS (Modern styling)
• TypeScript (Type-safe JavaScript)

Backend:
• Node.js + Express (JavaScript backend)
• Django / FastAPI (Python frameworks)
• Spring Boot (Java enterprise)
• Go with Gin/Echo frameworks
• ASP.NET Core (C# Microsoft stack)

🗄️ DATABASES:
SQL (Relational):
• PostgreSQL ⭐ (Most advanced open-source)
• MySQL (Wide adoption)
• SQLite (Embedded databases)

NoSQL:
• MongoDB (Document store, most popular)
• Redis (Caching, in-memory)
• Cassandra (Distributed, big data)
• Elasticsearch (Search engine)

☁️ CLOUD PLATFORMS (Pick ONE to start):
• AWS ⭐⭐⭐ (Market leader, 33% share)
  - EC2, S3, Lambda, RDS, DynamoDB
  - Most job openings require AWS
• Google Cloud Platform (GCP)
  - Strong in ML/AI services
  - Kubernetes origin (GKE)
• Microsoft Azure
  - Enterprise focused
  - Great for .NET developers

🔧 DEVOPS & TOOLS:
• Git & GitHub (Version control - MUST KNOW)
• Docker (Containerization - ESSENTIAL 2025)
• Kubernetes (Container orchestration)
• Jenkins/GitHub Actions (CI/CD)
• Terraform (Infrastructure as Code)
• Prometheus/Grafana (Monitoring)

🤖 AI/ML SKILLS (Hot in 2025!):
• TensorFlow / PyTorch (Deep learning)
• Scikit-learn (Traditional ML)
• Hugging Face Transformers (NLP)
• LangChain (LLM applications)
• OpenAI API, Anthropic Claude API
• Vector Databases (Pinecone, Weaviate)

📱 MOBILE DEVELOPMENT:
• Flutter (Cross-platform, Google)
• React Native (Cross-platform, Facebook)
• Kotlin (Native Android)
• Swift (Native iOS)

🔐 ADDITIONAL CRUCIAL SKILLS:
• REST APIs & GraphQL
• Microservices Architecture
• Message Queues (RabbitMQ, Kafka)
• Web Sockets & Real-time apps
• Security (OAuth, JWT, HTTPS)
• Testing (Jest, PyTest, JUnit)
• Agile/Scrum methodologies

═══════════════════════════════════════════════
💰 PHASE 5: SALARY EXPECTATIONS (2025 DATA)
═══════════════════════════════════════════════

📍 INDIA SALARY BREAKDOWN:

🏆 TIER 1 COMPANIES (FAANG, Top Startups):
• Fresh Graduate: ₹15-45 LPA
  - Google India: ₹20-30 LPA
  - Microsoft IDC: ₹18-25 LPA
  - Amazon: ₹18-28 LPA
  - Flipkart/Swiggy: ₹20-35 LPA

• 2-4 Years: ₹25-60 LPA
  - SDE-2 Level in FAANG
  - Includes base + stocks + bonus

• 5-8 Years (Senior): ₹40-90 LPA
  - Staff Engineer level
  - Team lead responsibilities

• 10+ Years (Principal/Architect): ₹80 LPA - ₹2.5 Cr+
  - Leadership positions
  - Technical decisions for products

💼 TIER 2 COMPANIES (Service, Product):
• Entry Level: ₹4-10 LPA
  - TCS, Infosys, Wipro: ₹3.5-6 LPA
  - Mid-tier startups: ₹6-12 LPA
• Mid Level (3-5 years): ₹10-20 LPA
• Senior Level (6-10 years): ₹20-40 LPA

📍 INTERNATIONAL SALARIES (2025):

🇺🇸 USA (Silicon Valley/Tech Hubs):
• Entry (L3/E3): $130K-180K total comp
  - Base: $110K-140K
  - Stocks: $20K-40K/year
  - Bonus: $10K-20K
• Mid (L4/E4): $200K-300K
• Senior (L5/E5): $350K-500K
• Staff (L6/E6): $500K-700K
• Principal (L7+): $700K-$1M+

FAANG Specific (Total Compensation):
• Google L3: $180K-200K
• Meta E3: $190K-210K
• Amazon SDE1: $160K-180K
• Apple ICT2: $170K-190K
• Netflix: $350K-450K (junior level!)

🇨🇦 CANADA:
• Entry: CAD 70K-95K ($52K-70K USD)
• Mid Level: CAD 95K-130K
• Senior: CAD 130K-180K
• Staff: CAD 180K-250K+

🇬🇧 UK (London):
• Entry: £40K-60K ($50K-75K USD)
• Mid Level: £60K-90K
• Senior: £90K-140K
• Staff: £140K-200K+

🇩🇪 GERMANY (Berlin/Munich):
• Entry: €50K-65K ($55K-70K USD)
• Mid Level: €65K-90K
• Senior: €90K-120K
• Staff: €120K-160K+

🇦🇺 AUSTRALIA:
• Entry: AUD 70K-95K
• Mid Level: AUD 95K-130K
• Senior: AUD 130K-170K

🇸🇬 SINGAPORE:
• Entry: SGD 55K-75K
• Mid Level: SGD 80K-120K
• Senior: SGD 130K-180K

💡 SALARY NEGOTIATION TIPS:
• Research market rates (Levels.fyi, Glassdoor)
• Always ask for 15-20% more than current
• Include stocks/ESOPs in total compensation
• Compare total comp, not just base salary
• Consider cost of living (Bangalore vs Mumbai)

═══════════════════════════════════════════════
🏢 PHASE 6: TOP RECRUITERS & COMPANIES (2025)
═══════════════════════════════════════════════

🌟 FAANG+ (Highest Paying):
╔══════════════════════════════════════════════╗
║ META (Facebook): ₹18-45 LPA fresh          ║
║ AMAZON: ₹18-30 LPA, massive hiring         ║
║ APPLE: ₹20-35 LPA, design focused          ║
║ NETFLIX: ₹35-60 LPA, senior hires only     ║
║ GOOGLE: ₹20-30 LPA, best work culture      ║
╚══════════════════════════════════════════════╝

💎 FAANG-EQUIVALENT (Equal/Better Pay):
• Microsoft (India Development Center)
• Adobe (Product development)
• NVIDIA (AI/Graphics)
• Intel (Chip design)
• Qualcomm (Mobile tech)
• Salesforce (CRM leader)
• Oracle (Database/Cloud)
• VMware (Virtualization)

🦄 INDIAN UNICORNS (₹15-35 LPA):
• Flipkart, PhonePe - E-commerce
• Swiggy, Zomato - Food delivery
• CRED - Fintech
• Razorpay, Paytm - Payments
• Ola, Uber India - Mobility
• Dream11 - Gaming
• Meesho, Udaan - B2B commerce

🚀 HIGH-GROWTH STARTUPS (₹10-25 LPA):
• Zerodha (Fintech)
• Postman (API platform)
• Browserstack (Testing)
• Freshworks (SaaS)
• Zoho (SaaS giant)
• Chargebee (Subscription)
• Razorpay (Payments)

💼 PRODUCT COMPANIES (₹8-18 LPA):
• Atlassian (Jira, Confluence)
• Intuit (QuickBooks)
• PayPal (Payments)
• Booking.com (Travel)
• Uber (Mobility)
• LinkedIn (Professional network)
• Twitter/X (Social media)

🏛️ INDIAN IT SERVICES (₹3-8 LPA Entry):
• TCS (Largest Indian IT)
• Infosys (Innovation focused)
• Wipro (Digital services)
• HCL Technologies
• Tech Mahindra
• Capgemini, Cognizant
• Accenture

🌐 GLOBAL CONSULTING (₹8-15 LPA):
• McKinsey Digital
• BCG Digital Ventures
• Bain & Company
• Deloitte Digital
• PwC Technology

💰 FINTECH & BANKS (₹10-25 LPA):
• Goldman Sachs (Bangalore)
• JP Morgan Chase
• Morgan Stanley
• Barclays Technology
• Deutsche Bank
• HSBC Technology
• American Express

🎮 GAMING COMPANIES (₹8-20 LPA):
• Zynga India
• EA Sports
• Ubisoft
• Dream11
• Mobile Premier League (MPL)
• Games24x7

🔬 AI/ML FOCUSED (₹15-40 LPA):
• OpenAI (Remote possible)
• Anthropic
• Hugging Face
• DataRobot
• C3.ai
• Scale AI

═══════════════════════════════════════════════
🎯 PHASE 7: SPECIALIZATION PATHS (Choose Wisely!)
═══════════════════════════════════════════════

🌐 FULL-STACK DEVELOPMENT (Most Versatile):
✓ Skills: React/Next.js, Node.js, PostgreSQL, AWS
✓ Job Market: HIGHEST demand, all companies need
✓ Salary: ₹8-35 LPA in India
✓ Pros: Can build complete products alone
✓ Cons: Need to know multiple technologies
✓ Best for: People who like variety

📱 MOBILE DEVELOPMENT (Specialized High-Pay):
✓ Skills: Flutter/React Native or Native (iOS/Android)
✓ Job Market: Strong demand, less competition
✓ Salary: ₹10-40 LPA
✓ Pros: Less competition than web dev
✓ Cons: Platform-specific knowledge needed
✓ Best for: UI-focused developers

🤖 AI/ML ENGINEERING (HOTTEST in 2025!):
✓ Skills: Python, TensorFlow/PyTorch, LangChain, LLMs
✓ Job Market: EXPLODING with ChatGPT revolution
✓ Salary: ₹15-60 LPA (highest paying!)
✓ Pros: Cutting-edge work, highest salaries
✓ Cons: Requires strong math background
✓ Best for: People good at math + coding

☁️ CLOUD/DEVOPS ENGINEERING (Infrastructure):
✓ Skills: AWS/GCP/Azure, Kubernetes, Docker, Terraform
✓ Job Market: Very high demand, every company needs
✓ Salary: ₹10-45 LPA
✓ Pros: Critical role, good job security
✓ Cons: On-call duties, troubleshooting stress
✓ Best for: People who like systems & automation

📊 DATA ENGINEERING (Big Data Focus):
✓ Skills: Python, SQL, Spark, Kafka, Airflow
✓ Job Market: Growing with data explosion
✓ Salary: ₹12-50 LPA
✓ Pros: High demand, work with big data
✓ Cons: Can be repetitive ETL work
✓ Best for: People who like data systems

🔐 CYBERSECURITY ENGINEERING (Security Focus):
✓ Skills: Ethical hacking, penetration testing, security
✓ Job Market: Growing with cyber threats
✓ Salary: ₹8-40 LPA
✓ Pros: Exciting work, critical role
✓ Cons: Constantly learning new threats
✓ Best for: People interested in security

🎮 GAME DEVELOPMENT (Creative + Technical):
✓ Skills: Unity/Unreal, C++/C#, 3D math, physics
✓ Job Market: Niche but growing in India
✓ Salary: ₹6-30 LPA
✓ Pros: Creative and fun work
✓ Cons: Can involve crunch time
✓ Best for: Gaming enthusiasts

⚡ BACKEND SPECIALIZATION (Scalability Expert):
✓ Skills: System design, databases, microservices
✓ Job Market: High demand in product companies
✓ Salary: ₹12-50 LPA
✓ Pros: Deep technical work, scalability challenges
✓ Cons: Less visible than frontend
✓ Best for: People who love architecture

🎨 FRONTEND SPECIALIZATION (UI/UX Engineering):
✓ Skills: React, TypeScript, CSS, animations
✓ Job Market: High demand, competitive
✓ Salary: ₹8-35 LPA
✓ Pros: Visual results, creative work
✓ Cons: Frequent framework changes
✓ Best for: Visual-minded developers

🏗️ BLOCKCHAIN DEVELOPMENT (Emerging):
✓ Skills: Solidity, Web3, Ethereum, Smart Contracts
✓ Job Market: Niche but high-paying
✓ Salary: ₹15-60 LPA
✓ Pros: Cutting edge, high pay
✓ Cons: Market volatility, uncertain future
✓ Best for: Risk-takers & early adopters

═══════════════════════════════════════════════
🌟 PHASE 8: CAREER PROGRESSION LADDER
═══════════════════════════════════════════════

INDIVIDUAL CONTRIBUTOR (IC) PATH:
Year 0-2: Junior/Associate Software Engineer
  → Learn tech stack, fix bugs, small features
  → Salary: ₹4-12 LPA

Year 2-5: Software Engineer/SDE-2
  → Own features end-to-end, mentor juniors
  → Salary: ₹12-30 LPA

Year 5-8: Senior Software Engineer/SDE-3
  → Design systems, lead technical decisions
  → Salary: ₹30-60 LPA

Year 8-12: Staff Engineer (L6)
  → Multi-team impact, architecture decisions
  → Salary: ₹60-100 LPA

Year 12+: Principal Engineer (L7)
  → Organization-wide technical vision
  → Salary: ₹100 LPA - ₹2 Cr+

Year 15+: Distinguished Engineer/Fellow
  → Industry-level influence, rare position
  → Salary: ₹2-5 Cr+

MANAGEMENT PATH (Alternative):
Year 3-5: Tech Lead/Team Lead
  → Lead 3-5 engineers, technical + people management
  → Salary: ₹18-35 LPA

Year 5-8: Engineering Manager
  → Manage 8-12 engineers, multiple teams
  → Salary: ₹30-60 LPA

Year 8-12: Senior Engineering Manager
  → Multiple teams, org-level planning
  → Salary: ₹50-90 LPA

Year 12+: Director of Engineering
  → Department-level responsibility
  → Salary: ₹80 LPA - ₹1.5 Cr

Year 15+: VP Engineering/CTO
  → Company-wide technical leadership
  → Salary: ₹1.5-5 Cr + equity

💡 SWITCHING PATHS:
You can switch between IC and Management paths at senior levels!

═══════════════════════════════════════════════
🌍 PHASE 9: INTERNATIONAL OPPORTUNITIES (2025)
═══════════════════════════════════════════════

🇺🇸 USA - TECH MECCA:
Work Visa Options:
• H1-B Visa (Lottery): 85,000 visas/year
  - Bachelor's quota: 65,000
  - Master's quota: 20,000 (better odds!)
  - Stay: 3 years, renewable once (6 years total)
  - Green Card path: Available

• L1 Visa (Internal Transfer): Work 1 year in India office
  - Transfer to US office guaranteed
  - Path to Green Card faster than H1B

• O1 Visa (Extraordinary Ability): For exceptional talent
  - No lottery, direct approval
  - Publications, patents, recognition required

Best States for Tech:
• California: Silicon Valley, highest salaries
• Washington: Seattle (Amazon, Microsoft)
• Texas: Austin (Tesla, Oracle), no state tax!
• New York: Fintech, startups
• Massachusetts: Boston tech scene

🇨🇦 CANADA - EASIEST PERMANENT RESIDENCE:
Express Entry Points System:
• Age (max 12 points): Under 30 best
• Education (max 25 points): Master's gets 23
• Work Experience (max 15 points): 3+ years optimal
• Language (max 28 points): IELTS 8+ band
• Job Offer: +50-200 points (huge boost!)

Score 470+ = Guaranteed PR invitation
Tech jobs get priority processing!

Popular Cities:
• Toronto: Most jobs, expensive
• Vancouver: Tech hub, beautiful, expensive
• Montreal: French + English, affordable
• Waterloo: Tech startup scene
• Calgary: Growing tech, affordable

🇩🇪 GERMANY - FREE EDUCATION + EU ACCESS:
Blue Card Requirements:
• Bachelor's degree (recognized)
• Job offer with €45,300+ salary (2025)
• IT gets relaxed requirements!

Benefits:
• Permanent residence after 2 years (with German B1)
• Free healthcare, social security
• Work anywhere in EU after PR
• Bring family immediately

Job Search Visa:
• 6-month visa to find jobs
• Available after German degree

🇦🇺 AUSTRALIA - POINTS-BASED PR:
Skilled Migration:
• Points test (pass mark: 65)
• Age: Under 32 gets maximum points
• Work experience: 3-5 years optimal
• Education: Bachelor's minimum
• English: IELTS 7+ (PTE 65+)

Tech hubs:
• Sydney: Largest tech scene
• Melbourne: Startup culture
• Brisbane: Growing, affordable

🇬🇧 UK - 2-YEAR POST-STUDY VISA:
Graduate Route:
• Study in UK → 2-year work visa automatic
• Switch to Skilled Worker visa later
• Salary threshold: £38,700 (2024)

Skilled Worker Visa:
• Job offer required
• Tech jobs on shortage list (easier)
• Path to settlement after 5 years

🇸🇬 SINGAPORE - ASIA TECH HUB:
Employment Pass (EP):
• Minimum salary: SGD 5,000/month
• Tech professionals: High approval rate
• Permanent Residence after 2-3 years
• Great base for Asian market

🇳🇱 NETHERLANDS - 30% TAX RULING:
Highly Skilled Migrant Visa:
• Salary: €41,954+ (2024)
• 30% of salary TAX-FREE for 5 years!
• English widely spoken
• Amsterdam: Fintech + startups

💰 TAX COMPARISON (on $100K salary):
• USA (California): ~$30K tax
• Canada: ~$25K tax
• Germany: ~$30K tax
• UK: ~$28K tax
• Singapore: ~$8K tax (lowest!)
• Netherlands: ~$20K (with 30% ruling)

🌐 REMOTE WORK OPTIONS (2025):
Companies hiring remotely from India:
• GitLab (100% remote, pays SF salaries!)
• Automattic (WordPress)
• Zapier
• Toptal
• Turing.com
• Remote.com

Salary range: $40K-120K USD working from India!

═══════════════════════════════════════════════
🚀 PHASE 10: PATHS TO EXCELLENCE
═══════════════════════════════════════════════

🌟 BECOME A 10X ENGINEER:

1. OPEN SOURCE CONTRIBUTIONS:
✓ Start with good first issues
✓ Contribute to projects you use daily
✓ Popular projects: React, TensorFlow, Kubernetes
✓ Build your GitHub profile (green squares!)
✓ Benefits: Recognition, learning, job offers

Success Story: Dan Abramov (React core team) got hired by Facebook through open source!

2. TECHNICAL BLOGGING:
✓ Platforms: Medium, Dev.to, HashNode
✓ Write about what you learn
✓ Tutorials, project breakdowns, tech comparisons
✓ Benefits: Personal brand, monetization, job offers

Top Tech Bloggers earn $5K-20K/month!

3. BUILD SIDE PROJECTS:
✓ SaaS products (recurring revenue)
✓ Mobile apps (Play Store/App Store)
✓ Chrome extensions
✓ Open source tools

Success Stories:
• Pieter Levels: $5M ARR from side projects
• Danny Postma: $100K/month from Headshot AI

4. YOUTUBE TECH CHANNEL:
✓ Programming tutorials
✓ System design explanations
✓ Career advice for developers
✓ Code review videos

Top Channels: TechLead ($50K/month), Fireship, ThePrimeagen

5. COMPETITIVE PROGRAMMING:
✓ Codeforces (reach Grandmaster)
✓ TopCoder
✓ Google Code Jam, Meta Hacker Cup
✓ ACM ICPC

Benefits: Google/Meta interviews get easier!

6. SPEAKING AT CONFERENCES:
✓ Start with local meetups
✓ Apply to tech conferences
✓ Google I/O, AWS re:Invent, React Conf
✓ Benefits: Recognition, networking, free travel

7. FREELANCING DURING COLLEGE:
Platforms:
• Upwork: $30-150/hour
• Toptal: $80-200/hour (vetted)
• Fiverr: Good for beginners
• Freelancer.com

Build portfolio → charge higher → passive income!

═══════════════════════════════════════════════
🎓 PHASE 11: CERTIFICATIONS (Worth Your Time)
═══════════════════════════════════════════════

☁️ CLOUD CERTIFICATIONS (Highest ROI):

AWS:
1. AWS Certified Solutions Architect - Associate
   → Cost: $150 | Duration: 2-3 months study
   → Salary Boost: +₹3-5 LPA
   → Most valuable cloud cert

2. AWS Certified Developer - Associate
   → Cost: $150 | Focuses on app development
   
3. AWS Certified DevOps Engineer - Professional
   → Cost: $300 | Advanced level
   → Salary: +₹5-8 LPA

Google Cloud:
• Associate Cloud Engineer: $125
• Professional Cloud Architect: $200
  → Growing demand, less competition than AWS

Azure:
• Azure Fundamentals (AZ-900): $99
• Azure Developer Associate: $165
  → Great for enterprise jobs

🤖 AI/ML CERTIFICATIONS (Hot in 2025!):
• TensorFlow Developer Certificate: $100
• AWS Machine Learning Specialty: $300
• Google ML Engineer Certificate: $200
• Deep Learning Specialization (Coursera): $49/month

💻 PROGRAMMING CERTIFICATIONS:
• Oracle Certified Java Programmer: $245
  → Still valuable for enterprise
• Python Institute PCAP: $295
• Meta Front-End Developer: $39/month
• Meta Back-End Developer: $39/month

🔐 SECURITY CERTIFICATIONS:
• Certified Ethical Hacker (CEH): $1,199
  → High demand, ₹15-30 LPA salaries
• CompTIA Security+: $392
• CISSP: $749 (for experienced)

🎯 WORTH IT?
AWS/Cloud certs: YES (30-50% salary increase!)
AI/ML certs: YES (hottest field 2025)
Programming certs: MAYBE (portfolio matters more)
Security certs: YES (specialized, high-pay)

═══════════════════════════════════════════════
📚 PHASE 12: LEARNING RESOURCES (2025 UPDATED)
═══════════════════════════════════════════════

💻 CODING PRACTICE (MUST DO):

LeetCode (⭐⭐⭐⭐⭐):
• Free + Premium ($35/month)
• 3000+ problems
• Company-specific questions
• Mock interviews
Goal: Solve 300+ problems for FAANG interviews

HackerRank:
• Free platform
• Interview preparation kits
• Skill certifications

CodeChef/Codeforces:
• Competitive programming
• Monthly contests
• Rating system

📖 MUST-READ BOOKS:

For Beginners:
1. "Python Crash Course" - Eric Matthes
2. "Clean Code" - Robert Martin
3. "The Pragmatic Programmer" - Hunt & Thomas

For Interviews:
1. "Cracking the Coding Interview" - Gayle McDowell
2. "System Design Interview" - Alex Xu (Vol 1 & 2)
3. "Grokking Algorithms" - Aditya Bhargava

For Advanced:
1. "Designing Data-Intensive Applications" - Martin Kleppmann
2. "The Phoenix Project" - Gene Kim (DevOps)
3. "Building Microservices" - Sam Newman

🎥 ONLINE LEARNING PLATFORMS:

Free:
• freeCodeCamp.org (⭐⭐⭐⭐⭐)
• The Odin Project
• CS50 Harvard
• MIT OpenCourseWare
• YouTube: Traversy Media, Web Dev Simplified

Paid (Worth It):
• Udemy: $10-15 courses (wait for sales!)
  → "Complete Web Development Bootcamp"
  → "100 Days of Code"
• Coursera: $39-79/month
  → Google Career Certificates
  → Meta Certifications
• Pluralsight: $29/month (enterprise quality)
• Frontend Masters: $39/month (advanced)

🎬 YOUTUBE CHANNELS (Free Gold):
• Fireship: Quick tech updates (5 min videos)
• ThePrimeagen: System design, coding
• Web Dev Simplified: Frontend tutorials
• Traversy Media: Full-stack projects
• TechLead: Career advice (controversial but honest)
• Coding with Mosh: Beginner-friendly

🐦 TWITTER/X FOLLOWS (Stay Updated):
• @ThePrimeagen
• @dan_abramov (React creator)
• @kentcdodds
• @addyosmani (Google engineer)
• @swyx (AI + Web)

📰 NEWSLETTERS (Daily Tech Updates):
• TLDR: 3-minute tech news
• Bytes: Weekly web dev
• Software Lead Weekly
• Hacker Newsletter

💬 COMMUNITIES (Ask Questions):
• Stack Overflow
• Reddit: r/learnprogramming, r/cscareerquestions
• Discord: 100devs, Reactiflux
• Dev.to community

🎯 6-MONTH FAANG PREPARATION PLAN:
Month 1-2: DSA basics (arrays, strings, linked lists)
Month 3-4: Advanced DSA (trees, graphs, DP)
Month 5: System design + Mock interviews
Month 6: Company-specific prep + Applications

═══════════════════════════════════════════════
💡 SUCCESS FACTORS & FINAL TIPS
═══════════════════════════════════════════════

🔥 WHAT MAKES A SUCCESSFUL SOFTWARE ENGINEER:

Technical Skills (40%):
✓ Strong DSA fundamentals
✓ System design knowledge
✓ Clean code practices
✓ Testing & debugging skills

Soft Skills (30%):
✓ Communication (explaining tech to non-tech)
✓ Teamwork & collaboration
✓ Time management
✓ Problem-solving approach

Mindset (30%):
✓ Continuous learning (tech changes fast!)
✓ Curiosity & experimentation
✓ Resilience (debugging can be frustrating)
✓ Passion for building things

🎯 ACTIONABLE TIPS FOR 2025:

1. START EARLY:
   → Begin coding in 11th grade
   → Build 5+ projects before graduation
   → Contribute to open source in college

2. FOCUS ON FUNDAMENTALS:
   → DSA > Frameworks
   → Computer Science basics > Trendy tech
   → Problem-solving > Memorization

3. BUILD IN PUBLIC:
   → GitHub profile = your resume
   → Tweet about your learning journey
   → Write blogs about projects

4. NETWORK ACTIVELY:
   → LinkedIn: Connect with engineers
   → Twitter: Engage with tech community
   → Meetups: Attend local tech events

5. INTERVIEW CONTINUOUSLY:
   → Apply to 50+ companies
   → Practice mock interviews
   → Learn from rejections

6. SPECIALIZE BUT STAY VERSATILE:
   → Deep expertise in one area
   → Broad knowledge of tech ecosystem
   → T-shaped engineer concept

🚫 COMMON MISTAKES TO AVOID:

❌ Tutorial hell (watching videos without coding)
❌ Not building projects (theory ≠ practice)
❌ Ignoring DSA (thinking frameworks are enough)
❌ Not preparing for interviews systematically
❌ Waiting for "perfect" skills before applying
❌ Comparing yourself to others (focus on growth)
❌ Burning out (balance is key!)

✅ FINAL ENCOURAGEMENT:

Software Engineering is one of the BEST careers in 2025:
✓ High salaries (₹15-60 LPA+ achievable)
✓ Remote work possibilities
✓ Global opportunities
✓ Continuous learning & growth
✓ Create impactful products
✓ Financial freedom achievable in 5-7 years

"The best time to start was yesterday. 
The second best time is NOW!" 🚀

═══════════════════════════════════════════════
📞 QUICK REFERENCE SUMMARY
═══════════════════════════════════════════════

⏰ TIMELINE: 4 years degree + 2-3 years = Senior Engineer
💰 SALARY: ₹3-8 LPA → ₹80 LPA+ possible in 10 years
🎓 EDUCATION: IIT/NIT best, but skills > college name
🌍 ABROAD: USA ($130K+), Canada (Easy PR), Germany (Free!)
🛠️ SKILLS: DSA + System Design + 1 Specialization
🏢 COMPANIES: FAANG, Unicorns, Startups (₹15-45 LPA)
📚 LEARN: LeetCode, System Design, Open Source
🎯 GOAL: 300+ LeetCode, 5+ projects, Strong GitHub

"Code. Build. Ship. Repeat." 💻
        """.trimIndent()
    }
    
    private fun generateDataScientistRoadmap(): String {
        return """
📊 DATA SCIENTIST CAREER ROADMAP (2025 EDITION)

═══════════════════════════════════════════════
📚 PHASE 1: FOUNDATION (High School & Bachelor's)
═══════════════════════════════════════════════

🎯 HIGH SCHOOL (11th-12th) - CRITICAL PREPARATION:
• Mathematics: Statistics, Probability, Calculus (MUST EXCEL)
• Computer Science: Python programming basics
• Physics: Optional but helpful for ML concepts
• Projects: Simple data analysis (surveys, Excel charts)
• Competitions: Kaggle beginner competitions, data olympiads

🎓 UNDERGRADUATE PATHS (Choose Wisely):

OPTION 1: Computer Science/Engineering (Most Common)
• Best for: Coding-heavy data science roles
• Entrance: JEE, BITSAT for engineering colleges
• Focus: Programming + Math + ML courses

OPTION 2: Mathematics/Statistics (Pure Math)
• Best for: Research-oriented data science
• Top Institutes: ISI Kolkata, CMI Chennai, IISc Bangalore
• Focus: Advanced statistics, probability theory

OPTION 3: Economics/Finance with Data Focus
• Best for: Business analytics, fintech data science
• Top: IIM Indore (IPM), SRCC, St. Stephen's
• Focus: Business + Statistics + Programming

OPTION 4: Physics/Engineering Physics
• Best for: Computational physics, simulations
• Many ML concepts come from physics!

🏆 TOP INSTITUTES FOR DATA SCIENCE IN INDIA:
Tier 1:
• IITs (Delhi, Bombay, Madras) - B.Tech CS with ML specialization
• IISc Bangalore - Research-focused
• ISI Kolkata - Pure statistics, highly respected
• CMI Chennai - Mathematics & CS

Tier 2:
• IIIT Hyderabad, Bangalore - Excellent CS programs
• BITS Pilani - Good placements
• Top NITs - Value for money

INTERNATIONAL:
• USA: Stanford, MIT, CMU, UC Berkeley ($50K-80K/year)
• Canada: Waterloo, Toronto, UBC (CAD 25K-40K/year)
• UK: Oxford, Imperial, Cambridge (£20K-35K/year)
• Germany: TUM, RWTH Aachen (€0-3K/year - tuition free!)

═══════════════════════════════════════════════
📈 PHASE 2: SKILL DEVELOPMENT JOURNEY
═══════════════════════════════════════════════

YEAR 1-2 (Foundation Building):
✓ Master Python (Pandas, NumPy, Matplotlib)
✓ Learn SQL deeply (joins, window functions, CTEs)
✓ Statistics fundamentals (hypothesis testing, distributions)
✓ Excel/Google Sheets advanced (pivot tables, formulas)
✓ First internship (analytics role, even unpaid)
✓ Build portfolio projects (Kaggle datasets)

YEAR 3-4 (Advanced Skills):
✓ Machine Learning (Scikit-learn, all algorithms)
✓ Deep Learning basics (TensorFlow/PyTorch)
✓ Big Data tools (Spark fundamentals)
✓ Cloud platforms (AWS/GCP basics)
✓ Kaggle competitions (aim for top 10%)
✓ Internship at product companies (₹50K-80K/month)

POST-GRADUATION (Career Launch):
✓ Advanced ML/DL projects
✓ MLOps and model deployment
✓ Business understanding (talk to business teams!)
✓ Communication skills (explain ML to non-technical)
✓ Portfolio with 5+ end-to-end projects
✓ Target: FAANG, Unicorns, or consulting firms

═══════════════════════════════════════════════
🛠️ PHASE 3: TECHNICAL SKILLS ARSENAL (2025)
═══════════════════════════════════════════════

💻 PROGRAMMING LANGUAGES (MUST KNOW):
• Python ⭐⭐⭐⭐⭐ (MANDATORY)
  - Most used in data science (80%+ jobs)
  - Libraries: Pandas, NumPy, Scikit-learn
• SQL ⭐⭐⭐⭐⭐ (CRITICAL)
  - Data extraction, joins, window functions
  - PostgreSQL, MySQL knowledge
• R (Optional but good for statistics)
  - Statistical modeling, academic research
• Scala (Advanced - for big data engineering)

📊 DATA MANIPULATION & ANALYSIS:
• Pandas: DataFrames, groupby, merge, pivot
• NumPy: Arrays, vectorization, linear algebra
• SQL: Advanced queries, optimization
• Excel/Google Sheets: Business tool, know well

🤖 MACHINE LEARNING LIBRARIES:
Traditional ML:
• Scikit-learn ⭐⭐⭐⭐⭐ (MUST MASTER)
  - Classification, regression, clustering
  - All basic algorithms
• XGBoost, LightGBM (Gradient boosting - very powerful)
• CatBoost (Categorical data)

Deep Learning:
• TensorFlow/Keras (Industry standard)
• PyTorch ⭐ (Research & production, growing fast)
• Hugging Face Transformers (NLP - HOT in 2025!)
• Fast.ai (Easy to start deep learning)

📈 DATA VISUALIZATION:
• Matplotlib, Seaborn (Python plotting)
• Plotly (Interactive charts)
• Tableau ⭐⭐⭐ (Business tool, very common in companies)
• Power BI (Microsoft ecosystem)
• Looker, Data Studio (Google)

🗄️ DATABASES & BIG DATA:
SQL Databases:
• PostgreSQL, MySQL (relational)
• Know: Joins, CTEs, window functions, indexes

NoSQL:
• MongoDB (document store)
• Redis (caching)

Big Data:
• Apache Spark ⭐⭐⭐ (VERY IMPORTANT for big data roles)
• Hadoop (older but still used)
• Kafka (streaming data)
• Airflow (workflow orchestration)

☁️ CLOUD PLATFORMS (Pick ONE to master):
AWS:
• S3 (data storage)
• EC2 (compute)
• SageMaker (ML platform)
• Redshift (data warehouse)

Google Cloud:
• BigQuery ⭐⭐⭐ (amazing for analytics!)
• Cloud Storage
• Vertex AI (ML platform)

Azure:
• Azure ML Studio
• Azure Synapse (analytics)

🧪 MLOps & DEPLOYMENT (Hot in 2025!):
• Docker (containerization)
• Kubernetes (orchestration)
• MLflow (experiment tracking)
• Weights & Biases (ML experiment management)
• FastAPI/Flask (API creation)
• Streamlit (quick ML apps)
• AWS Lambda, Cloud Functions (serverless)

🔥 TRENDING IN 2025:
• Large Language Models (LLMs) - ChatGPT, GPT-4
• LangChain (LLM applications framework)
• Vector Databases (Pinecone, Weaviate, ChromaDB)
• Prompt Engineering (AI prompt optimization)
• Retrieval Augmented Generation (RAG)
• AutoML tools (H2O.ai, AutoGluon)

═══════════════════════════════════════════════
💰 PHASE 4: SALARY EXPECTATIONS (2025 DATA)
═══════════════════════════════════════════════

📍 INDIA SALARY BREAKDOWN:

🏆 TIER 1 COMPANIES (FAANG, Top Startups):
• Fresh Graduate: ₹12-35 LPA
  - Google: ₹15-25 LPA
  - Microsoft: ₹12-22 LPA
  - Amazon: ₹12-20 LPA
  - Flipkart/Swiggy: ₹15-28 LPA

• 2-4 Years: ₹25-50 LPA
  - Data Scientist II level
  - Include stocks + bonus

• 5-8 Years (Senior): ₹40-80 LPA
  - Lead data scientist
  - Team management

• 10+ Years (Principal/Staff): ₹80 LPA - ₹2 Cr+
  - ML strategy, research leadership

💼 TIER 2 COMPANIES & CONSULTING:
• Entry Level: ₹6-15 LPA
  - Analytics firms: ₹6-10 LPA
  - Mid-tier startups: ₹8-15 LPA
• McKinsey/BCG/Bain: ₹18-30 LPA fresh
• Accenture/Deloitte: ₹8-15 LPA
• Finance (Goldman, JP Morgan): ₹15-25 LPA

📍 INTERNATIONAL SALARIES (2025):

🇺🇸 USA (Silicon Valley/NYC):
• Entry (DS I): $120K-160K total comp
  - Base: $100K-130K
  - Stock: $20K-30K/year
  - Bonus: $10K-20K
• Mid (DS II): $180K-250K
• Senior (DS III): $250K-350K
• Staff DS: $350K-500K
• Principal DS: $500K-800K+

FAANG Data Science:
• Google L3: $160K-190K
• Meta E3: $170K-200K
• Netflix: $300K-400K (they pay TOP!)
• Apple: $150K-180K

🇨🇦 CANADA:
• Entry: CAD 75K-100K
• Mid: CAD 100K-140K
• Senior: CAD 140K-200K

🇬🇧 UK (London):
• Entry: £45K-65K
• Mid: £65K-100K
• Senior: £100K-150K

🇸🇬 SINGAPORE:
• Entry: SGD 70K-100K
• Mid: SGD 100K-150K
• Senior: SGD 150K-220K

═══════════════════════════════════════════════
🏢 PHASE 5: TOP RECRUITERS & OPPORTUNITIES
═══════════════════════════════════════════════

🌟 FAANG & TOP TECH:
• Google (ML Engineer, Applied Scientist)
• Meta (Data Scientist, ML Engineer)
• Amazon (Data Scientist, Applied Scientist)
• Microsoft (Data & Applied Scientist)
• Netflix (Analytics Engineer - highest pay!)
• Apple (ML Scientist)
• NVIDIA (AI Research Scientist)
• OpenAI, Anthropic (LLM specialists)

🦄 INDIAN UNICORNS (₹15-35 LPA):
• Flipkart, Myntra - E-commerce recommendation systems
• Swiggy, Zomato - Delivery optimization, pricing
• CRED, Paytm - Fraud detection, credit scoring
• Dream11, MPL - Gaming analytics
• Ola, Uber - Route optimization, dynamic pricing
• Meesho, Udaan - Demand forecasting

💼 CONSULTING FIRMS (₹18-35 LPA):
• McKinsey Analytics (McA)
• BCG Gamma (Data Science arm)
• Bain Advanced Analytics
• Deloitte AI & Data
• EY Parthenon Analytics

💰 FINANCE & FINTECH (₹15-40 LPA):
• Goldman Sachs (Quantitative strategies)
• JP Morgan Chase (AI/ML team)
• Morgan Stanley (Data Science)
• American Express (Fraud detection)
• Visa, Mastercard (Payment analytics)
• Razorpay, PhonePe (Fintech analytics)

🛒 E-COMMERCE & RETAIL (₹12-30 LPA):
• Amazon (Pricing, recommendations)
• Walmart Labs (Supply chain analytics)
• Flipkart (Personalization)
• BigBasket, Blinkit (Demand forecasting)

🎯 PRODUCT ANALYTICS (₹10-25 LPA):
• LinkedIn (Product data science)
• Twitter/X (Recommendation algorithms)
• Spotify India (Music recommendations)
• YouTube (Video recommendations)

🏥 HEALTHCARE AI (Growing Field):
• Siemens Healthineers
• Philips Healthcare
• Apollo Digital Health
• Diagnostic AI startups

⏰ TIMELINE: 4 years degree + 1-2 years = Senior DS role
💡 TIP: Consulting gives fastest growth, Tech gives highest ceiling!

═══════════════════════════════════════════════
🎯 PHASE 6: SPECIALIZATION PATHS
═══════════════════════════════════════════════

🤖 MACHINE LEARNING ENGINEER (Most Common):
✓ Focus: Model development, deployment, MLOps
✓ Salary: ₹12-50 LPA
✓ Skills: PyTorch/TensorFlow, Docker, Kubernetes
✓ Best for: People who like engineering + math

📊 BUSINESS INTELLIGENCE ANALYST:
✓ Focus: Business metrics, dashboards, SQL
✓ Salary: ₹8-25 LPA
✓ Skills: SQL, Tableau, Power BI, Excel
✓ Best for: Business-minded, less coding

🔬 RESEARCH SCIENTIST (AI/ML):
✓ Focus: New algorithms, research papers
✓ Salary: ₹20-80 LPA (PhD often required)
✓ Skills: Advanced math, deep learning, research
✓ Best for: Academic-minded, love theory

👁️ COMPUTER VISION ENGINEER:
✓ Focus: Image recognition, object detection
✓ Salary: ₹15-50 LPA
✓ Skills: PyTorch, OpenCV, CNNs
✓ Best for: Visual thinkers, creative

💬 NLP ENGINEER (Hot in 2025!):
✓ Focus: Text analysis, chatbots, LLMs
✓ Salary: ₹18-60 LPA (highest demand now!)
✓ Skills: Transformers, Hugging Face, LangChain
✓ Best for: Language lovers + coding

📈 DATA ENGINEER (Infrastructure):
✓ Focus: Data pipelines, ETL, databases
✓ Salary: ₹12-45 LPA
✓ Skills: Spark, Airflow, SQL, cloud
✓ Best for: People who like systems

🎮 RECOMMENDATION SYSTEMS:
✓ Focus: Personalization algorithms
✓ Salary: ₹15-45 LPA
✓ Companies: Netflix, Amazon, YouTube
✓ Best for: Product-minded engineers

═══════════════════════════════════════════════
📚 PHASE 7: LEARNING RESOURCES
═══════════════════════════════════════════════

📖 MUST-READ BOOKS:
1. "Hands-On Machine Learning" - Aurélien Géron ⭐⭐⭐⭐⭐
2. "Deep Learning" - Ian Goodfellow (FREE online)
3. "The Elements of Statistical Learning" - Hastie et al
4. "Python for Data Analysis" - Wes McKinney
5. "Designing Machine Learning Systems" - Chip Huyen

🎓 ONLINE COURSES (Best ROI):

Free & Excellent:
• Andrew Ng's ML Course (Coursera) - MUST DO!
• Fast.ai Practical Deep Learning - FREE
• Google ML Crash Course - FREE
• Kaggle Learn - FREE hands-on

Paid (Worth It):
• DeepLearning.AI Specializations ($49/month)
• DataCamp ($25/month) - Interactive learning
• Coursera ML Engineering ($39-79/month)

🏆 KAGGLE (MOST IMPORTANT!):
• Start with "Titanic" competition
• Aim for Kaggle Expert (top 5% in 2 competitions)
• Kaggle Master = Job interviews easy!
• Companies hire directly from Kaggle

Progression:
→ Competitions Novice
→ Competitions Contributor (bronze medal)
→ Competitions Expert (silver medal)
→ Kaggle Master (gold medal) = ₹25+ LPA easily!

🎬 YOUTUBE CHANNELS:
• StatQuest (Statistics explained simply) ⭐⭐⭐⭐⭐
• 3Blue1Brown (Math intuition)
• Sentdex (Python ML tutorials)
• Two Minute Papers (AI research updates)
• Krish Naik (Indian data science)

💬 COMMUNITIES:
• Kaggle Forums
• Reddit: r/datascience, r/MachineLearning
• LinkedIn Data Science groups
• Discord: MLOps Community

═══════════════════════════════════════════════
🎓 PHASE 8: CERTIFICATIONS (2025)
═══════════════════════════════════════════════

🔥 HIGHEST VALUE:

1. AWS Certified Machine Learning - Specialty
   → Cost: $300 | Salary Boost: +₹5-8 LPA
   → Best for: ML Engineer roles

2. TensorFlow Developer Certificate (Google)
   → Cost: $100 | Shows practical DL skills
   → Portfolio piece for resume

3. Google Professional ML Engineer
   → Cost: $200 | GCP-focused companies

4. Microsoft Azure Data Scientist Associate
   → Cost: $165 | Enterprise companies

🎯 WORTH IT?
• Cloud ML certs: YES (high salary boost)
• Kaggle Medals: YES (better than most certs!)
• Online course certs: MAYBE (portfolio > certs)
• PhD: Only if you want research roles

💡 BETTER THAN CERTS:
→ Strong Kaggle profile (Expert/Master)
→ GitHub with good ML projects
→ Published research papers (if aiming for research)
→ Medium blog with good articles

═══════════════════════════════════════════════
🌍 PHASE 9: INTERNATIONAL OPTIONS
═══════════════════════════════════════════════

🇺🇸 USA - HIGHEST SALARIES:
MS in Data Science:
• Top Unis: Stanford, CMU, UC Berkeley, MIT
• Cost: $60K-80K total
• Starting salary: $120K-160K
• OPT: 3 years for STEM (work after graduation)
• H1B lottery: Masters get better odds

Best for Data Science:
• California: Highest salaries, most jobs
• Seattle: Amazon, Microsoft
• NYC: Finance, consulting jobs

🇨🇦 CANADA - EASIEST PR:
MS Programs:
• Universities: Waterloo, Toronto, UBC
• Cost: CAD 30K-50K
• PR Path: Very easy for data scientists!
• Express Entry: High points for tech

🇩🇪 GERMANY - FREE EDUCATION:
• TUM, RWTH Aachen - FREE tuition!
• Cost: Only €300/semester fees
• English programs available
• Job market: Growing for data science

🇬🇧 UK - 1 YEAR MASTERS:
• Oxford, Imperial, UCL, Cambridge
• Cost: £20K-30K
• Duration: Only 1 year!
• 2-year post-study work visa

🇸🇬 SINGAPORE - ASIA HUB:
• NUS, NTU - Top Asian universities
• Financial hub, many data science roles
• Salary: SGD 70K-150K
• Easy PR after 2-3 years

═══════════════════════════════════════════════
💡 FINAL TIPS & SUCCESS FACTORS
═══════════════════════════════════════════════

🔥 WHAT SEPARATES GOOD FROM GREAT DS:

Technical (50%):
✓ Strong statistics foundation
✓ Coding skills (Python, SQL)
✓ ML algorithm understanding
✓ Can deploy models to production

Business (30%):
✓ Understand business problems
✓ Translate ML to business value
✓ Communicate with non-technical stakeholders
✓ Focus on impact, not just accuracy

Soft Skills (20%):
✓ Curiosity & experimentation
✓ Storytelling with data
✓ Collaboration with engineers
✓ Continuous learning

🎯 ACTIONABLE ROADMAP:

MONTH 1-6 (Foundations):
→ Complete Andrew Ng ML course
→ Learn Python (Pandas, NumPy)
→ SQL mastery (practice on Mode.com)
→ 2-3 Kaggle beginner competitions

MONTH 7-12 (Intermediate):
→ Deep Learning course
→ Build 3-5 portfolio projects
→ Contribute to open-source ML
→ Aim for Kaggle bronze medal

YEAR 2 (Advanced):
→ Learn MLOps (Docker, deployment)
→ Cloud platform (AWS/GCP)
→ Big data tools (Spark)
→ Target Kaggle silver/gold medal
→ Apply to companies

🚫 COMMON MISTAKES:

❌ Theory without projects (build things!)
❌ Not learning business context
❌ Ignoring SQL (super important!)
❌ Only Jupyter notebooks (learn deployment)
❌ Not doing Kaggle (best learning platform)
❌ Perfectionism (80% is good enough, iterate!)

✅ SUCCESS CHECKLIST:

□ Complete Andrew Ng's ML course
□ Build 5+ portfolio projects on GitHub
□ Achieve Kaggle Expert rank
□ Master SQL (can write complex queries)
□ Understand business metrics
□ Deploy 1+ models to production
□ Network on LinkedIn (500+ connections)
□ Technical blog with 5+ articles

═══════════════════════════════════════════════
📞 QUICK REFERENCE SUMMARY
═══════════════════════════════════════════════

⏰ TIMELINE: 4 years degree + 1-2 years = Senior DS
💰 SALARY: ₹6-15 LPA → ₹60 LPA+ in 8-10 years
🎓 EDUCATION: CS/Stats/Math degree best
🌍 ABROAD: USA ($120K+), Canada (Easy PR)
🛠️ SKILLS: Python + SQL + ML + Cloud
🏢 COMPANIES: FAANG, Consulting, Finance
📚 LEARN: Kaggle, Andrew Ng course, Projects
🎯 GOAL: Kaggle Expert + Strong Portfolio + Business Skills

"Data is the new oil. Be the one who refines it!" 📊✨
        """.trimIndent()
    }
    
    private fun generateDoctorRoadmap(): String {
        return """
🏥 DOCTOR CAREER ROADMAP

📚 HIGH SCHOOL PREPARATION (11th-12th)
• Mandatory: Physics, Chemistry, Biology (PCB)
• Minimum: 85%+ in 12th grade for competitive entrance
• NEET Preparation: 2-3 years of focused study
• Extracurriculars: Medical camps, first aid training

🎓 UNDERGRADUATE EDUCATION (5.5 years)
📍 MBBS (Bachelor of Medicine, Bachelor of Surgery)
• Duration: 4.5 years + 1 year mandatory internship
• Top Institutes: AIIMS, JIPMER, CMC Vellore, AFMC, KMC Manipal
• Entrance: NEET-UG (National Eligibility cum Entrance Test)
• Curriculum: Anatomy, Physiology, Pathology, Medicine, Surgery

📈 POSTGRADUATE SPECIALIZATION (3 years)
• MD (Doctor of Medicine): Internal Medicine, Pediatrics, Psychiatry
• MS (Master of Surgery): General Surgery, Orthopedics, ENT
• Entrance: NEET-PG
• Super-specialization: DM/MCh (3 years additional)

💰 SALARY PROGRESSION
📍 INDIA:
• Junior Resident: ₹6-12 LPA
• Senior Resident: ₹12-20 LPA
• Consultant: ₹25-50 LPA
• Private Practice: ₹50 LPA - ₹2 Cr+

📍 ABROAD:
• Resident (USA): $55K-65K
• Attending Physician: $200K-400K+
• Specialist: $300K-800K+

🏢 CAREER OPPORTUNITIES
• Government Hospitals: AIIMS, PGI, State Medical Colleges
• Private Hospitals: Apollo, Fortis, Max, Manipal
• International: USMLE for USA, PLAB for UK
• Research: Medical research institutions, pharmaceutical companies

⏰ TOTAL TIMELINE: 11-15 years (MBBS + Specialization + Experience)
        """.trimIndent()
    }
    
    private fun generateGenericRoadmap(occupation: String): String {
        return """
🎯 $occupation CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: Relevant stream selection based on field requirements
• Undergraduate: 3-4 years bachelor's degree in related field
• Postgraduate: 2 years master's degree (optional but recommended)
• Professional Certifications: Industry-specific credentials

📈 SKILL DEVELOPMENT
• Core technical skills relevant to $occupation
• Soft skills: Communication, leadership, problem-solving
• Industry knowledge and best practices
• Continuous learning and professional development

💰 CAREER PROSPECTS
• Entry Level: Competitive starting salary in the field
• Mid-Level: Significant growth opportunities with experience
• Senior Level: Leadership roles and specialized positions
• Expert Level: Consulting, training, and strategic roles

🏢 EMPLOYMENT OPPORTUNITIES
• Private sector companies and corporations
• Government organizations and public sector
• Non-profit organizations and NGOs
• Freelancing and consulting opportunities
• International career prospects

🌟 SUCCESS FACTORS
• Strong foundation in core subject knowledge
• Practical experience through internships and projects
• Professional networking and industry connections
• Adaptability to changing industry trends
• Commitment to lifelong learning and skill development

⏰ TIMELINE
• Education: 4-6 years (bachelor's + master's)
• Early Career: 2-3 years to establish expertise
• Mid-Career: 5-8 years to reach senior positions
• Leadership: 10+ years for executive roles
        """.trimIndent()
    }
    
    private fun generateTeacherRoadmap(): String {
        return """
👩‍🏫 TEACHER CAREER ROADMAP

📚 HIGH SCHOOL PREPARATION
• Stream: Any (choose based on subject you want to teach)
• Skills: Communication, patience, subject mastery
• Activities: Tutoring, volunteer teaching, debate participation

🎓 EDUCATION PATHWAY (4-5 years total)
• Bachelor's Degree: 3 years in subject specialization
• B.Ed (Bachelor of Education): 2 years - MANDATORY for teaching
• Subject Options: Mathematics, Science, English, Social Studies, Languages

🎯 CERTIFICATION REQUIREMENTS
• TET (Teacher Eligibility Test): For state government schools
• CTET (Central TET): For central government schools
• NET/SET: For college-level teaching

💰 SALARY PROGRESSION
📍 GOVERNMENT SCHOOLS:
• PRT (Primary): ₹25K-40K per month
• TGT (Trained Graduate): ₹50K-80K per month
• PGT (Post Graduate): ₹80K-1.2L per month

📍 PRIVATE SCHOOLS:
• Entry Level: ₹15K-30K per month
• Experienced: ₹40K-70K per month
• Senior Positions: ₹80K+ per month

🏢 CAREER OPPORTUNITIES
• Government Schools (Central/State)
• Private Schools and International Schools
• Coaching Institutes and Tutorial Centers
• Online Education Platforms
• Educational Content Development

⏰ TIMELINE: 4-5 years education + experience for senior roles
        """.trimIndent()
    }
    
    private fun generateMechanicalEngineerRoadmap(): String {
        return """
⚙️ MECHANICAL ENGINEER CAREER ROADMAP

📚 HIGH SCHOOL (11th-12th)
• Core: Physics, Chemistry, Mathematics (PCM) - MANDATORY
• Skills: Technical drawing, basic mechanics understanding
• Preparation: JEE coaching, physics olympiads

🎓 UNDERGRADUATE (4 years)
• B.Tech/BE Mechanical Engineering
• Top Institutes: IITs, NITs, BITS Pilani, VIT, Thapar
• Entrance: JEE Main/Advanced, state engineering exams
• Curriculum: Thermodynamics, Fluid Mechanics, Machine Design, Manufacturing

📈 SPECIALIZATIONS
• Automotive Engineering
• Aerospace Engineering  
• Manufacturing and Production
• Thermal Engineering
• Robotics and Automation

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹3-6 LPA
• Mid Level: ₹8-15 LPA
• Senior Level: ₹15-30 LPA
• Manager/GM: ₹30-50+ LPA

🏢 TOP RECRUITERS
• Automotive: Tata Motors, Mahindra, Maruti Suzuki, Hero MotoCorp
• Heavy Industries: L&T, BHEL, SAIL, ONGC
• Manufacturing: Godrej, Bajaj, TVS, Ashok Leyland
• MNCs: Siemens, GE, Honeywell, Bosch

⏰ TIMELINE: 4 years B.Tech + 2-3 years for senior positions
        """.trimIndent()
    }
    
    private fun generateGraphicDesignerRoadmap(): String {
        return """
🎨 GRAPHIC DESIGNER CAREER ROADMAP

📚 EDUCATION PATHWAY
• Stream: Any (Arts/Commerce preferred)
• Courses: B.Des, BFA, Diploma in Graphic Design
• Duration: 3-4 years
• Portfolio: Most important for career success

🛠️ TECHNICAL SKILLS
• Adobe Creative Suite: Photoshop, Illustrator, InDesign
• Web Design: Figma, Sketch, Adobe XD
• Typography, Color Theory, Layout Design
• Basic HTML/CSS knowledge helpful

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹2-4 LPA
• Mid Level: ₹6-12 LPA
• Senior Level: ₹15-25 LPA
• Creative Director: ₹25-40+ LPA

🏢 CAREER PATHS
• Advertising Agencies
• Design Studios
• In-house Corporate Teams
• Freelancing
• Digital Marketing Agencies

⏰ TIMELINE: 3-4 years education + portfolio development
        """.trimIndent()
    }
    
    private fun generateBusinessManagerRoadmap(): String {
        return """
💼 BUSINESS MANAGER CAREER ROADMAP

📚 EDUCATION PATHWAY
• Undergraduate: BBA, B.Com, Economics (3 years)
• MBA: 2 years from top B-schools (highly recommended)
• Top Institutes: IIMs, ISB, XLRI, FMS, SPJIMR
• Entrance: CAT, XAT, GMAT, NMAT

📈 SPECIALIZATIONS
• Marketing Management
• Operations Management
• Human Resources
• Finance and Strategy
• International Business

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹6-12 LPA
• Mid Level: ₹15-25 LPA
• Senior Level: ₹25-50 LPA
• C-Suite: ₹50 LPA - ₹2 Cr+

🏢 TOP RECRUITERS
• Consulting: McKinsey, BCG, Bain, Deloitte
• FMCG: HUL, P&G, Nestle, ITC
• Banking: HDFC, ICICI, Axis, Kotak
• Technology: Google, Microsoft, Amazon

⏰ TIMELINE: 5-6 years education + 3-5 years for senior management
        """.trimIndent()
    }
    
    private fun generateNurseRoadmap(): String {
        return """
👩‍⚕️ NURSING CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: PCB (Physics, Chemistry, Biology)
• Courses: B.Sc Nursing (4 years), GNM (3 years)
• Top Institutes: AIIMS, CMC, nursing colleges
• Practical Training: Hospital rotations, patient care

📈 SPECIALIZATIONS
• Critical Care Nursing
• Pediatric Nursing
• Psychiatric Nursing
• Community Health Nursing
• Nurse Practitioner (with additional certification)

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹2-4 LPA
• Experienced: ₹5-8 LPA
• Senior/Supervisor: ₹8-15 LPA
• Nursing Superintendent: ₹15-25 LPA

📍 ABROAD:
• USA/Canada: $50K-80K
• Middle East: $30K-50K (tax-free)
• Australia/UK: $45K-70K

🏢 OPPORTUNITIES
• Hospitals and Clinics
• Home Healthcare
• International Nursing (high demand)
• Public Health Programs
• Nursing Education

⏰ TIMELINE: 3-4 years education + experience for specialization
        """.trimIndent()
    }
    
    private fun generateAccountantRoadmap(): String {
        return """
💼 ACCOUNTANT CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: Commerce stream preferred
• Undergraduate: B.Com, BBA (3 years)
• Professional: CA, CS, CMA (3-5 years)
• Entrance: CPT/Foundation, Intermediate, Final

📈 PROFESSIONAL CERTIFICATIONS
• CA (Chartered Accountant): Most prestigious, 4-5 years
• CS (Company Secretary): Corporate law focus, 3-4 years  
• CMA (Cost Management Accountant): Cost accounting, 3-4 years
• ACCA: International certification

💰 SALARY PROGRESSION
📍 INDIA:
• Article/Trainee: ₹2-4 LPA
• Qualified CA: ₹8-15 LPA
• Senior Manager: ₹15-30 LPA
• Partner/CFO: ₹30 LPA - ₹1 Cr+

🏢 TOP RECRUITERS
• Big 4: Deloitte, PwC, EY, KPMG
• Banks: HDFC, ICICI, SBI, Axis
• Corporates: TCS, Infosys, Reliance
• Government: Income Tax, CAG, RBI

⏰ TIMELINE: 3 years B.Com + 3-5 years professional course
        """.trimIndent()
    }
    
    private fun generateLawyerRoadmap(): String {
        return """
⚖️ LAWYER CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: Any stream (Humanities preferred)
• Law Degree: LLB (3 years) or BA LLB (5 years)
• Top Institutes: NLUs, Government Law Colleges
• Entrance: CLAT, AILET, state law entrance exams

📈 SPECIALIZATIONS
• Corporate Law
• Criminal Law
• Civil Litigation
• Constitutional Law
• Intellectual Property Law
• International Law

💰 SALARY PROGRESSION
📍 INDIA:
• Junior Lawyer: ₹3-6 LPA
• Senior Associate: ₹8-15 LPA
• Partner: ₹25-50 LPA
• Senior Counsel: ₹50 LPA - ₹5 Cr+

🏢 CAREER PATHS
• Law Firms (Corporate/Litigation)
• In-house Legal (Companies)
• Government Legal Services
• Judiciary (Judge positions)
• Legal Consulting

⏰ TIMELINE: 5 years law degree + 3-5 years practice for seniority
        """.trimIndent()
    }
    
    private fun generatePsychologistRoadmap(): String {
        return """
🧠 PSYCHOLOGIST CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: Any stream (Science/Humanities preferred)
• Bachelor's: BA/B.Sc Psychology (3 years)
• Master's: MA/M.Sc Psychology (2 years)
• Doctorate: Ph.D Psychology (3-5 years) for clinical practice

🎯 SPECIALIZATIONS
• Clinical Psychology
• Counseling Psychology
• Educational Psychology
• Industrial Psychology
• Forensic Psychology
• Child Psychology

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹3-6 LPA
• Mid Level: ₹6-12 LPA
• Senior Level: ₹12-25 LPA
• Private Practice: ₹25-50+ LPA

🏢 CAREER OPPORTUNITIES
• Hospitals and Clinics
• Educational Institutions
• Corporate HR Departments
• Private Practice
• Research Institutions

⏰ TIMELINE: 8-10 years education + experience for specialization
        """.trimIndent()
    }
    
    private fun generateArchitectRoadmap(): String {
        return """
🏗️ ARCHITECT CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: PCM (Physics, Chemistry, Mathematics)
• Bachelor's: B.Arch (5 years)
• Master's: M.Arch (2 years) - optional
• Entrance: NATA, JEE Main Paper 2

🛠️ TECHNICAL SKILLS
• Design Software: AutoCAD, Revit, SketchUp, 3ds Max
• Visualization: Photoshop, Lumion, V-Ray
• Building Codes and Regulations
• Structural Engineering Basics
• Sustainable Design Principles

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹3-6 LPA
• Mid Level: ₹8-15 LPA
• Senior Level: ₹15-30 LPA
• Principal Architect: ₹30-60+ LPA

🏢 CAREER PATHS
• Architectural Firms
• Construction Companies
• Government Planning Departments
• Real Estate Development
• Independent Practice

⏰ TIMELINE: 5 years B.Arch + 3-5 years experience for senior roles
        """.trimIndent()
    }
    
    private fun generatePharmacistRoadmap(): String {
        return """
💊 PHARMACIST CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: PCB (Physics, Chemistry, Biology)
• Bachelor's: B.Pharm (4 years)
• Master's: M.Pharm (2 years) - for specialization
• Doctorate: Pharm.D (6 years) - for clinical practice

🎯 SPECIALIZATIONS
• Clinical Pharmacy
• Hospital Pharmacy
• Community Pharmacy
• Industrial Pharmacy
• Regulatory Affairs
• Pharmaceutical Marketing

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹3-5 LPA
• Mid Level: ₹6-12 LPA
• Senior Level: ₹12-25 LPA
• Manager/Director: ₹25-50+ LPA

🏢 CAREER OPPORTUNITIES
• Hospitals and Clinics
• Retail Pharmacy Chains
• Pharmaceutical Companies
• Government Health Departments
• Research Organizations

⏰ TIMELINE: 4-6 years education + experience for specialization
        """.trimIndent()
    }
    
    private fun generateDentistRoadmap(): String {
        return """
🦷 DENTIST CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: PCB (Physics, Chemistry, Biology)
• Bachelor's: BDS (5 years including internship)
• Master's: MDS (3 years) - for specialization
• Entrance: NEET-UG for BDS, NEET-MDS for postgraduate

🎯 SPECIALIZATIONS
• Orthodontics
• Oral Surgery
• Periodontics
• Endodontics
• Prosthodontics
• Pediatric Dentistry

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹4-8 LPA
• Mid Level: ₹8-15 LPA
• Senior Level: ₹15-30 LPA
• Private Practice: ₹30-80+ LPA

🏢 CAREER OPPORTUNITIES
• Private Dental Clinics
• Hospitals
• Government Health Services
• Dental Colleges (Teaching)
• Independent Practice

⏰ TIMELINE: 5 years BDS + 3 years MDS for specialization
        """.trimIndent()
    }
    
    private fun generateVeterinarianRoadmap(): String {
        return """
🐾 VETERINARIAN CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: PCB (Physics, Chemistry, Biology)
• Bachelor's: BVSc & AH (5.5 years including internship)
• Master's: MVSc (2 years) - for specialization
• Entrance: NEET-UG, state veterinary entrance exams

🎯 SPECIALIZATIONS
• Small Animal Practice
• Large Animal Practice
• Wildlife Veterinary Medicine
• Veterinary Surgery
• Animal Nutrition
• Veterinary Public Health

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹3-6 LPA
• Mid Level: ₹6-12 LPA
• Senior Level: ₹12-25 LPA
• Private Practice: ₹25-50+ LPA

🏢 CAREER OPPORTUNITIES
• Veterinary Hospitals and Clinics
• Government Veterinary Services
• Animal Husbandry Departments
• Pharmaceutical Companies
• Private Practice

⏰ TIMELINE: 5.5 years BVSc + experience for specialization
        """.trimIndent()
    }
    
    private fun generateCivilEngineerRoadmap(): String {
        return """
🏗️ CIVIL ENGINEER CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: PCM (Physics, Chemistry, Mathematics)
• Bachelor's: B.Tech/BE Civil Engineering (4 years)
• Master's: M.Tech Civil (2 years) - optional
• Entrance: JEE Main/Advanced, state engineering exams

🛠️ TECHNICAL SKILLS
• Design Software: AutoCAD, STAAD Pro, ETABS, Revit
• Project Management: MS Project, Primavera
• Surveying and GIS
• Construction Materials Knowledge
• Building Codes and Standards

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹3-6 LPA
• Mid Level: ₹8-15 LPA
• Senior Level: ₹15-30 LPA
• Project Manager: ₹30-50+ LPA

🏢 TOP RECRUITERS
• Construction: L&T, Shapoorji Pallonji, Godrej
• Infrastructure: NHAI, Indian Railways, DMRC
• Consulting: AECOM, Jacobs, Arup
• Government: PWD, CPWD, State Engineering Services

⏰ TIMELINE: 4 years B.Tech + 3-5 years for senior positions
        """.trimIndent()
    }
    
    private fun generateMarketingManagerRoadmap(): String {
        return """
📈 MARKETING MANAGER CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: Any stream (Commerce preferred)
• Bachelor's: BBA, B.Com, Economics (3 years)
• Master's: MBA Marketing (2 years) - highly recommended
• Entrance: CAT, XAT, GMAT for top B-schools

🛠️ SKILLS REQUIRED
• Digital Marketing: SEO, SEM, Social Media
• Analytics: Google Analytics, Data Analysis
• Creative Skills: Content Creation, Design Thinking
• Communication and Presentation
• Market Research and Consumer Behavior

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹4-8 LPA
• Mid Level: ₹10-18 LPA
• Senior Level: ₹20-35 LPA
• Marketing Director: ₹35-60+ LPA

🏢 CAREER OPPORTUNITIES
• FMCG Companies: HUL, P&G, Nestle
• Digital Agencies: Ogilvy, Publicis, Dentsu
• E-commerce: Amazon, Flipkart, Myntra
• Startups and Tech Companies

⏰ TIMELINE: 5 years education + 3-5 years for senior management
        """.trimIndent()
    }
    
    private fun generateFinancialAnalystRoadmap(): String {
        return """
💰 FINANCIAL ANALYST CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: Commerce/Science stream
• Bachelor's: B.Com, BBA, Economics (3 years)
• Professional: CA, CFA, FRM certifications
• Master's: MBA Finance (2 years) - preferred

🛠️ TECHNICAL SKILLS
• Financial Modeling and Valuation
• Excel Advanced, SQL, Python/R
• Bloomberg Terminal, Reuters
• Risk Management Tools
• Financial Statement Analysis

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹5-10 LPA
• Mid Level: ₹12-20 LPA
• Senior Level: ₹25-40 LPA
• VP/Director: ₹50-80+ LPA

🏢 TOP RECRUITERS
• Investment Banks: Goldman Sachs, JP Morgan
• Consulting: McKinsey, BCG, Bain
• Asset Management: Blackstone, KKR
• Indian Banks: HDFC, ICICI, Axis

⏰ TIMELINE: 5 years education + certifications + experience
        """.trimIndent()
    }
    
    private fun generateUXDesignerRoadmap(): String {
        return """
🎨 UX DESIGNER CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: Any stream (Arts preferred)
• Bachelor's: Design, Psychology, Computer Science (3-4 years)
• Specialized Courses: UX/UI Design bootcamps
• Portfolio: Most critical for career success

🛠️ TECHNICAL SKILLS
• Design Tools: Figma, Sketch, Adobe XD, Photoshop
• Prototyping: InVision, Marvel, Principle
• Research: User interviews, surveys, analytics
• Basic HTML/CSS knowledge
• Design Systems and Accessibility

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹4-8 LPA
• Mid Level: ₹10-18 LPA
• Senior Level: ₹20-35 LPA
• Design Director: ₹40-60+ LPA

🏢 CAREER OPPORTUNITIES
• Tech Companies: Google, Microsoft, Adobe
• Product Companies: Flipkart, Swiggy, Zomato
• Design Agencies: Fractal, Designit
• Startups and Consulting Firms

⏰ TIMELINE: 3-4 years education + portfolio + 3-5 years experience
        """.trimIndent()
    }
    
    private fun generateProductManagerRoadmap(): String {
        return """
📱 PRODUCT MANAGER CAREER ROADMAP

📚 EDUCATION PATHWAY
• High School: Any stream (Science/Commerce preferred)
• Bachelor's: Engineering, Business, Economics (3-4 years)
• Master's: MBA (2 years) - highly preferred
• Experience: 2-3 years in tech/business roles

🛠️ SKILLS REQUIRED
• Product Strategy and Vision
• Data Analysis and Metrics
• User Experience and Design Thinking
• Technical Understanding (APIs, databases)
• Project Management and Agile
• Communication and Leadership

💰 SALARY PROGRESSION
📍 INDIA:
• Entry Level: ₹8-15 LPA
• Mid Level: ₹18-30 LPA
• Senior Level: ₹35-60 LPA
• VP Product: ₹80 LPA - ₹2 Cr+

🏢 TOP RECRUITERS
• Tech Giants: Google, Microsoft, Amazon, Meta
• Indian Unicorns: Flipkart, Swiggy, Zomato, Paytm
• Consulting: McKinsey Digital, BCG Digital
• Startups: High growth potential companies

⏰ TIMELINE: 5-6 years education + 2-3 years experience for PM roles
        """.trimIndent()
    }
    
    // Missing REALISTIC (R) roadmaps
    private fun generateElectricalEngineerRoadmap(): String {
        return generateGenericRoadmap("Electrical Engineer")
    }
    
    private fun generateAutomotiveTechnicianRoadmap(): String {
        return generateGenericRoadmap("Automotive Technician")
    }
    
    private fun generateCarpenterRoadmap(): String {
        return generateGenericRoadmap("Carpenter")
    }
    
    private fun generateElectricianRoadmap(): String {
        return generateGenericRoadmap("Electrician")
    }
    
    private fun generatePilotRoadmap(): String {
        return generateGenericRoadmap("Pilot")
    }
    
    private fun generateIndustrialDesignerRoadmap(): String {
        return generateGenericRoadmap("Industrial Designer")
    }
    
    // Missing INVESTIGATIVE (I) roadmaps
    private fun generateResearchScientistRoadmap(): String {
        return generateGenericRoadmap("Research Scientist")
    }
    
    private fun generateMathematicianRoadmap(): String {
        return generateGenericRoadmap("Mathematician")
    }
    
    private fun generatePhysicistRoadmap(): String {
        return generateGenericRoadmap("Physicist")
    }
    
    private fun generateChemistRoadmap(): String {
        return generateGenericRoadmap("Chemist")
    }
    
    private fun generateBiologistRoadmap(): String {
        return generateGenericRoadmap("Biologist")
    }
    
    private fun generateMarketResearchAnalystRoadmap(): String {
        return generateGenericRoadmap("Market Research Analyst")
    }
    
    // Missing ARTISTIC (A) roadmaps
    private fun generateArtistRoadmap(): String {
        return generateGenericRoadmap("Artist")
    }
    
    private fun generateWriterRoadmap(): String {
        return generateGenericRoadmap("Writer")
    }
    
    private fun generateMusicianRoadmap(): String {
        return generateGenericRoadmap("Musician")
    }
    
    private fun generateActorRoadmap(): String {
        return generateGenericRoadmap("Actor")
    }
    
    private fun generatePhotographerRoadmap(): String {
        return generateGenericRoadmap("Photographer")
    }
    
    private fun generateInteriorDesignerRoadmap(): String {
        return generateGenericRoadmap("Interior Designer")
    }
    
    private fun generateFashionDesignerRoadmap(): String {
        return generateGenericRoadmap("Fashion Designer")
    }
    
    private fun generateVideoEditorRoadmap(): String {
        return generateGenericRoadmap("Video Editor")
    }
    
    // Missing SOCIAL (S) roadmaps
    private fun generateCounselorRoadmap(): String {
        return generateGenericRoadmap("Counselor")
    }
    
    private fun generateSocialWorkerRoadmap(): String {
        return generateGenericRoadmap("Social Worker")
    }
    
    private fun generateTherapistRoadmap(): String {
        return generateGenericRoadmap("Therapist")
    }
    
    private fun generateHRManagerRoadmap(): String {
        return generateGenericRoadmap("Human Resources Manager")
    }
    
    private fun generateCustomerServiceRoadmap(): String {
        return generateGenericRoadmap("Customer Service Representative")
    }
    
    private fun generateCommunityOutreachRoadmap(): String {
        return generateGenericRoadmap("Community Outreach Coordinator")
    }
    
    private fun generateSchoolAdministratorRoadmap(): String {
        return generateGenericRoadmap("School Administrator")
    }
    
    private fun generateHealthcareAdministratorRoadmap(): String {
        return generateGenericRoadmap("Healthcare Administrator")
    }
    
    // Missing ENTERPRISING (E) roadmaps
    private fun generateSalesRepresentativeRoadmap(): String {
        return generateGenericRoadmap("Sales Representative")
    }
    
    private fun generateEntrepreneurRoadmap(): String {
        return generateGenericRoadmap("Entrepreneur")
    }
    
    private fun generatePoliticianRoadmap(): String {
        return generateGenericRoadmap("Politician")
    }
    
    private fun generateRealEstateAgentRoadmap(): String {
        return generateGenericRoadmap("Real Estate Agent")
    }
    
    private fun generateFinancialAdvisorRoadmap(): String {
        return generateGenericRoadmap("Financial Advisor")
    }
    
    private fun generateProjectManagerRoadmap(): String {
        return generateGenericRoadmap("Project Manager")
    }
    
    private fun generateOperationsManagerRoadmap(): String {
        return generateGenericRoadmap("Operations Manager")
    }
    
    // Missing CONVENTIONAL (C) roadmaps
    private fun generateDataEntryClerkRoadmap(): String {
        return generateGenericRoadmap("Data Entry Clerk")
    }
    
    private fun generateAdministrativeAssistantRoadmap(): String {
        return generateGenericRoadmap("Administrative Assistant")
    }
    
    private fun generateLibrarianRoadmap(): String {
        return generateGenericRoadmap("Librarian")
    }
    
    private fun generateBankTellerRoadmap(): String {
        return generateGenericRoadmap("Bank Teller")
    }
    
    private fun generateBookkeeperRoadmap(): String {
        return generateGenericRoadmap("Bookkeeper")
    }
    
    private fun generateQualityControlInspectorRoadmap(): String {
        return generateGenericRoadmap("Quality Control Inspector")
    }
    
    private fun generateOfficeManagerRoadmap(): String {
        return generateGenericRoadmap("Office Manager")
    }
    
    private fun generateRecordsClerkRoadmap(): String {
        return generateGenericRoadmap("Records Clerk")
    }
    
    private fun shareRoadmap(occupation: String) {
        try {
            val shareText = "Check out this comprehensive career roadmap for $occupation from our Career Assessment App!"
            val shareIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            }
            startActivity(android.content.Intent.createChooser(shareIntent, "Share Roadmap"))
        } catch (e: Exception) {
            android.util.Log.e("RoadmapDetail", "Error sharing roadmap", e)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        roadmapJob?.cancel()
    }
}

package com.theapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.theapp.databinding.ActivityOccupationSelectionBinding

class OccupationSelectionActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityOccupationSelectionBinding
    private lateinit var occupationsList: ListView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityOccupationSelectionBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            // Get assessment results from intent with safe defaults
            val rScore = intent?.getIntExtra("r_score", 5) ?: 5
            val iScore = intent?.getIntExtra("i_score", 3) ?: 3
            val aScore = intent?.getIntExtra("a_score", 2) ?: 2
            val sScore = intent?.getIntExtra("s_score", 8) ?: 8
            val eScore = intent?.getIntExtra("e_score", 4) ?: 4
            val cScore = intent?.getIntExtra("c_score", 1) ?: 1
            val aptitudeScore = intent?.getIntExtra("aptitude_score", 75) ?: 75
            
            // Find dominant type safely
            val scores = mapOf("R" to rScore, "I" to iScore, "A" to aScore, "S" to sScore, "E" to eScore, "C" to cScore)
            val dominantType = scores.maxByOrNull { it.value }?.key ?: "S"
            
            // Set basic results with null checks
            binding.tvPersonalityType?.text = "Your Personality Type: ${getTypeName(dominantType)}"
            binding.tvPersonalityDescription?.text = getTypeDescription(dominantType)
            binding.tvAptitudeScore?.text = "Aptitude Score: $aptitudeScore%"
            
            // Set RIASEC scores with null checks
            binding.tvRealisticScore?.text = "R: $rScore"
            binding.tvInvestigativeScore?.text = "I: $iScore"
            binding.tvArtisticScore?.text = "A: $aScore"
            binding.tvSocialScore?.text = "S: $sScore"
            binding.tvEnterprisingScore?.text = "E: $eScore"
            binding.tvConventionalScore?.text = "C: $cScore"
            
            setupOccupationList(dominantType)
            
            binding.btnRestart?.setOnClickListener {
                finish()
            }
            
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error in onCreate", e)
            // Set fallback UI
            setFallbackUI()
        }
    }
    
    private fun setFallbackUI() {
        try {
            binding.tvPersonalityType?.text = "Career Assessment Results"
            binding.tvPersonalityDescription?.text = "Your assessment has been completed successfully."
            binding.tvAptitudeScore?.text = "Aptitude Score: Available"
            setupOccupationList("S") // Default to Social type
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error in fallback UI", e)
            finish() // Close activity if everything fails
        }
    }
    
    private fun setupOccupationList(dominantType: String) {
        try {
            // Get recommended occupations based on personality type
            val occupations = getRecommendedOccupations(dominantType)
            
            if (occupations.isNotEmpty()) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    occupations
                )
                
                binding.listOccupations?.adapter = adapter
                
                binding.listOccupations?.setOnItemClickListener { _, _, position, _ ->
                    try {
                        if (position >= 0 && position < occupations.size) {
                            val selectedOccupation = occupations[position]
                            openRoadmapDetail(selectedOccupation)
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("OccupationSelection", "Error in item click", e)
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("OccupationSelection", "Error setting up occupation list", e)
        }
    }
    
    private fun openRoadmapDetail(occupation: String) {
        try {
            val newIntent = Intent(this, RoadmapDetailActivity::class.java)
            newIntent.putExtra("occupation_title", occupation)
            
            // Get scores from current intent safely
            val currentIntent = intent
            newIntent.putExtra("r_score", currentIntent?.getIntExtra("r_score", 5) ?: 5)
            newIntent.putExtra("i_score", currentIntent?.getIntExtra("i_score", 3) ?: 3)
            newIntent.putExtra("a_score", currentIntent?.getIntExtra("a_score", 2) ?: 2)
            newIntent.putExtra("s_score", currentIntent?.getIntExtra("s_score", 8) ?: 8)
            newIntent.putExtra("e_score", currentIntent?.getIntExtra("e_score", 4) ?: 4)
            newIntent.putExtra("c_score", currentIntent?.getIntExtra("c_score", 1) ?: 1)
            newIntent.putExtra("aptitude_score", currentIntent?.getIntExtra("aptitude_score", 75) ?: 75)
            
            startActivity(newIntent)
        } catch (e: Exception) {
            // Log error and show fallback
            android.util.Log.e("OccupationSelection", "Error opening roadmap detail", e)
            // Could show a toast or dialog here
        }
    }
    
    private fun getRecommendedOccupations(type: String): List<String> {
        return when (type) {
            "R" -> listOf(
                "Software Engineer",
                "Mechanical Engineer",
                "Civil Engineer",
                "Electrical Engineer",
                "Automotive Technician",
                "Carpenter",
                "Electrician",
                "Pilot",
                "Architect",
                "Industrial Designer"
            )
            "I" -> listOf(
                "Data Scientist",
                "Research Scientist",
                "Doctor",
                "Psychologist",
                "Mathematician",
                "Physicist",
                "Chemist",
                "Biologist",
                "Market Research Analyst",
                "Software Developer"
            )
            "A" -> listOf(
                "Graphic Designer",
                "UX/UI Designer",
                "Artist",
                "Writer",
                "Musician",
                "Actor",
                "Photographer",
                "Interior Designer",
                "Fashion Designer",
                "Video Editor"
            )
            "S" -> listOf(
                "Teacher",
                "Counselor",
                "Social Worker",
                "Nurse",
                "Therapist",
                "Human Resources Manager",
                "Customer Service Representative",
                "Community Outreach Coordinator",
                "School Administrator",
                "Healthcare Administrator"
            )
            "E" -> listOf(
                "Business Manager",
                "Sales Representative",
                "Entrepreneur",
                "Marketing Manager",
                "Lawyer",
                "Politician",
                "Real Estate Agent",
                "Financial Advisor",
                "Project Manager",
                "Operations Manager"
            )
            "C" -> listOf(
                "Accountant",
                "Financial Analyst",
                "Data Entry Clerk",
                "Administrative Assistant",
                "Librarian",
                "Bank Teller",
                "Bookkeeper",
                "Quality Control Inspector",
                "Office Manager",
                "Records Clerk"
            )
            else -> listOf(
                "Teacher",
                "Counselor",
                "Social Worker",
                "Nurse",
                "Therapist",
                "Human Resources Manager",
                "Customer Service Representative",
                "Healthcare Administrator"
            )
        }
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
}

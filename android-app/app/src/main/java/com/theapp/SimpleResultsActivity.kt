package com.theapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.theapp.databinding.ActivityResultsBinding

class SimpleResultsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityResultsBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
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
        
        // Set UI values
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
        
        // Hide occupation selection components to prevent crashes
        binding.spinnerOccupations.visibility = android.view.View.GONE
        binding.btnShowRoadmap.visibility = android.view.View.GONE
        binding.cardRoadmapDisplay.visibility = android.view.View.GONE
        
        binding.btnRestart.setOnClickListener {
            finish()
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
            "R" -> "Practical, hands-on problem solvers"
            "I" -> "Analytical, intellectual, scientific"
            "A" -> "Creative, expressive, original"
            "S" -> "Helpful, caring, people-oriented"
            "E" -> "Persuasive, ambitious, leadership-oriented"
            "C" -> "Organized, detail-oriented, systematic"
            else -> "Helpful, caring, people-oriented"
        }
    }
    
    private fun getCareers(type: String): String {
        return when (type) {
            "R" -> "Engineer, Technician, Mechanic, Electrician, Carpenter"
            "I" -> "Scientist, Researcher, Doctor, Analyst, Programmer"
            "A" -> "Artist, Designer, Writer, Musician, Actor"
            "S" -> "Teacher, Counselor, Social Worker, Nurse, Therapist"
            "E" -> "Manager, Sales Representative, Entrepreneur, Lawyer, Politician"
            "C" -> "Accountant, Clerk, Secretary, Librarian, Data Entry"
            else -> "Teacher, Counselor, Social Worker, Nurse, Therapist"
        }
    }
}

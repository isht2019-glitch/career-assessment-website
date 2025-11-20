package com.theapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.theapp.databinding.ActivityRoadmapDetailBinding
import kotlinx.coroutines.*

/**
 * NEW RoadmapDetailActivity with AI Generation
 * Replace the old RoadmapDetailActivity.kt with this code
 */
class RoadmapDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRoadmapDetailBinding
    private var roadmapJob: Job? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityRoadmapDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val occupationTitle = intent.getStringExtra("occupation_title") ?: "Unknown Occupation"
        
        // Set header
        binding.tvOccupationTitle?.text = occupationTitle
        binding.tvLoadingMessage?.text = "🤖 AI is generating your personalized roadmap..."
        
        // Load AI-generated roadmap
        loadAIRoadmap(occupationTitle)
        
        // Back button
        binding.btnBack?.setOnClickListener {
            finish()
        }
        
        // Share button
        binding.btnShare?.setOnClickListener {
            shareRoadmap(occupationTitle)
        }
    }
    
    private fun loadAIRoadmap(occupation: String) {
        // Show loading state
        binding.progressBar?.visibility = View.VISIBLE
        binding.tvLoadingMessage?.visibility = View.VISIBLE
        binding.scrollViewRoadmap?.visibility = View.GONE
        
        // Cancel any existing job
        roadmapJob?.cancel()
        
        // Generate AI roadmap
        roadmapJob = CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    GeminiApiService.generateRoadmap(occupation)
                }
                
                when (result) {
                    is GeminiApiService.RoadmapResult.Success -> {
                        displayRoadmap(result.roadmap)
                    }
                    is GeminiApiService.RoadmapResult.Error -> {
                        showError(result.message)
                    }
                }
                
            } catch (e: Exception) {
                showError(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    private fun displayRoadmap(roadmapText: String) {
        // Hide loading
        binding.progressBar?.visibility = View.GONE
        binding.tvLoadingMessage?.visibility = View.GONE
        binding.scrollViewRoadmap?.visibility = View.VISIBLE
        
        // Format and display the AI-generated roadmap
        val formattedHtml = formatRoadmapAsHtml(roadmapText)
        binding.tvRoadmapContent?.text = android.text.Html.fromHtml(
            formattedHtml,
            android.text.Html.FROM_HTML_MODE_LEGACY
        )
    }
    
    private fun showError(message: String) {
        binding.progressBar?.visibility = View.GONE
        binding.tvLoadingMessage?.visibility = View.VISIBLE
        binding.tvLoadingMessage?.text = "❌ Error: $message\n\nPlease check your internet connection and try again."
        binding.scrollViewRoadmap?.visibility = View.GONE
        
        Toast.makeText(this, "Failed to generate roadmap", Toast.LENGTH_LONG).show()
    }
    
    private fun formatRoadmapAsHtml(text: String): String {
        var html = text
        
        // Convert markdown-style headers to HTML
        html = html.replace(Regex("\\*\\*(.+?)\\*\\*"), "<b>$1</b>")
        html = html.replace(Regex("^# (.+)$", RegexOption.MULTILINE), "<h1>$1</h1>")
        html = html.replace(Regex("^## (.+)$", RegexOption.MULTILINE), "<h2>$1</h2>")
        html = html.replace(Regex("^### (.+)$", RegexOption.MULTILINE), "<h3>$1</h3>")
        
        // Convert bullet points
        html = html.replace(Regex("^- (.+)$", RegexOption.MULTILINE), "• $1")
        
        // Add line breaks
        html = html.replace("\n", "<br>")
        
        // Add some styling
        return """
            <style>
                body { font-family: sans-serif; line-height: 1.6; }
                h1 { color: #667eea; font-size: 24px; margin-top: 20px; }
                h2 { color: #764ba2; font-size: 20px; margin-top: 16px; }
                h3 { color: #667eea; font-size: 18px; margin-top: 12px; }
                b { color: #667eea; }
            </style>
            $html
        """.trimIndent()
    }
    
    private fun shareRoadmap(occupation: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Career Roadmap: $occupation")
            putExtra(Intent.EXTRA_TEXT, "Check out this career roadmap for $occupation from TheApp!")
        }
        startActivity(Intent.createChooser(shareIntent, "Share Roadmap"))
    }
    
    override fun onDestroy() {
        super.onDestroy()
        roadmapJob?.cancel()
    }
}

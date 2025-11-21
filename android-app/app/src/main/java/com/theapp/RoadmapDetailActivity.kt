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
    private var fullRoadmapText: String = ""
    private var isComprehensiveMode: Boolean = false
    
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
        
        // Home button
        binding.btnBackToHome?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }
        
        // Survey/Feedback button
        binding.btnSurvey?.setOnClickListener {
            startActivity(Intent(this, SurveyActivity::class.java))
            finish()
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
        
        // Store full roadmap
        fullRoadmapText = roadmapText
        
        // Clean up the roadmap text - remove unrelated content
        val cleanedText = cleanRoadmapText(roadmapText)
        
        // Show full content by default (no truncation)
        val displayText = cleanedText
        
        // Format and display the AI-generated roadmap
        val formattedHtml = formatRoadmapAsHtml(displayText)
        binding.tvRoadmapContent?.text = android.text.Html.fromHtml(
            formattedHtml,
            android.text.Html.FROM_HTML_MODE_LEGACY
        )
        
        // Add Load More button (for future use if needed)
        addLoadMoreButton()
    }
    
    private fun addLoadMoreButton() {
        val loadMoreBtn = android.widget.Button(this).apply {
            text = if (isComprehensiveMode) "Show Less" else "Load More"
            setTextColor(getColor(android.R.color.white))
            setBackgroundColor(getColor(R.color.purple_500))
            layoutParams = android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(32, 16, 32, 16)
            }
            setPadding(0, 20, 0, 20)
            setOnClickListener {
                toggleComprehensiveMode()
            }
        }
        
        // Remove old button if exists
        binding.root.findViewWithTag<android.widget.Button>("loadMoreBtn")?.let {
            binding.root.removeView(it)
        }
        
        loadMoreBtn.tag = "loadMoreBtn"
        binding.root.addView(loadMoreBtn)
    }
    
    private fun toggleComprehensiveMode() {
        isComprehensiveMode = !isComprehensiveMode
        
        val cleanedText = cleanRoadmapText(fullRoadmapText)
        // Always show full content
        val displayText = cleanedText
        
        val formattedHtml = formatRoadmapAsHtml(displayText)
        binding.tvRoadmapContent?.text = android.text.Html.fromHtml(
            formattedHtml,
            android.text.Html.FROM_HTML_MODE_LEGACY
        )
        
        // Update button text
        binding.root.findViewWithTag<android.widget.Button>("loadMoreBtn")?.apply {
            text = "✅ Full Roadmap Loaded"
            isEnabled = false
        }
        
        // Scroll to top
        binding.scrollViewRoadmap?.smoothScrollTo(0, 0)
    }
    
    private fun cleanRoadmapText(text: String): String {
        // Remove width/length/dimension related content
        var cleaned = text
        
        // Remove lines containing width, length, dimensions, etc.
        cleaned = cleaned.split("\n").filter { line ->
            val lower = line.lowercase()
            !lower.contains("width") && 
            !lower.contains("length") && 
            !lower.contains("dimension") &&
            !lower.contains("km") &&
            !lower.contains("meter") &&
            !lower.contains("feet") &&
            !lower.contains("inch") &&
            !lower.contains("cm") &&
            !lower.contains("mm") &&
            !lower.contains("calculate") &&
            !lower.contains("problem") &&
            !lower.contains("math") &&
            !lower.contains("answer:") &&
            !lower.contains("solution:") &&
            !lower.contains("ratio") &&
            !lower.contains("percentage") &&
            !lower.contains("profit") &&
            !lower.contains("cost price") &&
            !lower.contains("selling price")
        }.joinToString("\n")
        
        // Remove any remaining math-like content
        cleaned = cleaned.replace(Regex("\\d+\\s*km.*?(?=\\n|$)"), "")
        cleaned = cleaned.replace(Regex(".*?\\d+\\s*(?:hours?|minutes?|seconds?).*?(?=\\n|$)"), "")
        
        return cleaned.trim()
    }
    
    private fun showError(message: String) {
        binding.progressBar?.visibility = View.GONE
        binding.tvLoadingMessage?.visibility = View.VISIBLE

        val lower = message.lowercase()
        val finalText = if ("quota" in lower || "exhausted" in lower || "api key" in lower) {
            "❌ AI roadmap quota reached for this API key.\n\n" +
                "Please add your own Gemini API key in the app config, or try again after the daily limit resets."
        } else {
            "❌ Error: $message\n\nPlease check your internet connection and try again."
        }

        binding.tvLoadingMessage?.text = finalText
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
        
        // Return plain HTML without style tags (Android Html.fromHtml doesn't support style tags)
        return html
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

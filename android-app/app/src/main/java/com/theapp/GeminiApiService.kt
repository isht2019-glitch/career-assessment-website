package com.theapp

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Google Gemini API Service for AI Roadmap Generation
 * Uses gemini-1.5-flash model
 */
object GeminiApiService {
    
    private const val TAG = "GeminiAPI"
    private const val API_KEY = "AIzaSyAgkLm9NEBiNJvsdw1JpYJRpS8A-B3-xt8"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$API_KEY"
    
    /**
     * Generate career roadmap using AI
     */
    suspend fun generateRoadmap(occupationName: String): RoadmapResult = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🤖 Generating AI roadmap for: $occupationName")
            
            val prompt = buildPrompt(occupationName)
            val response = callGeminiApi(prompt)
            
            Log.d(TAG, "✅ AI roadmap generated successfully")
            RoadmapResult.Success(response)
            
        } catch (e: java.net.UnknownHostException) {
            Log.e(TAG, "❌ DNS Error: Cannot resolve host", e)
            RoadmapResult.Error("Unable to connect to AI service. Please check your internet connection and DNS settings.")
        } catch (e: java.net.SocketTimeoutException) {
            Log.e(TAG, "❌ Timeout Error", e)
            RoadmapResult.Error("Connection timeout. Please check your internet connection and try again.")
        } catch (e: java.io.IOException) {
            Log.e(TAG, "❌ Network Error", e)
            RoadmapResult.Error("Network error: ${e.message}. Please check your internet connection.")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error generating roadmap: ${e.message}", e)
            RoadmapResult.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    private fun buildPrompt(occupationName: String): String {
        return """
Generate a DETAILED 12-PHASE career roadmap for: $occupationName

Create a comprehensive, structured roadmap with these EXACT 12 phases:

**Phase 1: Career Overview**
- What is this career?
- Key responsibilities
- Why pursue this career?

**Phase 2: Education Requirements**
- Required degrees/certifications
- Recommended institutions/programs
- Typical duration

**Phase 3: Essential Skills (Technical)**
- Core technical skills
- Tools and software
- Industry-specific knowledge

**Phase 4: Essential Skills (Soft Skills)**
- Communication
- Leadership
- Problem-solving
- Teamwork

**Phase 5: Entry-Level Preparation**
- Internships/apprenticeships
- Entry-level certifications
- Portfolio/projects needed

**Phase 6: Entry-Level Positions**
- Job titles
- Typical responsibilities
- Salary range (USD)
- Timeline to reach

**Phase 7: Mid-Career Development**
- Advancement opportunities
- Additional certifications
- Specializations available

**Phase 8: Mid-Career Positions**
- Senior/specialist roles
- Increased responsibilities
- Salary range (USD)
- Years of experience needed

**Phase 9: Advanced Skills & Leadership**
- Management skills
- Strategic thinking
- Industry expertise

**Phase 10: Senior/Leadership Positions**
- Executive roles
- Consulting opportunities
- Salary range (USD)

**Phase 11: Industry Outlook & Trends**
- Job market growth
- Future demand
- Emerging opportunities
- Industry changes

**Phase 12: Getting Started Today**
- First steps (this month)
- Resources and platforms
- Networking groups
- Recommended learning paths

IMPORTANT: 
- Only include relevant career information
- Do NOT include math problems, width/length/dimensions, geometry, or unrelated material
- Do NOT include any CSS, HTML, or code snippets
- Use clear formatting with bullet points
- Provide realistic salary ranges
- Make it specific to $occupationName, not generic
- Keep each phase concise but detailed
- Format as plain text with markdown (**, #, ##, ###, -)
- NO style tags, NO HTML tags, NO code blocks
        """.trimIndent()
    }
    
    private suspend fun callGeminiApi(prompt: String): String {
        val url = URL(BASE_URL)
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true
            
            val requestBody = """
                {
                    "contents": [{
                        "parts": [{
                            "text": ${JSONObject.quote(prompt)}
                        }]
                    }],
                    "generationConfig": {
                        "temperature": 0.7,
                        "topK": 40,
                        "topP": 0.95,
                        "maxOutputTokens": 4096,
                        "stopSequences": []
                    },
                    "safetySettings": [
                        {
                            "category": "HARM_CATEGORY_HARASSMENT",
                            "threshold": "BLOCK_MEDIUM_AND_ABOVE"
                        },
                        {
                            "category": "HARM_CATEGORY_HATE_SPEECH",
                            "threshold": "BLOCK_MEDIUM_AND_ABOVE"
                        },
                        {
                            "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                            "threshold": "BLOCK_MEDIUM_AND_ABOVE"
                        },
                        {
                            "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
                            "threshold": "BLOCK_MEDIUM_AND_ABOVE"
                        }
                    ]
                }
            """.trimIndent()
            
            // Send request body once
            connection.outputStream.bufferedWriter().use { writer ->
                writer.write(requestBody)
            }
            
            // Read response
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                return parseGeminiResponse(response)
            } else {
                val rawErrorBody = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "No error details"
                Log.e(TAG, "Gemini API error $responseCode: $rawErrorBody")

                // Handle quota / rate limit errors (429) with a friendly message
                if (responseCode == 429) {
                    throw Exception(
                        "Our AI roadmap quota for this API key is exhausted right now. " +
                            "Please try again later today, or configure your own Gemini API key in the app settings/config."
                    )
                }

                // Try to extract a concise error message from the JSON, if present
                val friendlyMessage = try {
                    val json = JSONObject(rawErrorBody)
                    if (json.has("error")) {
                        val err = json.getJSONObject("error")
                        val msg = err.optString("message", "")
                        if (msg.isNotBlank()) msg else "Unexpected error from AI service (code $responseCode)."
                    } else {
                        "Unexpected error from AI service (code $responseCode)."
                    }
                } catch (e: Exception) {
                    "Unexpected error from AI service (code $responseCode)."
                }

                throw Exception(friendlyMessage)
            }
            
        } finally {
            connection.disconnect()
        }
    }
    
    private fun parseGeminiResponse(response: String): String {
        val json = JSONObject(response)
        val candidates = json.getJSONArray("candidates")
        
        if (candidates.length() > 0) {
            val candidate = candidates.getJSONObject(0)
            val content = candidate.getJSONObject("content")
            val parts = content.getJSONArray("parts")
            
            if (parts.length() > 0) {
                val part = parts.getJSONObject(0)
                return part.getString("text")
            }
        }
        
        throw Exception("No content in API response")
    }
    
    sealed class RoadmapResult {
        data class Success(val roadmap: String) : RoadmapResult()
        data class Error(val message: String) : RoadmapResult()
    }
}

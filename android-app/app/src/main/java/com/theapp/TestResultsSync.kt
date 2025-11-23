package com.theapp

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 * TestResultsSync - Syncs test results with Firebase for cross-platform access
 * Allows users to start test on web and continue on app (or vice versa)
 */
object TestResultsSync {
    
    private val db = FirebaseFirestore.getInstance()
    private const val COLLECTION_NAME = "userTestResults"
    
    /**
     * Save test results to Firebase
     * This allows the results to be accessed from web app too
     */
    fun saveTestResultsToFirebase(
        context: Context,
        email: String,
        dominantType: String,
        aptitudeScore: Int,
        rScore: Int,
        iScore: Int,
        aScore: Int,
        sScore: Int,
        eScore: Int,
        cScore: Int
    ) {
        try {
            val testData = mapOf(
                "email" to email.lowercase(),
                "dominantType" to dominantType,
                "aptitudeScore" to aptitudeScore,
                "rScore" to rScore,
                "iScore" to iScore,
                "aScore" to aScore,
                "sScore" to sScore,
                "eScore" to eScore,
                "cScore" to cScore,
                "timestamp" to System.currentTimeMillis(),
                "platform" to "android"
            )
            
            // Use email as document ID for easy lookup
            db.collection(COLLECTION_NAME)
                .document(email.lowercase())
                .set(testData, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("TestResultsSync", "✅ Test results saved to Firebase for: $email")
                }
                .addOnFailureListener { e ->
                    Log.e("TestResultsSync", "❌ Error saving test results to Firebase: ${e.message}", e)
                }
        } catch (e: Exception) {
            Log.e("TestResultsSync", "❌ Error saving test results: ${e.message}", e)
        }
    }
    
    /**
     * Fetch test results from Firebase for a given email
     * This allows app to load results saved from web
     */
    fun fetchTestResultsFromFirebase(
        email: String,
        onSuccess: (TestResults?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            db.collection(COLLECTION_NAME)
                .document(email.lowercase())
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val results = TestResults(
                            dominantType = document.getString("dominantType") ?: "",
                            aptitudeScore = document.getLong("aptitudeScore")?.toInt() ?: 0,
                            rScore = document.getLong("rScore")?.toInt() ?: 0,
                            iScore = document.getLong("iScore")?.toInt() ?: 0,
                            aScore = document.getLong("aScore")?.toInt() ?: 0,
                            sScore = document.getLong("sScore")?.toInt() ?: 0,
                            eScore = document.getLong("eScore")?.toInt() ?: 0,
                            cScore = document.getLong("cScore")?.toInt() ?: 0
                        )
                        Log.d("TestResultsSync", "✅ Fetched test results from Firebase for: $email")
                        onSuccess(results)
                    } else {
                        Log.d("TestResultsSync", "⚠️ No test results found in Firebase for: $email")
                        onSuccess(null)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("TestResultsSync", "❌ Error fetching test results: ${e.message}", e)
                    onFailure(e)
                }
        } catch (e: Exception) {
            Log.e("TestResultsSync", "❌ Error fetching test results: ${e.message}", e)
            onFailure(e)
        }
    }
    
    /**
     * Delete test results from Firebase (for account deletion)
     */
    fun deleteTestResultsFromFirebase(email: String) {
        try {
            db.collection(COLLECTION_NAME)
                .document(email.lowercase())
                .delete()
                .addOnSuccessListener {
                    Log.d("TestResultsSync", "✅ Test results deleted from Firebase for: $email")
                }
                .addOnFailureListener { e ->
                    Log.e("TestResultsSync", "❌ Error deleting test results: ${e.message}", e)
                }
        } catch (e: Exception) {
            Log.e("TestResultsSync", "❌ Error deleting test results: ${e.message}", e)
        }
    }
    
    /**
     * Data class for test results
     */
    data class TestResults(
        val dominantType: String,
        val aptitudeScore: Int,
        val rScore: Int,
        val iScore: Int,
        val aScore: Int,
        val sScore: Int,
        val eScore: Int,
        val cScore: Int
    )
}

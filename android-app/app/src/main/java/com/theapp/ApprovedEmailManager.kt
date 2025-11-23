package com.theapp

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

/**
 * ApprovedEmailManager - Manages approved emails for payment
 * Stores and retrieves emails that have been approved for payment
 * This prevents users from being asked to pay again
 */
object ApprovedEmailManager {
    
    private const val PREF_NAME = "approved_emails_prefs"
    private const val KEY_APPROVED_EMAILS = "approved_emails_list"
    private const val KEY_LAST_SYNC = "last_sync_time"
    private const val SYNC_INTERVAL = 3600000L // 1 hour in milliseconds
    
    private val db = FirebaseFirestore.getInstance()
    
    /**
     * Get SharedPreferences instance
     */
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * Check if an email has been approved for payment
     */
    fun isEmailApproved(context: Context, email: String): Boolean {
        val approvedEmails = getApprovedEmails(context)
        return approvedEmails.contains(email.lowercase())
    }
    
    /**
     * Get list of all approved emails from local storage
     */
    fun getApprovedEmails(context: Context): Set<String> {
        return getPrefs(context).getStringSet(KEY_APPROVED_EMAILS, emptySet()) ?: emptySet()
    }
    
    /**
     * Add an email to approved list
     */
    fun addApprovedEmail(context: Context, email: String) {
        val approvedEmails = getApprovedEmails(context).toMutableSet()
        approvedEmails.add(email.lowercase())
        
        with(getPrefs(context).edit()) {
            putStringSet(KEY_APPROVED_EMAILS, approvedEmails)
            apply()
        }
        
        Log.d("ApprovedEmailManager", "✅ Email approved and stored: $email")
    }
    
    /**
     * Sync approved emails from Firestore (admin panel)
     * This fetches the latest list of approved emails from the database
     */
    fun syncApprovedEmails(context: Context) {
        try {
            val lastSync = getPrefs(context).getLong(KEY_LAST_SYNC, 0L)
            val now = System.currentTimeMillis()
            
            // Only sync if enough time has passed
            if (now - lastSync < SYNC_INTERVAL) {
                Log.d("ApprovedEmailManager", "⏭️ Skipping sync - too soon (${(now - lastSync) / 1000}s ago)")
                return
            }
            
            Log.d("ApprovedEmailManager", "🔄 Syncing approved emails from Firestore...")
            
            db.collection("paymentRequests")
                .whereEqualTo("status", "approved")
                .get()
                .addOnSuccessListener { snapshot ->
                    val approvedEmails = mutableSetOf<String>()
                    for (doc in snapshot.documents) {
                        val email = doc.getString("email")
                        if (email != null) {
                            approvedEmails.add(email.lowercase())
                        }
                    }
                    
                    // Save to local storage
                    with(getPrefs(context).edit()) {
                        putStringSet(KEY_APPROVED_EMAILS, approvedEmails)
                        putLong(KEY_LAST_SYNC, now)
                        apply()
                    }
                    
                    Log.d("ApprovedEmailManager", "✅ Synced ${approvedEmails.size} approved emails")
                }
                .addOnFailureListener { e ->
                    Log.e("ApprovedEmailManager", "❌ Error syncing approved emails: ${e.message}", e)
                }
        } catch (e: Exception) {
            Log.e("ApprovedEmailManager", "❌ Error syncing approved emails: ${e.message}", e)
        }
    }
    
    /**
     * Remove a specific email from approved list
     */
    fun removeApprovedEmail(context: Context, email: String) {
        val approvedEmails = getApprovedEmails(context).toMutableSet()
        approvedEmails.remove(email.lowercase())
        
        with(getPrefs(context).edit()) {
            putStringSet(KEY_APPROVED_EMAILS, approvedEmails)
            apply()
        }
        
        Log.d("ApprovedEmailManager", "🗑️ Removed email from approved list: $email")
    }
    
    /**
     * Clear all approved emails (for testing or logout)
     */
    fun clearApprovedEmails(context: Context) {
        with(getPrefs(context).edit()) {
            remove(KEY_APPROVED_EMAILS)
            remove(KEY_LAST_SYNC)
            apply()
        }
        Log.d("ApprovedEmailManager", "🗑️ Cleared all approved emails")
    }
}

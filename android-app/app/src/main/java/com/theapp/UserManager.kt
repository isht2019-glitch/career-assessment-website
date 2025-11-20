package com.theapp

import android.content.Context
import android.content.SharedPreferences

/**
 * UserManager - Centralized user data management
 * Handles storage and retrieval of user information using SharedPreferences
 */
object UserManager {
    
    private const val PREF_NAME = "theapp_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_LOGIN_DATE = "login_date"
    private const val KEY_TEST_COMPLETED = "test_completed"
    private const val KEY_TEST_R_SCORE = "test_r_score"
    private const val KEY_TEST_I_SCORE = "test_i_score"
    private const val KEY_TEST_A_SCORE = "test_a_score"
    private const val KEY_TEST_S_SCORE = "test_s_score"
    private const val KEY_TEST_E_SCORE = "test_e_score"
    private const val KEY_TEST_C_SCORE = "test_c_score"
    private const val KEY_TEST_APTITUDE_SCORE = "test_aptitude_score"
    private const val KEY_TEST_DOMINANT_TYPE = "test_dominant_type"
    private const val KEY_PAYMENT_APPROVED = "payment_approved"
    private const val KEY_PAYMENT_APPROVED_DATE = "payment_approved_date"
    
    /**
     * Get SharedPreferences instance
     */
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Get stored user email
     * @return Email string or null if not found
     */
    fun getUserEmail(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_EMAIL, null)
    }
    
    /**
     * Get login timestamp
     * @return Login timestamp in milliseconds or 0 if not found
     */
    fun getLoginDate(context: Context): Long {
        return getPrefs(context).getLong(KEY_LOGIN_DATE, 0L)
    }
    
    /**
     * Save user login information
     */
    fun saveUserLogin(context: Context, email: String) {
        with(getPrefs(context).edit()) {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_EMAIL, email)
            putLong(KEY_LOGIN_DATE, System.currentTimeMillis())
            apply()
        }
    }
    
    /**
     * Clear all user data (logout)
     */
    fun clearUserData(context: Context) {
        with(getPrefs(context).edit()) {
            remove(KEY_IS_LOGGED_IN)
            remove(KEY_USER_EMAIL)
            remove(KEY_LOGIN_DATE)
            remove(KEY_TEST_COMPLETED)
            remove(KEY_TEST_R_SCORE)
            remove(KEY_TEST_I_SCORE)
            remove(KEY_TEST_A_SCORE)
            remove(KEY_TEST_S_SCORE)
            remove(KEY_TEST_E_SCORE)
            remove(KEY_TEST_C_SCORE)
            remove(KEY_TEST_APTITUDE_SCORE)
            remove(KEY_TEST_DOMINANT_TYPE)
            apply()
        }
    }

    data class StoredTestResults(
        val dominantType: String,
        val aptitudeScore: Int,
        val rScore: Int,
        val iScore: Int,
        val aScore: Int,
        val sScore: Int,
        val eScore: Int,
        val cScore: Int
    )

    fun saveTestResults(
        context: Context,
        riasecScores: Map<String, Int>,
        aptitudeScore: Int,
        dominantType: String
    ) {
        with(getPrefs(context).edit()) {
            putBoolean(KEY_TEST_COMPLETED, true)
            putInt(KEY_TEST_R_SCORE, riasecScores["R"] ?: 0)
            putInt(KEY_TEST_I_SCORE, riasecScores["I"] ?: 0)
            putInt(KEY_TEST_A_SCORE, riasecScores["A"] ?: 0)
            putInt(KEY_TEST_S_SCORE, riasecScores["S"] ?: 0)
            putInt(KEY_TEST_E_SCORE, riasecScores["E"] ?: 0)
            putInt(KEY_TEST_C_SCORE, riasecScores["C"] ?: 0)
            putInt(KEY_TEST_APTITUDE_SCORE, aptitudeScore)
            putString(KEY_TEST_DOMINANT_TYPE, dominantType)
            apply()
        }
    }

    fun hasCompletedTest(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_TEST_COMPLETED, false)
    }

    fun getStoredTestResults(context: Context): StoredTestResults? {
        val prefs = getPrefs(context)
        if (!prefs.getBoolean(KEY_TEST_COMPLETED, false)) return null
        val dominantType = prefs.getString(KEY_TEST_DOMINANT_TYPE, null) ?: return null
        val aptitude = prefs.getInt(KEY_TEST_APTITUDE_SCORE, 0)
        val r = prefs.getInt(KEY_TEST_R_SCORE, 0)
        val i = prefs.getInt(KEY_TEST_I_SCORE, 0)
        val a = prefs.getInt(KEY_TEST_A_SCORE, 0)
        val s = prefs.getInt(KEY_TEST_S_SCORE, 0)
        val e = prefs.getInt(KEY_TEST_E_SCORE, 0)
        val c = prefs.getInt(KEY_TEST_C_SCORE, 0)
        return StoredTestResults(dominantType, aptitude, r, i, a, s, e, c)
    }
    
    /**
     * Get user display name (email prefix before @)
     * Example: user@example.com -> user
     */
    fun getUserDisplayName(context: Context): String {
        val email = getUserEmail(context) ?: return "User"
        return email.substringBefore("@").replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase() else it.toString() 
        }
    }
    
    /**
     * Check if payment is approved for this user
     */
    fun isPaymentApproved(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_PAYMENT_APPROVED, false)
    }
    
    /**
     * Mark payment as approved
     */
    fun setPaymentApproved(context: Context, approved: Boolean) {
        with(getPrefs(context).edit()) {
            putBoolean(KEY_PAYMENT_APPROVED, approved)
            if (approved) {
                putLong(KEY_PAYMENT_APPROVED_DATE, System.currentTimeMillis())
            }
            apply()
        }
    }
    
    /**
     * Get payment approval date
     */
    fun getPaymentApprovedDate(context: Context): Long {
        return getPrefs(context).getLong(KEY_PAYMENT_APPROVED_DATE, 0L)
    }
}

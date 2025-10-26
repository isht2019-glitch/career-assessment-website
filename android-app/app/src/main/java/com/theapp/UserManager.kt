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
            apply()
        }
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
}

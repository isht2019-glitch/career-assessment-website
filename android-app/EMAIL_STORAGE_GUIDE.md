# 📧 Email Storage Guide

## Overview
This document explains how user email addresses are stored and managed in the Android career assessment app.

---

## ✅ **Current Implementation**

### **Storage Method: SharedPreferences**

**Location:** `theapp_prefs` (private to the app)

**Data Structure:**
```kotlin
SharedPreferences("theapp_prefs") {
    "is_logged_in" -> Boolean
    "user_email" -> String
    "login_date" -> Long (timestamp in milliseconds)
}
```

---

## 🔧 **How It Works**

### **1. Email Capture (AuthActivity.kt)**

**Email/Password Sign Up:**
```kotlin
val email = binding.etEmail.text.toString().trim()
// Validates email format using Android Patterns.EMAIL_ADDRESS
// Stores email on successful signup
```

**Email/Password Sign In:**
```kotlin
val email = binding.etSignInEmail.text.toString().trim()
// Stores email on successful login
```

**Google Sign-In:**
```kotlin
val email = "google.user@gmail.com" // Placeholder
// Note: Real Google email would come from GoogleSignInAccount
```

**Phone OTP:**
```kotlin
val phone = "1234567890"
val email = "$phone@phone.user" // Converted to email format
```

---

### **2. Email Storage (UserManager.kt)**

**Centralized management through `UserManager` singleton:**

```kotlin
// Save user login with email
UserManager.saveUserLogin(context, email)

// Retrieve user email
val email: String? = UserManager.getUserEmail(context)

// Check if user is logged in
val isLoggedIn: Boolean = UserManager.isLoggedIn(context)

// Get user display name (from email prefix)
val displayName: String = UserManager.getUserDisplayName(context)
// Example: "user@example.com" -> "User"

// Clear user data (logout)
UserManager.clearUserData(context)
```

---

## 📂 **Storage Details**

### **File Location on Device:**
```
/data/data/com.theapp/shared_prefs/theapp_prefs.xml
```

### **XML Structure:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<map>
    <boolean name="is_logged_in" value="true" />
    <string name="user_email">user@example.com</string>
    <long name="login_date" value="1696838400000" />
</map>
```

---

## 🔒 **Security Considerations**

### **Current State:**
- ✅ **Private to app** - Other apps cannot access this data
- ✅ **Persists between sessions** - Survives app restarts
- ✅ **Survives app updates** - Data preserved during updates
- ❌ **NOT encrypted by default** - Stored in plain text XML
- ❌ **Lost on app uninstall** - Data deleted when app is removed
- ❌ **Accessible on rooted devices** - Root users can read the file

### **⚠️ Security Warnings:**
1. **Plain Text Storage** - Email is stored unencrypted
2. **No Password Storage** - Passwords are NOT saved (good!)
3. **Rooted Devices** - Data can be read with root access
4. **Backup Vulnerability** - May be included in Android backups

---

## 🔐 **Recommended Improvements** (Future)

### **Option 1: EncryptedSharedPreferences**
```kotlin
// Use Android Jetpack Security library
val masterKey = MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

val encryptedPrefs = EncryptedSharedPreferences.create(
    context,
    "secure_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

**Pros:**
- ✅ Encrypted at rest
- ✅ Easy to implement
- ✅ Same API as SharedPreferences

**Cons:**
- ❌ Requires dependency: `androidx.security:security-crypto`
- ❌ Slightly slower performance

---

### **Option 2: Room Database with SQLCipher**
```kotlin
// Use SQLCipher for encrypted database
val db = Room.databaseBuilder(context, AppDatabase::class.java, "app-db")
    .openHelperFactory(SupportFactory(SQLiteDatabase.getBytes("password".toCharArray())))
    .build()
```

**Pros:**
- ✅ Full database encryption
- ✅ Better for complex data structures
- ✅ Query capabilities

**Cons:**
- ❌ More complex setup
- ❌ Heavier dependency
- ❌ Overkill for simple key-value storage

---

### **Option 3: Firebase Authentication**
```kotlin
// Store user data in Firebase (already commented out in AuthActivity)
val user = FirebaseAuth.getInstance().currentUser
val email = user?.email
```

**Pros:**
- ✅ Secure server-side storage
- ✅ Cross-device sync
- ✅ Built-in authentication
- ✅ No local storage vulnerabilities

**Cons:**
- ❌ Requires internet connection
- ❌ External dependency on Firebase
- ❌ Requires Google Services setup

---

## 📝 **Usage Examples**

### **Example 1: Display User Email in UI**
```kotlin
class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val email = UserManager.getUserEmail(this)
        binding.tvUserEmail.text = email ?: "No email found"
    }
}
```

### **Example 2: Check Login Before Navigation**
```kotlin
if (UserManager.isLoggedIn(this)) {
    // User is logged in, proceed
    startActivity(Intent(this, TestActivity::class.java))
} else {
    // Redirect to login
    startActivity(Intent(this, AuthActivity::class.java))
}
```

### **Example 3: Logout Implementation**
```kotlin
binding.btnLogout.setOnClickListener {
    // Clear all user data
    UserManager.clearUserData(this)
    
    // Redirect to login screen
    val intent = Intent(this, AuthActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}
```

### **Example 4: Display User Name**
```kotlin
val displayName = UserManager.getUserDisplayName(this)
binding.tvWelcome.text = "Welcome, $displayName!"
// Output: "Welcome, User!" (from user@example.com)
```

---

## 🧪 **Testing**

### **Manual Testing:**
1. Sign up with an email
2. Close the app completely
3. Reopen the app
4. Verify you're still logged in
5. Check email is still available

### **Debugging SharedPreferences:**
```kotlin
// Log all stored data
val prefs = getSharedPreferences("theapp_prefs", MODE_PRIVATE)
Log.d("UserData", "Logged in: ${prefs.getBoolean("is_logged_in", false)}")
Log.d("UserData", "Email: ${prefs.getString("user_email", "none")}")
Log.d("UserData", "Login date: ${prefs.getLong("login_date", 0L)}")
```

### **View SharedPreferences in Device File Explorer:**
1. Open Android Studio
2. View > Tool Windows > Device File Explorer
3. Navigate to: `/data/data/com.theapp/shared_prefs/`
4. Download `theapp_prefs.xml` to inspect

---

## ⚡ **Performance**

### **SharedPreferences Performance:**
- **Read:** < 1ms (very fast, cached in memory)
- **Write:** 10-50ms (async commit using `apply()`)
- **Storage:** < 1KB per email (negligible)

### **Best Practices:**
- ✅ Use `apply()` instead of `commit()` for async writes
- ✅ Don't store large data (< 1MB total)
- ✅ Avoid frequent writes (batching recommended)
- ✅ Read on background thread for large datasets (not needed here)

---

## 🎯 **Summary**

**What's Working:**
- ✅ Email captured during authentication
- ✅ Stored in SharedPreferences
- ✅ Persists between app sessions
- ✅ Centralized management via UserManager
- ✅ Easy to retrieve from anywhere in the app

**What's Not Working:**
- ❌ Google Sign-In uses placeholder email (needs real implementation)
- ❌ No encryption (stored in plain text)
- ❌ No server-side backup

**Next Steps (if needed):**
1. Implement EncryptedSharedPreferences for security
2. Add server-side storage (Firebase/Backend API)
3. Implement real Google Sign-In to get actual Google email
4. Add email verification flow

---

## 📚 **Related Files**

- `AuthActivity.kt` - Email capture and authentication
- `UserManager.kt` - Centralized user data management
- `MainActivity.kt` - Login status check
- `TestActivity.kt` - May need user email for results
- `ResultsActivity.kt` - May need user email for sharing/saving

---

## ❓ **FAQ**

**Q: Where is the password stored?**  
A: Passwords are **NOT stored** at all. This is correct security practice.

**Q: What happens when user uninstalls the app?**  
A: All data in SharedPreferences is deleted automatically.

**Q: Can I access the email from a background service?**  
A: Yes! Use `UserManager.getUserEmail(context)` from anywhere.

**Q: Is the email backed up?**  
A: By default, yes (if Android Auto Backup is enabled). You can disable this in AndroidManifest.xml if needed.

**Q: How do I test with different emails?**  
A: Clear app data from Settings > Apps > TheApp > Storage > Clear Data, then sign up again.

---

**Last Updated:** October 2025  
**Maintained By:** Development Team

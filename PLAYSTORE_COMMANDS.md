# 🔧 Google Play Store Upload - Exact Commands

## 📋 Copy-Paste Commands

### 1️⃣ Build Release AAB

```bash
cd e:\CascadeProjects\windsurf-project\android-app
./gradlew clean bundleRelease
```

**Expected output:**
```
BUILD SUCCESSFUL in Xs
app-release.aab location: app/build/outputs/bundle/release/app-release.aab
```

---

### 2️⃣ Build Release APK (Alternative)

```bash
cd e:\CascadeProjects\windsurf-project\android-app
./gradlew clean assembleRelease
```

**Expected output:**
```
BUILD SUCCESSFUL in Xs
app-release.apk location: app/build/outputs/apk/release/app-release.apk
```

---

### 3️⃣ Create Keystore (If Needed)

```bash
keytool -genkey -v -keystore theapp-release-key.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias theapp-key
```

**When prompted, enter:**
```
Enter keystore password: [YOUR_STRONG_PASSWORD]
Re-enter new password: [YOUR_STRONG_PASSWORD]
What is your first and last name? [Your Name]
What is the name of your organizational unit? [Your Company]
What is the name of your organization? [Your Company]
What is the name of your City or Locality? [Your City]
What is the name of your State or Province? [Your State]
What is the two-letter country code for this unit? [IN]
Is CN=Your Name, OU=Your Company, O=Your Company, L=Your City, ST=Your State, C=IN correct? [yes]
Enter key password (RETURN if same as keystore password): [Press Enter]
```

**Output:**
```
Keystore saved in: theapp-release-key.keystore
```

---

### 4️⃣ Sign APK (If Not Auto-Signed)

```bash
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore theapp-release-key.keystore app-release.apk theapp-key
```

**When prompted:**
```
Enter Passphrase for keystore: [YOUR_KEYSTORE_PASSWORD]
```

---

### 5️⃣ Verify Signed APK

```bash
jarsigner -verify -verbose -certs app-release.apk
```

**Expected output:**
```
jar verified.
```

---

## 📁 File Locations

### After Building

**AAB file:**
```
e:\CascadeProjects\windsurf-project\android-app\app\build\outputs\bundle\release\app-release.aab
```

**APK file:**
```
e:\CascadeProjects\windsurf-project\android-app\app\build\outputs\apk\release\app-release.apk
```

**Keystore file:**
```
theapp-release-key.keystore
(Save this in a safe location!)
```

---

## 🔄 Update Version Code

Edit: `android-app/app/build.gradle`

```gradle
android {
    compileSdkVersion 34

    defaultConfig {
        applicationId "com.theapp"
        minSdkVersion 21
        targetSdkVersion 34
        versionCode 1  // ← Change this for updates
        versionName "1.0.0"  // ← Change this too
    }
}
```

**Version code progression:**
- Initial release: `versionCode 1`, `versionName "1.0.0"`
- First update: `versionCode 2`, `versionName "1.0.1"`
- Second update: `versionCode 3`, `versionName "1.1.0"`
- Major update: `versionCode 4`, `versionName "2.0.0"`

---

## 🎯 Complete Build Process

### For Initial Release

```bash
# 1. Navigate to project
cd e:\CascadeProjects\windsurf-project\android-app

# 2. Clean previous builds
./gradlew clean

# 3. Build release AAB
./gradlew bundleRelease

# 4. Verify build succeeded
# Check: app/build/outputs/bundle/release/app-release.aab exists

# 5. You're ready to upload!
```

### For Updates

```bash
# 1. Edit version code in build.gradle
# Change versionCode from 1 to 2
# Change versionName from "1.0.0" to "1.0.1"

# 2. Navigate to project
cd e:\CascadeProjects\windsurf-project\android-app

# 3. Clean and build
./gradlew clean bundleRelease

# 4. Upload new AAB to Play Console
```

---

## 📱 Play Console URLs

### Main Console
```
https://play.google.com/console
```

### Your App (After Creation)
```
https://play.google.com/console/u/0/developers/[YOUR_DEVELOPER_ID]/app/[YOUR_APP_ID]/dashboard
```

### Internal Testing
```
https://play.google.com/console/u/0/developers/[YOUR_DEVELOPER_ID]/app/[YOUR_APP_ID]/internal-app-sharing
```

### Production Release
```
https://play.google.com/console/u/0/developers/[YOUR_DEVELOPER_ID]/app/[YOUR_APP_ID]/releases/overview
```

---

## 🧪 Testing Commands

### Test on Emulator

```bash
# List available emulators
emulator -list-avds

# Start emulator
emulator -avd [EMULATOR_NAME]

# Install APK on emulator
adb install app/build/outputs/apk/release/app-release.apk

# Uninstall app
adb uninstall com.theapp
```

### Test on Physical Device

```bash
# Connect device via USB

# Enable USB debugging on device:
# Settings → Developer Options → USB Debugging → Enable

# Install APK
adb install app/build/outputs/apk/release/app-release.apk

# View logs
adb logcat | grep "com.theapp"

# Uninstall
adb uninstall com.theapp
```

---

## 🔐 Keystore Management

### Important: Save Your Keystore!

```bash
# Copy keystore to safe location
copy theapp-release-key.keystore "C:\Users\[YOUR_USER]\AppData\Local\TheApp\keystore\"
```

**⚠️ CRITICAL**: You MUST keep this file safe!
- You need it for all future updates
- If lost, you can't update your app
- Back it up to cloud storage

### Retrieve Keystore Info

```bash
keytool -list -v -keystore theapp-release-key.keystore
```

**When prompted:**
```
Enter keystore password: [YOUR_KEYSTORE_PASSWORD]
```

---

## 📊 Gradle Commands Reference

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Build release AAB
./gradlew bundleRelease

# Run tests
./gradlew test

# Check for errors
./gradlew lint

# View build info
./gradlew tasks
```

---

## 🐛 Troubleshooting Commands

### Build Fails

```bash
# Clean and rebuild
./gradlew clean
./gradlew bundleRelease

# Check for errors
./gradlew lint

# View detailed output
./gradlew bundleRelease --info
```

### APK Won't Install

```bash
# Uninstall old version
adb uninstall com.theapp

# Clear cache
adb shell pm clear com.theapp

# Reinstall
adb install app/build/outputs/apk/release/app-release.apk
```

### Check App Version

```bash
# View installed app info
adb shell dumpsys package com.theapp | grep version
```

---

## 📝 Release Notes Template

```
Version 1.0.0 - Initial Release

✨ Features:
• RIASEC personality assessment (30 questions)
• Aptitude testing (20 questions)
• AI-powered career recommendations
• Detailed career roadmaps
• Salary and institute information
• Cross-platform sync with web app
• Account management and deletion

🐛 Bug Fixes:
• Fixed login persistence issue
• Improved payment approval detection
• Enhanced cross-platform data sync

🚀 Performance:
• Optimized Firebase queries
• Improved app startup time
• Better offline support

📱 Compatibility:
• Android 6.0 and above
• Tested on multiple devices
• Optimized for all screen sizes
```

---

## ✅ Pre-Upload Checklist

```bash
# 1. Verify code is committed
git status

# 2. Check version code is incremented
grep "versionCode" android-app/app/build.gradle

# 3. Build the app
./gradlew clean bundleRelease

# 4. Verify AAB exists
ls -la android-app/app/build/outputs/bundle/release/app-release.aab

# 5. Check file size (should be < 100MB)
# If > 100MB, something is wrong

# 6. You're ready to upload!
```

---

## 🎯 Quick Reference

| Task | Command |
|------|---------|
| Build AAB | `./gradlew bundleRelease` |
| Build APK | `./gradlew assembleRelease` |
| Create keystore | `keytool -genkey -v -keystore theapp-release-key.keystore ...` |
| Sign APK | `jarsigner -verbose -sigalg SHA1withRSA ...` |
| Verify APK | `jarsigner -verify -verbose -certs app-release.apk` |
| Install APK | `adb install app-release.apk` |
| View logs | `adb logcat \| grep "com.theapp"` |
| Uninstall | `adb uninstall com.theapp` |

---

## 🚀 One-Command Build

```bash
cd e:\CascadeProjects\windsurf-project\android-app && ./gradlew clean bundleRelease && echo "✅ Build complete! AAB ready at: app/build/outputs/bundle/release/app-release.aab"
```

---

## 📞 Common Error Solutions

### "Gradle build failed"
```bash
./gradlew clean
./gradlew bundleRelease --info
```

### "Version code too low"
Edit `android-app/app/build.gradle` and increment `versionCode`

### "Target SDK too low"
Edit `android-app/app/build.gradle` and set `targetSdkVersion 34`

### "APK not signed"
```bash
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore theapp-release-key.keystore app-release.apk theapp-key
```

---

**Last Updated**: November 24, 2025  
**Status**: Ready to use

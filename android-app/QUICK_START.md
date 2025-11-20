# Android App: Quick Start Guide

## 3-Step Setup

### Step 1: Get API Key
1. Go to https://aistudio.google.com/app/apikey
2. Sign in with Google
3. Click "Create API Key"
4. Copy the key

### Step 2: Add Key to App
Open: `android-app/app/src/main/java/com/theapp/GeminiApiService.kt`

Line 18 - Replace:
```kotlin
private const val API_KEY = "YOUR_GEMINI_API_KEY"
```

With your key:
```kotlin
private const val API_KEY = "AIzaSy..."
```

### Step 3: Build & Run
```
Build > Clean Project
Build > Rebuild Project
Run > Run 'app'
```

---

## What You'll See

### First Time
1. **Login** → Enter email
2. **Payment** → Submit email, wait for approval
3. **Test** → Answer 50 questions (30 personality + 20 aptitude)
4. **Results** → See your personality type & aptitude score
5. **Occupations** → Select a career
6. **Roadmap** → AI generates your career roadmap

### Second Time
1. **Login** → Enter email
2. **Payment** → Wait for approval
3. **Occupations** → Skip test, go straight to careers (results remembered!)
4. **Roadmap** → Select career, see roadmap

---

## Features

✅ **AI Roadmap Generation**
- Uses Google Gemini API (same as web app)
- Generates career-specific roadmaps
- Includes education, skills, salary, job outlook

✅ **Test Result Persistence**
- Results saved after test completion
- Skips test on future app launches
- Stored securely in Android SharedPreferences

✅ **Error Handling**
- Friendly error messages
- Handles quota limits gracefully
- Network error recovery

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Quota exceeded" | Use your own API key (see Step 1-2) |
| "Unable to connect" | Check internet connection |
| Test asked again | Clear app data and retry |
| Blank roadmap | Try different career name |
| App crashes | Check logcat for errors |

---

## File Locations

- **API Service**: `android-app/app/src/main/java/com/theapp/GeminiApiService.kt`
- **Roadmap Display**: `android-app/app/src/main/java/com/theapp/RoadmapDetailActivity.kt`
- **Test Persistence**: `android-app/app/src/main/java/com/theapp/UserManager.kt`
- **Full Guide**: `android-app/ANDROID_ROADMAP_AND_PERSISTENCE_GUIDE.md`

---

## Key Points

🔑 **API Key**: Must be your own (shared key has limited quota)

💾 **Persistence**: Test results stored in SharedPreferences, cleared on logout

🚀 **Performance**: Roadmap generation takes 2-5 seconds (AI processing)

🔒 **Security**: API key visible in source (use backend proxy for production)

---

## Next Steps

1. ✅ Get API key
2. ✅ Add to GeminiApiService.kt
3. ✅ Rebuild app
4. ✅ Complete test
5. ✅ Select career
6. ✅ View roadmap
7. ✅ Close & reopen app (test should be skipped)

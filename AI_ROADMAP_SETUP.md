# 🤖 AI-Powered Roadmap Setup Guide

## ✅ What You Have Now

Your career assessment app now uses **Google Gemini AI** to generate personalized, career-specific roadmaps instead of generic templates!

### Features:
- ✅ **Intelligent roadmaps** for all 386 careers
- ✅ **Career-specific institutes** (NSD for actors, IITs for engineers, etc.)
- ✅ **Relevant skills** (acting techniques, not coding!)
- ✅ **Realistic salaries** with honest caveats
- ✅ **Proper platforms** (Backstage.com for actors, GitHub for developers)
- ✅ **Honest reality checks** about job market and challenges

---

## 🔑 Step 1: Get Your FREE API Key

### Go to Google AI Studio:
👉 **https://makersuite.google.com/app/apikey**

### Steps:
1. **Sign in** with your Google account
2. Click **"Create API Key"** button
3. **Copy** the API key (starts with `AIza...`)
4. Keep it safe!

### Important:
- ✅ **100% FREE** - No credit card required
- ✅ **Generous limits** - 60 requests/minute
- ✅ **No expiration** - Use forever
- ✅ **Easy to get** - Takes 2 minutes

---

## 🛠️ Step 2: Add API Key to Your Website

### Option A: Direct Edit (Easiest)

1. Open `roadmap-viewer.html` in a text editor
2. Find line 124:
   ```javascript
   const GEMINI_API_KEY = 'YOUR_API_KEY_HERE';
   ```
3. Replace `YOUR_API_KEY_HERE` with your actual API key:
   ```javascript
   const GEMINI_API_KEY = 'AIzaSyC1234567890abcdefghijklmnopqrstuvwxyz';
   ```
4. Save the file
5. Done! ✅

### Option B: Using Git

```bash
# Edit the file
code roadmap-viewer.html

# Replace YOUR_API_KEY_HERE with your key

# Commit and push
git add roadmap-viewer.html
git commit -m "Add Gemini API key"
git push origin main
```

---

## 🧪 Step 3: Test It

1. Go to your website: **theapp.work**
2. Complete the personality test
3. View results
4. Select any career from dropdown (e.g., "Actors")
5. Click **"View Roadmap"**
6. Wait 10-20 seconds
7. See AI-generated, career-specific roadmap! 🎉

### What to Expect:

**For "Actors":**
- 🎭 Education: NSD, FTII, Barry John Acting Studio
- 🎯 Skills: Method acting, voice modulation, improvisation
- 💼 Career: Theatre → TV → Films → OTT
- 💰 Salary: Variable, project-based (honest!)
- 🌐 Platforms: Backstage.com, Casting Networks
- ⚠️ Reality: Highly competitive, uncertain income

**For "Software Engineers":**
- 💻 Education: IITs, NITs, top engineering colleges
- 🎯 Skills: Programming, DSA, system design
- 💼 Career: Junior → Senior → Tech Lead → Architect
- 💰 Salary: ₹3-8 LPA → ₹50+ LPA
- 🌐 Platforms: LinkedIn, GitHub, LeetCode
- ⚠️ Reality: Competitive but stable

---

## 📱 Step 4: Android App (Optional)

The Android app can also use AI roadmaps, but it requires:
1. Adding internet permission to `AndroidManifest.xml`
2. Adding HTTP library dependency
3. Implementing the AI generator in Kotlin

**For now, the web app is fully functional with AI!**

---

## ❓ Troubleshooting

### Error: "Invalid response from AI"
- ✅ Check your API key is correct
- ✅ Make sure you copied the entire key
- ✅ Verify you have internet connection
- ✅ Check API quota (60 requests/min)

### Error: "API Key Required"
- ✅ You forgot to replace `YOUR_API_KEY_HERE`
- ✅ Open `roadmap-viewer.html` and add your key

### Roadmap takes too long
- ✅ Normal! AI generation takes 10-20 seconds
- ✅ Be patient, it's generating custom content
- ✅ Faster than manually creating 386 roadmaps!

### Generic roadmap still showing
- ✅ Hard refresh: Ctrl+Shift+R (Windows) or Cmd+Shift+R (Mac)
- ✅ Clear browser cache
- ✅ Check if API key is added correctly

---

## 💰 Cost & Limits

### FREE Tier:
- ✅ **60 requests per minute**
- ✅ **1,500 requests per day**
- ✅ **No credit card required**
- ✅ **No expiration**

### For Your Traffic:
- If you have 100 users/day
- Each views 3 roadmaps = 300 requests/day
- Well within FREE limit! ✅

### If You Need More:
- Paid tier available (very cheap)
- Or create multiple API keys
- Or cache generated roadmaps

---

## 🎯 Summary

### What Changed:
❌ **Before:** Generic template for all careers (wrong info)
✅ **After:** AI generates unique roadmap for each career

### Benefits:
- ✅ Career-specific information
- ✅ Real institutes and platforms
- ✅ Honest salary expectations
- ✅ Relevant skills and paths
- ✅ Reality checks and challenges
- ✅ Works for all 386 careers!

### Next Steps:
1. ✅ Get FREE API key (2 minutes)
2. ✅ Add to `roadmap-viewer.html` (1 minute)
3. ✅ Test with different careers
4. ✅ Enjoy accurate roadmaps! 🎉

---

## 📞 Support

If you need help:
1. Check this guide again
2. Verify API key is correct
3. Test with different careers
4. Check browser console for errors

**The AI roadmap system is now ready to use!** 🚀

---

**Last Updated:** November 10, 2025
**Version:** 1.0 - AI-Powered Roadmaps

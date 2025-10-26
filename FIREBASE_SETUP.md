# Firebase Authentication Setup Guide

## 🔥 How to Enable Real Google Sign-In

### Step 1: Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add Project"
3. Enter project name: `career-assessment-app` (or your choice)
4. Disable Google Analytics (optional)
5. Click "Create Project"

### Step 2: Enable Authentication

1. In Firebase Console, click **Authentication** in left menu
2. Click **Get Started**
3. Click **Sign-in method** tab
4. Enable **Email/Password**:
   - Click on "Email/Password"
   - Toggle "Enable"
   - Click "Save"
5. Enable **Google**:
   - Click on "Google"
   - Toggle "Enable"
   - Enter support email (your email)
   - Click "Save"

### Step 3: Register Your Web App

1. In Firebase Console, click **Project Settings** (gear icon)
2. Scroll down to "Your apps"
3. Click **Web** icon (`</>`)
4. Enter app nickname: `Career Assessment Web`
5. Check "Also set up Firebase Hosting" (optional)
6. Click "Register app"
7. **Copy the Firebase config object** - it looks like this:

```javascript
const firebaseConfig = {
  apiKey: "AIzaSyXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
  authDomain: "your-project.firebaseapp.com",
  projectId: "your-project-id",
  storageBucket: "your-project.appspot.com",
  messagingSenderId: "123456789012",
  appId: "1:123456789012:web:abcdef123456"
};
```

### Step 4: Add Your Domain to Authorized Domains

1. In Firebase Console → **Authentication** → **Settings** tab
2. Scroll to "Authorized domains"
3. Click "Add domain"
4. Add your Cloudflare Pages domain:
   ```
   career-assessment-website.pages.dev
   ```
5. Click "Add"

### Step 5: Update Your Code

1. Open `app.html` (or `index.html`)
2. Find this section around line 1128:

```javascript
const firebaseConfig = {
    apiKey: "AIzaSyDemoKey-ReplaceWithYourActualKey",
    authDomain: "your-project.firebaseapp.com",
    projectId: "your-project-id",
    storageBucket: "your-project.appspot.com",
    messagingSenderId: "123456789",
    appId: "1:123456789:web:abcdef123456"
};
```

3. **Replace with YOUR actual Firebase config** from Step 3
4. Save the file

### Step 6: Deploy

```powershell
cd C:\Users\isht2\CascadeProjects\windsurf-project
copy app.html index.html
git add .
git commit -m "Firebase-auth-enabled"
git push origin main
```

---

## ✅ What Works Now:

### With Firebase Configured:
- ✅ **Real Google Sign-In** with popup
- ✅ **Email/Password** signup & login
- ✅ **Persistent sessions** (stays logged in)
- ✅ **Auto-login** on return visits
- ✅ **User profile** with display name

### Without Firebase (Demo Mode):
- ✅ **Fallback modal** for Google sign-in
- ✅ **Basic email signup** (no persistence)
- ✅ **Phone OTP** (simulated)

---

## 🔐 Security Notes:

1. **API Key is Safe**: Firebase API keys are safe to expose in client-side code
2. **Domain Restrictions**: Only authorized domains can use your Firebase project
3. **Security Rules**: Set up Firestore/Storage rules if you add database features

---

## 📱 Phone Authentication (Optional)

To enable Phone OTP:

1. In Firebase Console → **Authentication** → **Sign-in method**
2. Enable **Phone**
3. Add test phone numbers (for development)
4. Update the `phoneAuth()` function in code

---

## 🎯 Testing

1. Visit your deployed site
2. Click "Google" button
3. Google sign-in popup should appear
4. Sign in with your Google account
5. You'll be redirected to payment screen
6. User stays logged in on refresh!

---

## 🆘 Troubleshooting

### "auth/unauthorized-domain"
- Add your domain to Authorized domains in Firebase Console

### "Firebase not initialized"
- Check if Firebase config is correct
- Check browser console for errors
- Verify Firebase scripts are loaded

### Google Sign-In popup blocked
- Allow popups in browser
- Or use redirect method instead of popup

---

## 📚 Next Steps (Optional)

1. **Add Database**: Store test results in Firestore
2. **Email Verification**: Require email verification
3. **Password Reset**: Add forgot password feature
4. **Social Logins**: Add Facebook, Twitter, GitHub
5. **Analytics**: Track user behavior

---

**Current Status**: Firebase authentication is **ready to use** - just add your config!

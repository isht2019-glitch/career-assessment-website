# 📱 Complete Android Setup Guide for TheApp

## Step 1: Install Required Tools

### **Download Android Studio**
- Go to: https://developer.android.com/studio
- Download Android Studio (1GB+)
- Install with default settings
- Includes: Android SDK, Emulator, Gradle

## Step 2: Firebase Setup (REQUIRED)

### **Create Firebase Project**
1. Go to: https://console.firebase.google.com
2. Click **"Create a project"**
3. Name: `theapp-project` 
4. Enable Google Analytics (optional)
5. Click **"Create project"**

### **Add Android App to Firebase**
1. In Firebase Console → Click **"Add app"** → Android icon
2. **Package name**: `com.theapp` (exactly this)
3. **App nickname**: `TheApp`
4. Click **"Register app"**
5. **Download `google-services.json`**
6. **Replace** the placeholder file I created with your real one

### **Enable Authentication**
1. Firebase Console → **Authentication** → **Get started**
2. **Sign-in method** tab → Enable:
   - ✅ **Email/Password**
   - ✅ **Google** (auto-configured)
   - ✅ **Phone** (requires SMS quota)

### **Enable Firestore Database**
1. Firebase Console → **Firestore Database** → **Create database**
2. Choose **Test mode** (for development)
3. Select your region

## Step 3: Payment Provider Setup

### **Stripe (Credit Cards)**
1. Go to: https://stripe.com
2. Create account → Get **Publishable Key**
3. Replace in `PaymentActivity.kt`:
   ```kotlin
   "pk_live_your_actual_stripe_key_here"
   ```

### **Razorpay (UPI/Indian Payments)**
1. Go to: https://razorpay.com
2. Create account → Get **Key ID**
3. Replace in `PaymentActivity.kt`:
   ```kotlin
   "rzp_live_your_actual_key_here"
   ```

## Step 4: Android Studio Setup

### **Import Project**
1. Open Android Studio
2. **File** → **Open** → Select `android-app` folder
3. Wait for Gradle sync (5-10 minutes first time)

### **Create Emulator**
1. **Tools** → **AVD Manager**
2. **Create Virtual Device**
3. Choose **Pixel 6** or similar
4. Download **API 34** system image
5. Click **Finish**

### **Run the App**
1. Click green **▶️ Run** button
2. Select your emulator
3. App will install and launch

## Step 5: Testing Features

### **Authentication Testing**
- ✅ Email/Password registration
- ✅ Google Sign-In (needs Firebase config)
- ✅ Phone OTP (needs Firebase Phone Auth)

### **Payment Testing**
- ✅ Plan selection (Free, Pro, Enterprise)
- ✅ Multiple payment methods
- ✅ Subscription management

## Step 6: Real Device Testing

### **Enable Developer Mode**
1. **Settings** → **About Phone**
2. Tap **Build Number** 7 times
3. **Settings** → **Developer Options**
4. Enable **USB Debugging**

### **Connect Device**
1. Connect phone via USB
2. Allow USB debugging prompt
3. Run app from Android Studio

## 🔧 **Troubleshooting**

### **Common Issues:**
- **Gradle sync fails** → Check internet connection
- **Emulator slow** → Allocate more RAM in AVD settings
- **Google Sign-In fails** → Check SHA-1 fingerprint in Firebase
- **Build errors** → Update Android Studio and SDK

### **Get SHA-1 Fingerprint:**
```bash
cd android-app
./gradlew signingReport
```
Copy the SHA-1 and add to Firebase → Project Settings → Your apps

## 📋 **Summary**
1. **Download**: Android Studio
2. **Setup**: Firebase project + Authentication + Firestore
3. **Configure**: Payment providers (Stripe, Razorpay)
4. **Import**: Project in Android Studio
5. **Run**: On emulator or real device

**Total setup time: ~30-45 minutes**

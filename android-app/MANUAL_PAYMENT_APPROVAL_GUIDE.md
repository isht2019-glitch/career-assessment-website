# Manual Payment Approval Guide

## Overview
The app now uses a manual email-based payment approval system instead of automated payment gateways.

## How It Works

### User Flow:
1. User enters their email address in the payment section
2. User submits the email
3. App saves the email with status "pending"
4. User sees a message: "Check your email for QR code"
5. User taps "Check Approval Status" button to see if payment is approved
6. Once approved, user can start the test

### Admin Flow (Your Side):
1. User submits email through the app
2. You manually send them a QR code for payment via email
3. User makes payment using any UPI app by scanning the QR code
4. You verify the payment
5. You manually approve the user in the app

## How to Approve a User

### Method 1: Using ADB (Android Debug Bridge)
```bash
# Connect your device or emulator
adb devices

# Set approval status to "approved" for the user
adb shell am broadcast -a com.theapp.APPROVE_PAYMENT
```

### Method 2: Using SharedPreferences Editor (Easier)
1. Install a SharedPreferences editor app like "Shared Preferences Editor" from Play Store
2. Open the app and find "com.theapp"
3. Look for "theapp_prefs"
4. Change `approval_status` from "pending" to "approved"
5. User can now tap "Check Status" and access the test

### Method 3: Create an Admin Panel (Recommended for Production)
You can create a simple web admin panel or Android admin app that:
- Lists all pending email submissions
- Shows user emails and submission dates
- Has an "Approve" button for each user
- Updates the status in a shared database (Firebase, etc.)

## Current Storage Location
- **File**: SharedPreferences
- **Preference Name**: `theapp_prefs`
- **Keys**:
  - `user_email`: User's submitted email
  - `approval_status`: "pending" or "approved"
  - `submission_date`: Timestamp of submission

## For Testing
To test the approval flow without real payment:
1. User submits email
2. Manually change `approval_status` to "approved" in SharedPreferences
3. User taps "Check Approval Status"
4. App allows access to test

## Future Enhancements
- Create a backend API to manage approvals
- Implement Firebase Realtime Database for instant approval updates
- Add push notifications when payment is approved
- Create an admin dashboard for managing all users
- Store QR code images and payment receipts

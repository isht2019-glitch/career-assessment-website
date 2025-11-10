# 📱 How to Connect Your Phone via USB to Android Studio

## 🎯 Complete Step-by-Step Guide

---

## 📋 Prerequisites

Before connecting your phone:

- ✅ USB cable (data cable, not just charging cable)
- ✅ Android Studio installed
- ✅ Your Android phone
- ✅ USB drivers (usually auto-install on Windows)

---

## 🔧 Part 1: Enable Developer Options on Your Phone

### **Step 1: Find Build Number**

The location varies by manufacturer:

**Most Android Phones:**
1. Open **Settings**
2. Go to **About Phone** (or **About Device**)
3. Find **Build Number**

**Samsung:**
- Settings → About Phone → Software Information → Build Number

**Xiaomi/MIUI:**
- Settings → About Phone → MIUI Version (tap 7 times)

**OnePlus:**
- Settings → About Phone → Build Number

**Realme:**
- Settings → About Phone → Version → Build Number

### **Step 2: Tap Build Number 7 Times**

1. **Tap "Build Number" 7 times rapidly**
2. You'll see a message: "You are now a developer!"
3. Enter your PIN/password if prompted

✅ **Developer Options are now enabled!**

---

## 🔓 Part 2: Enable USB Debugging

### **Step 1: Open Developer Options**

1. Go back to main **Settings**
2. Scroll down to find **Developer Options** (or **System → Developer Options**)
3. Tap to open

### **Step 2: Enable USB Debugging**

1. Find **USB Debugging** in the list
2. Toggle it **ON**
3. A popup appears: "Allow USB debugging?"
4. Tap **OK**

✅ **USB Debugging is now enabled!**

### **Optional: Enable Install via USB**

While in Developer Options:
1. Find **Install via USB** (or **USB Installation**)
2. Toggle it **ON**
3. This allows installing APKs directly from Android Studio

---

## 🔌 Part 3: Connect Phone to Computer

### **Step 1: Connect USB Cable**

1. Plug USB cable into your phone
2. Plug other end into your computer
3. Wait a few seconds

### **Step 2: Select USB Mode**

A notification appears on your phone:

**Choose one of these:**
- **File Transfer** (MTP) - Recommended
- **PTP** (Picture Transfer)
- **USB Tethering**

**Don't choose:**
- ❌ Charging only
- ❌ No data transfer

### **Step 3: Allow USB Debugging**

A popup appears on your phone:

```
Allow USB debugging?
The computer's RSA key fingerprint is:
XX:XX:XX:XX:XX...

□ Always allow from this computer
[Cancel] [OK]
```

1. ✅ Check **"Always allow from this computer"**
2. Tap **OK**

✅ **Your phone is now connected!**

---

## 💻 Part 4: Verify Connection in Android Studio

### **Method 1: Check Device Dropdown**

1. Open Android Studio
2. Look at the top toolbar
3. Find the device dropdown (next to the Run button)
4. Your phone should appear in the list

**Example:**
```
📱 Samsung Galaxy S21 (Android 12)
```

### **Method 2: Use ADB**

1. Open **Terminal** in Android Studio (bottom)
2. Type:
   ```bash
   adb devices
   ```
3. You should see:
   ```
   List of devices attached
   ABC123456789    device
   ```

✅ **If you see "device" (not "unauthorized"), you're connected!**

---

## 🚀 Part 5: Run Your App on Phone

### **Option 1: Using Run Button**

1. Select your phone from device dropdown
2. Click the **green Run button** ▶️ (or press Shift+F10)
3. Android Studio will:
   - Build the APK
   - Install it on your phone
   - Launch the app automatically

### **Option 2: Using Debug**

1. Select your phone from device dropdown
2. Click the **Debug button** 🐛 (or press Shift+F9)
3. App runs with debugging enabled
4. You can set breakpoints and inspect code

---

## 🐛 Troubleshooting

### **Problem 1: Phone Not Showing in Android Studio**

**Solution A: Check USB Debugging**
- Make sure USB Debugging is enabled on phone
- Try disconnecting and reconnecting USB cable
- Check "Always allow" when prompted

**Solution B: Restart ADB**
1. Open Terminal in Android Studio
2. Run:
   ```bash
   adb kill-server
   adb start-server
   adb devices
   ```

**Solution C: Check USB Mode**
- Swipe down notification panel on phone
- Tap USB notification
- Change to "File Transfer" mode

**Solution D: Install USB Drivers (Windows)**
1. Open Device Manager (Win+X → Device Manager)
2. Look for your phone under "Portable Devices" or "Other Devices"
3. If yellow warning icon:
   - Right-click → Update Driver
   - Search automatically for drivers

---

### **Problem 2: "Unauthorized" Device**

**Symptoms:**
```bash
adb devices
List of devices attached
ABC123456789    unauthorized
```

**Solution:**
1. Disconnect USB cable
2. On phone: Settings → Developer Options → Revoke USB Debugging Authorizations
3. Reconnect USB cable
4. Allow USB debugging popup will appear again
5. Check "Always allow" and tap OK

---

### **Problem 3: "Device Offline"**

**Symptoms:**
```bash
adb devices
List of devices attached
ABC123456789    offline
```

**Solution:**
1. Disconnect USB cable
2. Restart phone
3. Restart computer (if needed)
4. Reconnect USB cable
5. Allow USB debugging again

---

### **Problem 4: Phone Charges But Doesn't Connect**

**Possible Causes:**
- ❌ Charging-only cable (no data transfer)
- ❌ Damaged USB cable
- ❌ Faulty USB port

**Solution:**
1. Try a different USB cable (use original cable if possible)
2. Try a different USB port on computer
3. Try USB 2.0 port instead of USB 3.0
4. Clean USB port on phone (gently)

---

### **Problem 5: Windows Doesn't Recognize Phone**

**Solution A: Install Universal ADB Drivers**
1. Download from: https://adb.clockworkmod.com/
2. Install the drivers
3. Restart computer
4. Reconnect phone

**Solution B: Install Manufacturer Drivers**
- **Samsung:** Samsung USB Driver
- **Xiaomi:** Mi PC Suite
- **OnePlus:** OnePlus USB Drivers
- **Google Pixel:** Usually works automatically

---

### **Problem 6: "Install Failed" Error**

**Solution:**
1. Uninstall existing app from phone manually
2. Try again
3. Or in Android Studio:
   - Run → Clean Project
   - Run → Rebuild Project
   - Try installing again

---

## 💡 Pro Tips

### **Tip 1: Keep Screen On While Developing**

In Developer Options:
- Enable **"Stay Awake"** - Screen stays on while charging
- Useful when testing

### **Tip 2: Show Taps**

In Developer Options:
- Enable **"Show Taps"** - Visual feedback when you tap
- Enable **"Pointer Location"** - Shows touch coordinates
- Useful for debugging UI

### **Tip 3: Faster Deployment**

In Developer Options:
- Disable **"Verify apps over USB"** - Faster installation
- Enable **"Install via USB"** - Direct installation

### **Tip 4: Wireless Debugging (Android 11+)**

1. Connect phone via USB first
2. Enable **"Wireless Debugging"** in Developer Options
3. In Terminal:
   ```bash
   adb tcpip 5555
   adb connect PHONE_IP:5555
   ```
4. Now you can disconnect USB cable!

### **Tip 5: Multiple Devices**

If you have multiple phones connected:
```bash
# List all devices
adb devices

# Install on specific device
adb -s DEVICE_ID install app.apk

# Or select in Android Studio dropdown
```

---

## 📱 Device-Specific Notes

### **Samsung Phones**

- May need **Samsung USB Driver** for Windows
- **Smart Switch** software includes drivers
- Some Samsung phones need **"USB Configuration"** set to MTP

### **Xiaomi/MIUI Phones**

- May need **Mi USB Driver**
- Enable **"Install via USB"** in Developer Options
- Some MIUI versions require **"USB Debugging (Security Settings)"** enabled

### **Huawei Phones**

- May need **HiSuite** software for drivers
- Enable **"Allow ADB debugging in charge only mode"**

### **OnePlus Phones**

- Usually works out of the box
- May need **OnePlus USB Drivers** on older Windows versions

---

## ✅ Quick Checklist

Before running your app:

- [ ] Developer Options enabled on phone
- [ ] USB Debugging enabled
- [ ] USB cable connected (data cable, not charging-only)
- [ ] USB mode set to "File Transfer" or "PTP"
- [ ] USB debugging authorization allowed
- [ ] Phone appears in Android Studio device dropdown
- [ ] `adb devices` shows device as "device" (not "unauthorized")

---

## 🎯 Summary

**To connect your phone to Android Studio:**

1. ✅ **Enable Developer Options** - Tap Build Number 7 times
2. ✅ **Enable USB Debugging** - In Developer Options
3. ✅ **Connect USB cable** - Use data cable
4. ✅ **Select File Transfer mode** - On phone
5. ✅ **Allow USB debugging** - Check "Always allow"
6. ✅ **Select phone in Android Studio** - Device dropdown
7. ✅ **Click Run** - App installs and launches

---

## 🚀 Run Your App Now!

1. Open your project in Android Studio
2. Make sure phone is connected and showing in device dropdown
3. Click the green **Run** button ▶️
4. Wait for build and installation
5. App launches on your phone!

**Your phone is now ready for development!** 🎉

---

## 📞 Still Having Issues?

### **Check These:**

1. ✅ USB Debugging enabled
2. ✅ Correct USB mode (File Transfer)
3. ✅ USB debugging authorized
4. ✅ Using data cable (not charging-only)
5. ✅ Tried different USB port
6. ✅ Restarted ADB server
7. ✅ Phone unlocked when connecting

### **Advanced Troubleshooting:**

```bash
# Kill and restart ADB
adb kill-server
adb start-server

# Check device status
adb devices -l

# Check ADB version
adb version

# Reconnect device
adb disconnect
adb connect
```

---

**Created:** November 5, 2025  
**For:** Android Studio Development  
**Status:** Complete USB Debugging Guide

# 🔧 Failed to Connect to Port 3001 - Troubleshooting Guide

## 🐛 The Problem

**Error:** `Failed to connect to 192.168.1.4:3001`

**What it means:**
- Your Android app can't reach the backend server
- Server might not be running
- Wrong IP address
- Firewall blocking connection
- Not on same network

---

## ✅ Quick Fixes (Try These First)

### **Fix 1: Make Sure Server is Running**

**Check if server is running:**

1. Open Command Prompt/PowerShell
2. Navigate to project folder:
   ```bash
   cd e:\CascadeProjects\windsurf-project
   ```
3. Start the server:
   ```bash
   node custom-admin-server.js
   ```

**You should see:**
```
🚀 CUSTOM ADMIN SERVER STARTED
📍 Server running at http://0.0.0.0:3001
🔓 Admin Login: admin / YAMHAIHAM
```

**If server is NOT running, start it now!** ✅

---

### **Fix 2: Find Your Computer's Correct IP**

Your computer's IP might have changed!

**On Windows:**
```bash
ipconfig
```

Look for **"IPv4 Address"** under your WiFi adapter:
```
Wireless LAN adapter Wi-Fi:
   IPv4 Address. . . . . . . . . . . : 192.168.1.X
```

**Common IPs:**
- `192.168.1.X` (most home routers)
- `192.168.0.X` (some routers)
- `10.0.0.X` (some networks)

**Update ApiManager.kt with the correct IP!**

---

### **Fix 3: Update ApiManager.kt**

**File:** `app/src/main/java/com/theapp/ApiManager.kt` (line 18)

**Change:**
```kotlin
private const val BASE_URL = "http://192.168.1.4:3001/api"
```

**To your actual IP:**
```kotlin
private const val BASE_URL = "http://192.168.1.X:3001/api"
```

**Then rebuild the APK!**

---

### **Fix 4: Check Same WiFi Network**

**Both devices must be on the SAME WiFi:**

1. **Computer:** Check WiFi name in system tray
2. **Phone:** Settings → WiFi → Check connected network
3. **Must match!** Same network name

**If different networks:**
- Connect both to the same WiFi
- Restart server after connecting

---

### **Fix 5: Test Connection from Phone**

**Open browser on your phone:**

1. Open Chrome/Safari on phone
2. Type in address bar:
   ```
   http://192.168.1.4:3001
   ```
3. You should see: "Custom Admin Server is running"

**If it works in browser:**
- ✅ Server is reachable
- ✅ Network is OK
- ❌ App needs to be rebuilt with correct IP

**If it doesn't work in browser:**
- ❌ Server not running OR
- ❌ Wrong IP address OR
- ❌ Firewall blocking

---

## 🔥 Firewall Fix (Windows)

Windows Firewall might be blocking port 3001.

### **Allow Node.js Through Firewall:**

1. **Open Windows Defender Firewall**
   - Press Win+R
   - Type: `firewall.cpl`
   - Press Enter

2. **Click "Allow an app or feature through Windows Defender Firewall"**

3. **Click "Change settings"** (needs admin)

4. **Click "Allow another app..."**

5. **Browse and select:**
   ```
   C:\Program Files\nodejs\node.exe
   ```

6. **Check both boxes:**
   - ✅ Private networks
   - ✅ Public networks

7. **Click "Add"** then "OK"

8. **Restart your server**

---

## 🔍 Detailed Troubleshooting

### **Step 1: Verify Server is Running**

**In Command Prompt:**
```bash
cd e:\CascadeProjects\windsurf-project
node custom-admin-server.js
```

**Expected output:**
```
🚀 CUSTOM ADMIN SERVER STARTED
📍 Server running at http://0.0.0.0:3001
🔐 Admin Login: admin / YAMHAIHAM
📊 Database folder: custom-db/
✅ All systems ready!
```

**If you see errors:**
- Check if port 3001 is already in use
- Try a different port (change in server file)
- Check Node.js is installed: `node --version`

---

### **Step 2: Find Correct IP Address**

**Run this command:**
```bash
ipconfig
```

**Look for your WiFi adapter:**
```
Wireless LAN adapter Wi-Fi:

   Connection-specific DNS Suffix  . :
   Link-local IPv6 Address . . . . . : fe80::xxxx
   IPv4 Address. . . . . . . . . . . : 192.168.1.X  ← THIS ONE!
   Subnet Mask . . . . . . . . . . . : 255.255.255.0
   Default Gateway . . . . . . . . . : 192.168.1.1
```

**Copy the IPv4 Address (e.g., 192.168.1.5)**

---

### **Step 3: Update ApiManager.kt**

**File location:**
```
android-app/app/src/main/java/com/theapp/ApiManager.kt
```

**Line 18:**
```kotlin
private const val BASE_URL = "http://YOUR_IP_HERE:3001/api"
```

**Example:**
```kotlin
private const val BASE_URL = "http://192.168.1.5:3001/api"
```

**Save the file!**

---

### **Step 4: Rebuild APK**

**In Android Studio:**

1. **Build → Clean Project**
2. **Build → Rebuild Project**
3. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
4. **Wait for build to complete**
5. **Install new APK on phone**

**Important:** You MUST rebuild after changing the IP!

---

### **Step 5: Test from Phone Browser**

**Before testing the app:**

1. Open **Chrome** on your phone
2. Type: `http://YOUR_IP:3001`
3. Should see: "Custom Admin Server is running"

**If this works:**
- ✅ Server is reachable
- ✅ Network is configured correctly
- ✅ App should work after rebuild

**If this doesn't work:**
- ❌ Check server is running
- ❌ Check IP address is correct
- ❌ Check firewall settings
- ❌ Check both on same WiFi

---

### **Step 6: Test from Computer Browser**

**On your computer:**

1. Open browser
2. Go to: `http://localhost:3001`
3. Should see: "Custom Admin Server is running"

**If this works but phone doesn't:**
- ❌ Firewall is blocking external connections
- ❌ Server bound to localhost only (should be 0.0.0.0)

---

## 🔧 Advanced Troubleshooting

### **Check if Port 3001 is Open**

**On your computer:**
```bash
netstat -an | findstr :3001
```

**Should see:**
```
TCP    0.0.0.0:3001           0.0.0.0:0              LISTENING
```

**If you see `127.0.0.1:3001` instead of `0.0.0.0:3001`:**
- Server is only listening on localhost
- Won't accept external connections
- Check server code (should bind to 0.0.0.0)

---

### **Check Server Binding**

**In `custom-admin-server.js`:**

Look for this line:
```javascript
app.listen(PORT, '0.0.0.0', () => {
```

**Make sure it says `0.0.0.0` NOT `localhost` or `127.0.0.1`**

If it says `localhost`, change to `0.0.0.0` and restart server.

---

### **Ping Test**

**From your phone (using terminal app):**
```bash
ping 192.168.1.4
```

**Should see:**
```
PING 192.168.1.4: 56 data bytes
64 bytes from 192.168.1.4: icmp_seq=0 ttl=64 time=5.123 ms
```

**If ping fails:**
- ❌ Not on same network
- ❌ Firewall blocking ICMP
- ❌ Wrong IP address

---

### **Test with cURL (from phone terminal)**

**If you have a terminal app on phone:**
```bash
curl http://192.168.1.4:3001
```

**Should return:**
```
Custom Admin Server is running
```

---

## 🎯 Common Scenarios

### **Scenario 1: Server Not Running**

**Symptoms:**
- Connection refused
- Can't connect in browser
- Timeout error

**Solution:**
```bash
cd e:\CascadeProjects\windsurf-project
node custom-admin-server.js
```

Keep this terminal window open!

---

### **Scenario 2: Wrong IP Address**

**Symptoms:**
- Works on computer browser
- Doesn't work on phone
- Timeout on phone

**Solution:**
1. Run `ipconfig`
2. Find correct IPv4 address
3. Update `ApiManager.kt`
4. Rebuild APK

---

### **Scenario 3: Firewall Blocking**

**Symptoms:**
- Server running
- Correct IP
- Still can't connect from phone
- Works on localhost

**Solution:**
1. Allow Node.js through Windows Firewall
2. Or temporarily disable firewall to test
3. Restart server after firewall change

---

### **Scenario 4: Different Networks**

**Symptoms:**
- Everything seems correct
- Still can't connect
- Ping fails

**Solution:**
1. Check WiFi name on both devices
2. Connect both to same network
3. Get new IP with `ipconfig`
4. Update app and rebuild

---

### **Scenario 5: IP Changed**

**Symptoms:**
- Worked before
- Stopped working suddenly
- Router assigned new IP

**Solution:**
1. Run `ipconfig` to get new IP
2. Update `ApiManager.kt`
3. Rebuild and reinstall APK

---

## 📱 Quick Test Checklist

Before running the app:

- [ ] Server is running (see startup message)
- [ ] Found correct IP with `ipconfig`
- [ ] Updated `ApiManager.kt` with correct IP
- [ ] Rebuilt APK in Android Studio
- [ ] Installed new APK on phone
- [ ] Both devices on same WiFi network
- [ ] Can access `http://YOUR_IP:3001` in phone browser
- [ ] Firewall allows Node.js
- [ ] Port 3001 is listening on 0.0.0.0

---

## 💡 Pro Tips

### **Tip 1: Keep Server Running**

Don't close the terminal window where server is running!

### **Tip 2: Use Static IP**

Set a static IP for your computer in router settings so it doesn't change.

### **Tip 3: Check Logs**

Watch server terminal for connection attempts:
```
📥 POST /api/payments/submit - 192.168.1.X
```

If you don't see this when app tries to connect, request isn't reaching server.

### **Tip 4: Test Endpoints**

Use Postman or browser to test endpoints:
```
http://192.168.1.4:3001/api/admin/stats
```

### **Tip 5: Restart Everything**

When in doubt:
1. Stop server (Ctrl+C)
2. Close Android Studio
3. Restart server
4. Reopen Android Studio
5. Rebuild APK

---

## 🚀 Step-by-Step Fix Guide

**Follow these steps in order:**

### **1. Start Server**
```bash
cd e:\CascadeProjects\windsurf-project
node custom-admin-server.js
```
✅ See "Server running" message

### **2. Get IP Address**
```bash
ipconfig
```
✅ Note down IPv4 address (e.g., 192.168.1.5)

### **3. Test in Computer Browser**
```
http://localhost:3001
```
✅ Should see "Custom Admin Server is running"

### **4. Test in Phone Browser**
```
http://192.168.1.5:3001
```
✅ Should see "Custom Admin Server is running"

**If Step 4 fails:**
- Check firewall
- Check same WiFi
- Check IP is correct

### **5. Update ApiManager.kt**
```kotlin
private const val BASE_URL = "http://192.168.1.5:3001/api"
```
✅ Use your actual IP from Step 2

### **6. Rebuild APK**
- Build → Clean Project
- Build → Rebuild Project
- Build → Build APK
✅ Wait for "BUILD SUCCESSFUL"

### **7. Install on Phone**
- Transfer APK to phone
- Install it
✅ Allow installation from unknown sources

### **8. Test App**
- Open app
- Try to submit payment
✅ Should connect successfully!

---

## 📊 Connection Flow

```
Phone App (192.168.1.X)
    ↓
    HTTP Request to http://192.168.1.4:3001/api
    ↓
WiFi Router (192.168.1.1)
    ↓
Computer (192.168.1.4)
    ↓
Node.js Server (Port 3001)
    ↓
Response back to Phone
```

**Any break in this chain = connection fails!**

---

## 🎯 Summary

**Most common causes:**

1. ❌ Server not running → Start it!
2. ❌ Wrong IP in app → Update and rebuild!
3. ❌ Different WiFi networks → Connect to same!
4. ❌ Firewall blocking → Allow Node.js!
5. ❌ Didn't rebuild APK → Rebuild after changes!

**Fix checklist:**

✅ Server running  
✅ Correct IP in ApiManager.kt  
✅ APK rebuilt  
✅ Same WiFi network  
✅ Firewall allows connection  
✅ Can access in phone browser  

**If all checked, app should connect!** 🚀

---

**Created:** November 5, 2025  
**Status:** Complete troubleshooting guide  
**Next:** Follow step-by-step fix guide above

# Android Local Properties Fix

## Issue
JDK image transform error occurs when building with compileSdk 33 due to Android Studio's JBR 21 jlink.exe incompatibility.

## Solution
Add JDK 11 override to `local.properties` file in the `android-app` directory.

## Steps

1. **Copy the content below to your `android-app/local.properties` file:**

```properties
## This file must *NOT* be checked into Version Control Systems,
# as it contains information specific to your local configuration.
#
# Location of the SDK. This is only used by Gradle.
# For customization when using a Version Control System, please read the
# header note.
#sdk.dir=C\\:\\Users\\USERNAME\\AppData\\Local\\Android\\Sdk

# JDK 11 Override to fix JdkImageTransform errors
# This forces Gradle to use JDK 11 instead of Android Studio's JBR 21
org.gradle.java.home=C\\:\\Program Files\\Eclipse Adoptium\\jdk-11.0.28.6-hotspot
```

2. **Save the file**

3. **Restart Android Studio completely** (File → Exit, then reopen)

4. **Clean Project** → **Build APK**

## Why This Works
- Forces Gradle to use JDK 11 instead of Android Studio's problematic JBR 21
- Bypasses the jlink.exe compatibility issues
- Maintains compileSdk 33 for dependency compatibility
- Keeps targetSdk 31 to avoid runtime behavior changes

## Build Success Expected
After these changes, the build should complete successfully and generate APK at:
`app/build/outputs/apk/debug/app-debug.apk`

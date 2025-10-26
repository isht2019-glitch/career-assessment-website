@echo off
echo Starting Android build process...

REM Clean previous builds
if exist "app\build" (
    echo Cleaning previous build...
    rmdir /s /q "app\build"
)

if exist ".gradle" (
    echo Cleaning gradle cache...
    rmdir /s /q ".gradle"
)

REM Set JAVA_HOME if not set
if not defined JAVA_HOME (
)

call gradlew assembleDebug
if %ERRORLEVEL% neq 0 (
    echo Gradle wrapper build failed, trying alternative methods...
    goto method2
) else (
    echo Build successful! APK location:
    echo app\build\outputs\apk\debug\app-debug.apk
    goto end
)

:method2
echo.
echo Method 2: Android Studio Build
echo ==============================
echo Please build manually in Android Studio:
echo 1. Restart Android Studio completely
echo 2. File -^> Clean Project
echo 3. Build -^> Build APK(s)
echo.
echo Configuration applied:
echo - compileSdk: 31 (avoids JDK image transform)
echo - targetSdk: 31
echo - JDK 11 override in local.properties
echo - Multiple JDK transform disable flags
echo.

:end
pause

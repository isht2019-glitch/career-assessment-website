# Troubleshooting Guide - Career Assessment App

## Current App Flow

1. **Sign In** (AuthActivity)
2. **Payment Screen** (PaymentActivity) - Velly Bandaar explains ₹1 payment
3. **Email Input** - User enters email
4. **Test Begins** (TestActivity):
   - Phase 0: Questions 1-10 → Button: "See Analysis #1 🎯"
   - Phase 1: Analysis #1 → Button: "Continue to Questions 11-20 ➡️"
   - Phase 2: Questions 11-20 → Button: "See Analysis #2 🎯"
   - Phase 3: Analysis #2 → Button: "Continue to Questions 21-30 ➡️"
   - Phase 4: Questions 21-30 → Button: "See Complete Analysis 🎯"
   - Phase 5: Analysis #3 → Button: "Start Aptitude Test ➡️"
   - Phase 6: Aptitude Q1-20 → Button: "Submit Test 🚀"
   - Phase 7: Final Results

## Common Issues & Solutions

### Issue 1: App Crashes After Payment
**Solution**: Check if PaymentActivity has UserManager imported
```kotlin
import com.theapp.UserManager
```

### Issue 2: Questions Not Showing
**Cause**: getCurrentPhaseQuestions() returns empty list
**Check**: Phase numbers must be 0, 2, 4, or 6 for questions
**Solution**: Verify currentPhase variable

### Issue 3: Analysis Screen Not Appearing
**Cause**: PersonalityGuideSystem methods not being called
**Check Logcat for**:
- "Showing analysis after first 10"
- "First analysis (Q1-10). RIASEC: ..."

### Issue 4: Can't Proceed After Analysis
**Cause**: Continue button not working
**Solution**: Check if hideGuide() and onComplete() are called

### Issue 5: Submit Button Not Enabling
**Cause**: Not all questions answered
**Solution**: 
- Check phaseAnswers.count { it != null } == totalQuestions
- Ensure all radio buttons are answered

### Issue 6: Timer Not Working
**Cause**: Timer initialization issue
**Solution**: Check startTimer() is called in onCreate

### Issue 7: Personality Analysis Shows Wrong Data
**Cause**: calculatePartialScores() range issue
**Solution**:
- Analysis #1: calculatePartialScores(0, 10)
- Analysis #2: calculatePartialScores(0, 20)
- Analysis #3: calculatePartialScores(0, 30)

## How to Debug

### Enable Logging
Check Android Studio Logcat for these tags:
- `TestActivity` - Phase transitions and button clicks
- `PersonalityGuide` - Analysis display

### Key Log Messages to Look For
```
TestActivity: Starting Phase: 0
TestActivity: Submit button clicked. Phase: 0, Answered: 10
TestActivity: Handling submission for phase 0: 10/10 answered
TestActivity: Showing analysis after first 10
PersonalityGuide: Showing analysis #1 (Q1-10). Scores: ...
PersonalityGuide: Proceeding to Q11-20
```

### Check Phase Progression
1. Phase 0 → Phase 1 (after 10 questions)
2. Phase 1 → Phase 2 (after analysis #1)
3. Phase 2 → Phase 3 (after 10 questions)
4. Phase 3 → Phase 4 (after analysis #2)
5. Phase 4 → Phase 5 (after 10 questions)
6. Phase 5 → Phase 6 (after analysis #3)
7. Phase 6 → Phase 7 (after 20 aptitude questions)

## Required Files
- ✅ AuthActivity.kt
- ✅ MainActivity.kt
- ✅ PaymentActivity.kt
- ✅ PaymentGuideSystem.kt
- ✅ TestActivity.kt
- ✅ PersonalityGuideSystem.kt
- ✅ UserManager.kt

## Quick Checks
1. All files compiled without errors?
2. UserManager import present in all activities?
3. PersonalityGuideSystem has showFirstAnalysis(), showSecondAnalysis(), showCompletePersonalityAnalysis()?
4. TestActivity has showFirstAnalysis(), showSecondAnalysis(), showCompleteAnalysis()?

## Please Provide:
To help you better, please provide:
1. **Exact error message** (if any)
2. **Where the problem occurs** (which screen/phase)
3. **Logcat output** (if app crashes)
4. **What you expect** vs **what happens**

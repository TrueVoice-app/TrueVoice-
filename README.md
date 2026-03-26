# TrueVoice — Android App Setup Guide
## Competition MVP Demo | Jetpack Compose + Dark UI

---

## 📁 Project Structure

```
TrueVoice/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/truevoice/app/
│   │       │   ├── MainActivity.kt          ← ALL app logic + UI here
│   │       │   └── ui/theme/
│   │       │       └── Theme.kt             ← Dark color theme
│   │       ├── res/
│   │       │   └── values/
│   │       │       ├── themes.xml           ← Android XML theme
│   │       │       └── strings.xml          ← App name
│   │       └── AndroidManifest.xml          ← App config
│   ├── build.gradle                         ← App dependencies
│   └── proguard-rules.pro
├── build.gradle                             ← Root build file
├── settings.gradle                          ← Project settings
├── gradle.properties                        ← Gradle config
└── gradle/wrapper/
    └── gradle-wrapper.properties            ← Gradle version
```

---

## 🚀 How to Open in Android Studio

### Step 1: Install Android Studio
- Download from: https://developer.android.com/studio
- Install with default settings (includes Android SDK)

### Step 2: Open the Project
1. Open Android Studio
2. Click **"Open"** (not "New Project")
3. Navigate to and select the **`TrueVoice/`** folder
4. Click **OK**

### Step 3: Wait for Gradle Sync
- Android Studio will automatically sync dependencies
- This may take 2–5 minutes on first run (downloads libraries)
- You'll see "Gradle sync finished" at the bottom when done

### Step 4: Run the App
**Option A — Physical Android Phone:**
1. Enable Developer Options on your phone:
   - Go to Settings → About Phone → tap "Build Number" 7 times
2. Enable USB Debugging in Developer Options
3. Connect phone via USB → allow debugging when prompted
4. Click the ▶ green Run button in Android Studio

**Option B — Emulator:**
1. Go to Tools → Device Manager
2. Click "+ Create Device" → choose Pixel 6 → API 33
3. Download the system image if prompted
4. Click ▶ Run

---

## 🎮 App Flow

```
[IDLE SCREEN]
  Stats cards, How-it-works guide, Simulate button
        ↓ tap button
[RINGING SCREEN]  ~1.8 seconds
  Ripple animation, incoming call visual
        ↓ auto-transition
[ANALYZING SCREEN]  ~2.2 seconds  
  AI brain animation, 6 live checklist items, progress bar
        ↓ auto-transition
[RESULT SCREEN]
  Verdict banner, caller info, risk gauge, detection reasons
  Action buttons: Block / Report / Safe / Analyze Another
```

---

## ✨ Features Implemented

| Feature | Details |
|---------|---------|
| **4-Stage Flow** | Idle → Ringing → Analyzing → Result |
| **Risk Score** | Random 62–95%, animated gauge + circular arc |
| **Fake Callers** | 5 realistic contacts (Mom, Bank, Doctor, etc.) |
| **8 Risk Signals** | VoIP relay, voice mismatch, STIR/SHAKEN, carrier mismatch, etc. |
| **Severity Tags** | HIGH / MED / LOW color-coded per signal |
| **Animations** | Ripple rings, rotating AI arcs, staggered card reveals, gauge fill |
| **Ambient BG** | Breathing glow effect — blue + red gradient |
| **Action Buttons** | Block, Report Spoof, Mark Safe |
| **Stats Cards** | $1.03B lost, 68% victim rate, <500ms detection |
| **Privacy Tags** | Zero Upload, Real-Time, On-Device pills |

---

## 🎨 Design System

| Element | Value |
|---------|-------|
| Background | #050A14 (deep space black) |
| Surface | #080E1C |
| Primary Blue | #00C8FF (electric cyan) |
| Accent Purple | #7B61FF |
| Danger Red | #FF3B5C |
| Warning Gold | #FFB800 |
| Safe Green | #00FF87 |
| Text Primary | #CCDFFF |
| Text Muted | #4A6080 |

---

## ⚡ Troubleshooting

**"Cannot resolve symbol" errors:**
→ File → Invalidate Caches → Restart

**Gradle sync fails:**
→ Check internet connection (needs to download libraries first time)
→ Try File → Sync Project with Gradle Files

**App crashes on launch:**
→ Make sure minSdk is 26 in build.gradle
→ Check that AndroidManifest.xml has the correct activity name

**Emulator too slow:**
→ Enable Hardware Acceleration in BIOS
→ Use a physical phone instead

---

## 🏆 Competition Tips

- Demo with a dark room / bright screen for maximum visual impact
- Show all 4 stages of the flow — each has unique animations
- Highlight: "100% on-device, zero data leaves the phone"
- The risk reasons refresh with new random content each simulation
- Multiple caller identities (Mom, Bank, Doctor) add realism

Good luck! 🎉

# 🔧 Installation & Setup Guide

## Inhaltsverzeichnis
1. [Voraussetzungen](#voraussetzungen)
2. [Android Studio Setup](#android-studio-setup)
3. [Projekt importieren](#projekt-importieren)
4. [Server konfigurieren](#server-konfigurieren)
5. [App bauen & installieren](#app-bauen--installieren)
6. [Troubleshooting](#troubleshooting)

---

## 1. Voraussetzungen

### Software-Anforderungen
- **Android Studio** Hedgehog (2023.1.1) oder neuer
  - Download: https://developer.android.com/studio
- **JDK 17** (wird mit Android Studio installiert)
- **Android SDK** mit folgenden Komponenten:
  - Android SDK Platform 35
  - Android SDK Build-Tools 34.0.0
  - Android Emulator (optional, für Tests)

### Hardware-Anforderungen
- **Minimum:**
  - 8 GB RAM
  - 4 GB freier Speicherplatz
  - Intel i5 oder äquivalent
- **Empfohlen:**
  - 16 GB RAM
  - 8 GB freier Speicherplatz
  - Intel i7 oder äquivalent

### Backend-Server
- Der **bike-server** muss laufen und erreichbar sein
- Standard-Port: `5000`

---

## 2. Android Studio Setup

### Installation

#### Windows
1. [Android Studio herunterladen](https://developer.android.com/studio)
2. Installer ausführen
3. Setup-Wizard folgen
4. Standard-Einstellungen akzeptieren

#### macOS
```bash
brew install --cask android-studio
```

#### Linux (Ubuntu/Debian)
```bash
sudo snap install android-studio --classic
```

### Erste Schritte
1. Android Studio starten
2. "More Actions" → "SDK Manager"
3. "SDK Platforms" Tab:
   - ☑️ Android 14.0 (API 35)
   - ☑️ Android 8.0 (API 26)
4. "SDK Tools" Tab:
   - ☑️ Android SDK Build-Tools 34.0.0
   - ☑️ Android Emulator
   - ☑️ Android SDK Platform-Tools
5. "Apply" klicken und warten

---

## 3. Projekt importieren

### Option A: Aus ZIP
```bash
# Entpacken
unzip radstation-android-app.zip
cd radstation-android-app
```

### Option B: Git Clone
```bash
git clone https://github.com/radstation/android-app.git
cd android-app
```

### In Android Studio öffnen
1. Android Studio starten
2. "Open" → Projekt-Ordner wählen
3. Warten auf Gradle Sync (kann 5-10 Minuten dauern)
4. Falls Fehler auftreten: "File" → "Invalidate Caches / Restart"

---

## 4. Server konfigurieren

### IP-Adresse des Servers herausfinden

#### Server auf lokalem Rechner (Entwicklung)
```bash
# Windows
ipconfig

# macOS/Linux
ifconfig
# oder
ip addr show
```

Beispiel-Ausgabe:
```
192.168.1.100  # <-- Diese IP verwenden!
```

### Server-URL in App eintragen

Öffne `app/build.gradle.kts` und ändere:

```kotlin
buildTypes {
    release {
        // Für echte Geräte: Deine Server-IP verwenden
        buildConfigField("String", "API_BASE_URL", "\"http://192.168.1.100:5000\"")
    }
    debug {
        // Für Emulator: 10.0.2.2 verwenden (zeigt auf localhost des Host-Rechners)
        buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:5000\"")
    }
}
```

**Wichtig:**
- Für **Emulator**: `http://10.0.2.2:5000`
- Für **physisches Gerät**: `http://192.168.1.XXX:5000` (deine Server-IP)

### Gradle Sync durchführen
Nach Änderung: "Sync Now" klicken (oben rechts in Android Studio)

---

## 5. App bauen & installieren

### Methode 1: Über Android Studio (Empfohlen)

#### Auf Emulator
1. "Device Manager" öffnen (Seitenleiste)
2. Emulator erstellen:
   - "Create Device"
   - "Pixel 7" wählen
   - System Image: "API 35" (Android 14)
   - "Finish"
3. Emulator starten (Play-Button)
4. Warten bis Emulator geladen ist
5. "Run" → "Run 'app'" (grünes Play-Symbol)

#### Auf echtem Gerät
1. **USB-Debugging aktivieren:**
   - Einstellungen → Über das Telefon
   - 7x auf "Build-Nummer" tippen
   - Zurück → Entwickleroptionen
   - "USB-Debugging" aktivieren

2. **Gerät verbinden:**
   - USB-Kabel anschließen
   - Am Gerät: "USB-Debugging erlauben" bestätigen

3. **App installieren:**
   - Gerät in Android Studio auswählen (oben Mitte)
   - "Run" → "Run 'app'"

### Methode 2: APK erstellen

#### Debug-APK (für Tests)
```bash
./gradlew assembleDebug
```
APK-Pfad: `app/build/outputs/apk/debug/app-debug.apk`

#### Release-APK (für Produktion)
```bash
./gradlew assembleRelease
```

**APK auf Gerät installieren:**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 6. Troubleshooting

### Problem: "Gradle Sync Failed"

**Lösung:**
```bash
# Terminal in Android Studio
./gradlew clean
./gradlew build --refresh-dependencies
```

### Problem: "SDK not found"

**Lösung:**
1. File → Project Structure → SDK Location
2. Pfad prüfen (z.B. `/Users/username/Library/Android/sdk`)
3. Falls leer: "Download" klicken

### Problem: "Unable to connect to server"

**Checklist:**
- [ ] Server läuft? (`python3 app.py` im bike-server Ordner)
- [ ] Firewall erlaubt Port 5000?
- [ ] Richtige IP-Adresse in `build.gradle.kts`?
- [ ] Gerät im selben WLAN wie Server?

**Test:**
```bash
# Im Terminal
curl http://192.168.1.100:5000/api/auftraege
# Sollte JSON zurückgeben
```

### Problem: "Permission denied: Camera"

**Lösung:**
- App deinstallieren
- Neu installieren
- Bei Kamera-Nutzung: "Erlauben" klicken

### Problem: "Build failed: Out of memory"

**Lösung:**
In `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m
```

### Problem: Emulator startet nicht

**Lösung:**
1. "Device Manager" → Gerät löschen
2. Neues Gerät erstellen (kleineres Modell wählen)
3. Hardware Acceleration aktivieren:
   - Windows: Intel HAXM installieren
   - macOS: Automatisch via Hypervisor Framework
   - Linux: KVM aktivieren

---

## 🎉 Erfolgreiche Installation

Wenn alles funktioniert:
1. App startet
2. Dashboard wird angezeigt
3. "Neuer Auftrag" funktioniert
4. Kamera kann geöffnet werden

**Nächster Schritt:** [QUICKSTART.md](QUICKSTART.md) lesen!

---

## 📞 Hilfe benötigt?

- GitHub Issues: [Link zu Issues]
- E-Mail: support@radstation.de
- Dokumentation: [Link zur Doku]

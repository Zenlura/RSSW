# 🚴 Radstation Werkstatt - Android App

**Eine einfache, funktionale App für die Werkstatt-Verwaltung.**

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Platform](https://img.shields.io/badge/platform-Android-green)
![License](https://img.shields.io/badge/license-MIT-orange)

---

## 📱 Über die App

Die **Radstation Werkstatt App** ist eine **minimalistische, aber leistungsstarke** Android-Anwendung zur Verwaltung von Fahrradreparaturen. Sie wurde entwickelt mit dem Fokus auf:

- ✅ **Einfachheit** - Keine überladenen UIs
- ✅ **Funktionalität** - Alles was man braucht, nichts was man nicht braucht
- ✅ **Geschwindigkeit** - Schnell Aufträge erstellen und verwalten
- ✅ **Kamera-Integration** - Fotos von Fahrrädern direkt aufnehmen

### Design-Philosophie

❌ **NICHT wie die überladene Laufrad-App!**  
✅ Einfach, klar, "fahrbereit"

Das Design basiert auf dem Farbschema der Laufrad-App (Cyan/Orange), aber mit einem **minimalistischen Ansatz**:
- Keine überflüssigen Animationen
- Keine schwebenden Elemente ohne Funktion
- Klare Hierarchie und Navigation
- Material 3 Design System

---

## 🎯 Hauptfunktionen

### 1. **Dashboard** 📊
- Schnellübersicht über alle Aufträge
- Status-Statistiken (Nicht begonnen, In Bearbeitung, Fertig)
- Direktzugriff auf wichtigste Funktionen
- Quick Actions für Aufträge und Teile

### 2. **Neuer Auftrag** ➕
- Kamera-Integration für Fahrradfotos
- Automatische Auftragsnummern-Generierung
- Schlüsselnummer, Fahrradmarke, Mängelbeschreibung
- Einfaches Upload zum Server

### 3. **Auftrags-Verwaltung** 🔧
- Liste aller Aufträge
- Filter nach Status (Nicht begonnen, In Bearbeitung, Fertig)
- Auftrags-Details mit allen Informationen
- Status-Änderung mit einem Klick
- Flags: Angerufen, Mailbox, Bezahlt

### 4. **Teile-Verwaltung** 🛠️ *(Coming Soon)*
- Teile-Datenbank durchsuchen
- Bestand prüfen
- Verbrauch buchen

---

## 🏗️ Technologie-Stack

### Core
- **Kotlin** 1.9+
- **Jetpack Compose** - Moderne UI
- **Material 3 Design** - Google's neuestes Design-System

### Architektur
- **MVVM** (Model-View-ViewModel)
- **Clean Architecture** (Domain, Data, Presentation)
- **Hilt** - Dependency Injection
- **Kotlin Coroutines + Flow** - Asynchrone Programmierung

### Netzwerk & Daten
- **Retrofit 2** - REST API Client
- **Gson** - JSON Serialisierung
- **OkHttp** - HTTP Client mit Logging

### Kamera & Bilder
- **CameraX** - Moderne Kamera-API
- **Coil** - Bildladung und -darstellung
- **Accompanist Permissions** - Permission-Handling

### Navigation
- **Navigation Compose** - Type-safe Navigation

---

## 📦 Installation & Setup

### Voraussetzungen
- Android Studio Hedgehog (2023.1.1) oder neuer
- JDK 17
- Android SDK 26+ (Android 8.0 Oreo)
- Gradle 8.2+

### Server konfigurieren

Die App benötigt den **bike-server** Backend. Die Server-URL wird in `build.gradle.kts` konfiguriert:

```kotlin
buildTypes {
    release {
        buildConfigField("String", "API_BASE_URL", "\"http://192.168.1.100:5000\"")
    }
    debug {
        buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:5000\"")  // Emulator
    }
}
```

**Wichtig für Emulator:**
- `10.0.2.2` ist die Emulator-IP für `localhost` auf dem Host-Rechner
- Für physische Geräte: IP-Adresse des Server-Rechners im lokalen Netzwerk verwenden

### Build & Installation

1. **Projekt öffnen:**
   ```bash
   cd radstation-android-app
   # In Android Studio öffnen
   ```

2. **Gradle Sync:**
   - Android Studio führt automatisch `Gradle Sync` durch
   - Falls nicht: `File` → `Sync Project with Gradle Files`

3. **App bauen:**
   ```bash
   ./gradlew assembleDebug
   # APK wird erstellt in: app/build/outputs/apk/debug/
   ```

4. **Auf Gerät installieren:**
   ```bash
   ./gradlew installDebug
   ```

---

## 🎨 Design-System

### Farben
Basierend auf dem Laufrad-App Design, aber klarer strukturiert:

```kotlin
val RadstationBlue = Color(0xFF00BCD4)        // Cyan/Türkis - Primary
val RadstationOrange = Color(0xFFFF9800)      // Orange - Secondary
val RadstationDarkBlue = Color(0xFF1A2332)    // Dunkles Blau - Dark Background
val RadstationGray = Color(0xFF2D3748)        // Grau - Surface
```

### UI-Komponenten
- **Cards** - Für Auftrags-Anzeige
- **Chips** - Für Filter und Status
- **Buttons** - Primary, Secondary, Outlined
- **FAB (Floating Action Button)** - Neuer Auftrag
- **Switches** - Für Flags (Angerufen, Mailbox, Bezahlt)

---

## 📐 Projekt-Struktur

```
app/src/main/java/de/radstation/werkstatt/
├── data/                           # Data Layer
│   ├── remote/
│   │   ├── dto/                   # Data Transfer Objects
│   │   │   ├── ApiModels.kt       # API Response/Request Models
│   │   │   └── Mappers.kt         # DTO -> Domain Mapper
│   │   └── RadstationApi.kt       # Retrofit API Interface
│   └── repository/                # Repository Pattern
│       └── AuftraegeRepository.kt
├── domain/                         # Domain Layer
│   └── model/                     # Domain Models
│       ├── Auftrag.kt             # Auftrag Business Object
│       ├── Teil.kt                # Teil Business Object
│       ├── Leihrad.kt             # Leihrad Business Object
│       └── Resource.kt            # API State Wrapper
├── presentation/                   # Presentation Layer (UI)
│   ├── dashboard/                 # Dashboard Feature
│   │   ├── DashboardScreen.kt
│   │   └── DashboardViewModel.kt
│   ├── auftraege/                 # Aufträge Feature
│   │   ├── list/                  # Liste aller Aufträge
│   │   ├── create/                # Neuer Auftrag (mit Kamera!)
│   │   └── detail/                # Auftrags-Details
│   ├── teile/                     # Teile Feature
│   │   └── list/
│   ├── common/                    # Shared UI Components
│   │   └── theme/
│   │       ├── Color.kt           # Farbdefinitionen
│   │       └── Theme.kt           # Material Theme Setup
│   └── navigation/
│       └── Navigation.kt          # Navigation Graph
├── di/                            # Dependency Injection
│   └── AppModule.kt               # Hilt Modules
├── MainActivity.kt                # Entry Point
└── RadstationApp.kt              # Application Class
```

---

## 🔌 API-Integration

Die App kommuniziert mit dem **bike-server** Backend über REST-API.

### Verfügbare Endpunkte

#### Aufträge
```kotlin
GET    /api/auftraege                      // Alle Aufträge laden
POST   /upload/auftrag                     // Auftrag mit Fotos hochladen
POST   /api/auftraege/create               // Auftrag ohne Fotos erstellen
PUT    /api/auftraege/{nummer}/status      // Status ändern
PUT    /api/auftraege/{nummer}/flags       // Flags ändern (angerufen, mailbox, bezahlt)
```

#### Teile *(noch nicht implementiert)*
```kotlin
GET    /api/teile                          // Alle Teile
GET    /api/teile/{id}                     // Ein Teil
POST   /api/teile/verbrauch-buchen        // Verbrauch buchen
```

### Beispiel: Auftrag erstellen

```kotlin
// ViewModel
viewModelScope.launch {
    val result = repository.createAuftrag(
        auftragsnummer = "123456",
        reparaturdatum = "2026-01-29T14:30",
        schluesselnummer = "42",
        fahrradmarke = "Cube",
        maengelbeschreibung = "Platter Reifen, Bremse quietscht"
    )
    
    when (result) {
        is Resource.Success -> { /* Erfolg */ }
        is Resource.Error -> { /* Fehler anzeigen */ }
        is Resource.Loading -> { /* Loading Spinner */ }
    }
}
```

---

## 📸 Kamera-Integration

Die App nutzt **CameraX** für Foto-Aufnahmen:

### Features
- ✅ Kamera-Permission Handling
- ✅ Fotos im Cache speichern
- ✅ Mehrere Fotos pro Auftrag
- ✅ Vorschau der Fotos
- ✅ Fotos löschen vor Upload

### Implementierung

```kotlin
// FileProvider für Kamera
val uri = viewModel.createImageUri(context)
takePictureLauncher.launch(uri)

// Nach erfolgreichem Foto
if (success) {
    viewModel.addPhoto()  // URI zur Liste hinzufügen
}
```

### Permissions
Die App fragt automatisch nach Kamera-Berechtigung beim ersten Mal.

---

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### UI Tests (Instrumented)
```bash
./gradlew connectedAndroidTest
```

*(Tests werden in zukünftigen Versionen hinzugefügt)*

---

## 🚀 Deployment

### Debug Build (für Entwicklung)
```bash
./gradlew assembleDebug
```

### Release Build (für Produktion)
```bash
./gradlew assembleRelease
# APK signieren erforderlich
```

### APK Signieren
```bash
jarsigner -verbose -sigalg SHA256withRSA \
  -digestalg SHA-256 \
  -keystore your-keystore.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  alias_name
```

---

## 🔧 Konfiguration

### Server-URL ändern

In `app/build.gradle.kts`:
```kotlin
buildConfigField("String", "API_BASE_URL", "\"http://YOUR_SERVER_IP:5000\"")
```

### App-Name ändern

In `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Dein App Name</string>
```

### Farben anpassen

In `presentation/common/theme/Color.kt`:
```kotlin
val RadstationBlue = Color(0xFFYOUR_COLOR)
```

---

## 📱 Screenshots & Demo

### Dashboard
```
┌──────────────────────────┐
│   🚴 Radstation          │
├──────────────────────────┤
│  ┌────────┐  ┌────────┐  │
│  │ 📋     │  │ 🔧     │  │
│  │Aufträge│  │ Teile  │  │
│  │   12   │  │    0   │  │
│  └────────┘  └────────┘  │
├──────────────────────────┤
│ Status Übersicht         │
│ ⏳ Nicht begonnen    5   │
│ 🔧 In Bearbeitung    4   │
│ ✅ Fertig            3   │
├──────────────────────────┤
│ Neueste Aufträge         │
│ ┌──────────────────────┐ │
│ │ #8272          🔧    │ │
│ │ 29.01.2026 13:29    │ │
│ │ Rose Bike           │ │
│ └──────────────────────┘ │
```

### Neuer Auftrag
```
┌──────────────────────────┐
│ ← Neuer Auftrag          │
├──────────────────────────┤
│ Auftragsnummer: 123456   │
│ Schlüsselnummer: *       │
│ Fahrradmarke:            │
│ Mängelbeschreibung:      │
│ ┌────────────────────┐   │
│ │                    │   │
│ │                    │   │
│ └────────────────────┘   │
├──────────────────────────┤
│ Fotos                    │
│ [📷][📷]               │
│ 📸 Foto aufnehmen        │
├──────────────────────────┤
│ ✅ Auftrag erstellen     │
└──────────────────────────┘
```

---

## ⚠️ Bekannte Einschränkungen

1. **Keine Kundendaten** - Aus Datenschutzgründen werden keine Namen/Telefonnummern gespeichert
2. **Nur Portrait-Modus** - App ist für Hochformat optimiert
3. **Kein Offline-Modus** - Benötigt aktive Verbindung zum Server
4. **Nur lokales Netzwerk** - Server muss im selben Netzwerk sein

---

## 🛣️ Roadmap

### Version 1.1 (Geplant)
- [ ] Teile-Verwaltung vollständig implementieren
- [ ] Verbrauch-Buchung für Aufträge
- [ ] Leihräder-Übersicht

### Version 1.2 (Geplant)
- [ ] Offline-Modus mit lokaler Datenbank
- [ ] Push-Benachrichtigungen
- [ ] Barcode-Scanner für Teile

### Version 2.0 (Zukunft)
- [ ] Statistiken & Reports
- [ ] Export-Funktionen
- [ ] Multi-User Support

---

## 🤝 Mitwirken

Contributions sind willkommen! Bitte beachte:

1. Fork das Repository
2. Erstelle einen Feature-Branch (`git checkout -b feature/AmazingFeature`)
3. Committe deine Änderungen (`git commit -m 'Add some AmazingFeature'`)
4. Push zum Branch (`git push origin feature/AmazingFeature`)
5. Öffne einen Pull Request

---

## 📄 Lizenz

MIT License - siehe [LICENSE](LICENSE) Datei

---

## 👥 Kontakt & Support

Bei Fragen oder Problemen:
- GitHub Issues erstellen
- E-Mail an: support@radstation.de

---

## 🙏 Danksagungen

- **Material Design** von Google
- **Jetpack Compose** Team
- **Hilt** für Dependency Injection
- **CameraX** für die Kamera-Integration

---

**Made with ❤️ for Radstation**

*"Einfach. Funktional. Fahrbereit."* 🚴

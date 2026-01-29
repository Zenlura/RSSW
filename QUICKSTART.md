# ⚡ Quickstart Guide

**Radstation Werkstatt App in 5 Minuten einrichten!**

---

## 🚀 Schnellstart

### 1. Voraussetzungen prüfen
```bash
# Android Studio installiert?
which android-studio  # macOS/Linux
where android-studio  # Windows

# Server läuft?
curl http://localhost:5000/api/auftraege
```

### 2. Projekt öffnen
```bash
# In Android Studio:
File → Open → radstation-android-app wählen
```

### 3. Server-IP eintragen
```kotlin
// app/build.gradle.kts

// FÜR EMULATOR:
buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:5000\"")

// FÜR ECHTES GERÄT:
buildConfigField("String", "API_BASE_URL", "\"http://192.168.1.XXX:5000\"")
```

### 4. App starten
```bash
# Emulator oder Gerät wählen
# Grünes Play-Symbol klicken
# Fertig! 🎉
```

---

## 📱 Erste Schritte in der App

### Dashboard
```
1. App öffnet sich auf dem Dashboard
2. Siehst du "Aufträge" und "Teile"? ✅
3. Siehst du Status-Übersicht? ✅
```

### Neuen Auftrag erstellen
```
1. Orange FAB (+) unten rechts klicken
2. Felder ausfüllen:
   - Schlüsselnummer: z.B. "42" (Pflicht!)
   - Fahrradmarke: z.B. "Cube"
   - Mängelbeschreibung: z.B. "Platter Reifen"
3. Optional: "📸 Foto aufnehmen"
   - Kamera-Berechtigung erlauben
   - Foto machen
   - Mehrere Fotos möglich
4. "✅ Auftrag erstellen"
5. Zurück zum Dashboard
```

### Aufträge anzeigen
```
1. "Aufträge" Card klicken
2. Liste aller Aufträge
3. Filter nutzen:
   - "Alle"
   - "⏳" (Nicht begonnen)
   - "🔧" (In Bearbeitung)
   - "✅" (Fertig)
4. Auftrag antippen für Details
```

### Auftrag bearbeiten
```
1. Auftrag in Liste antippen
2. Details-Screen öffnet sich
3. Status-Flags ändern:
   - 📞 Angerufen: An/Aus
   - 📧 Mailbox: An/Aus
   - 💰 Bezahlt: An/Aus
4. Status ändern:
   - Button wählen (⏳/🔧/✅)
5. Änderungen werden sofort gespeichert
```

---

## 🎯 Typische Workflows

### Workflow 1: Kunde bringt Fahrrad
```
1. FAB (+) klicken
2. Schlüsselnummer eingeben (z.B. Schließfach-Nr.)
3. Foto vom Fahrrad machen
4. Mängel notieren
5. Auftrag erstellen
6. Fahrrad ins Schließfach
```

### Workflow 2: Reparatur beginnen
```
1. Dashboard → "Aufträge"
2. Filter "⏳ Nicht begonnen"
3. Auftrag wählen
4. "🔧 In Bearbeitung" klicken
5. Optional: "📞 Angerufen" aktivieren
```

### Workflow 3: Reparatur fertig
```
1. Auftrag öffnen
2. "✅ Fertig" klicken
3. "📞 Angerufen" aktivieren
4. Falls Kunde zahlt: "💰 Bezahlt" aktivieren
5. Zurück zum Dashboard
```

---

## ⚙️ Einstellungen & Tipps

### Server-URL ändern (während App läuft nicht möglich!)
```
1. Android Studio: app/build.gradle.kts öffnen
2. URL ändern
3. "Sync Now" klicken
4. App neu bauen
```

### Cache leeren (bei Problemen)
```
1. App-Info öffnen (Lang auf App-Icon)
2. "Speicher" → "Cache leeren"
3. App neu starten
```

### Kamera-Berechtigung zurücksetzen
```
1. App-Info öffnen
2. "Berechtigungen"
3. "Kamera" → "Nicht erlaubt"
4. App öffnen → Erneut fragen
```

---

## 🐛 Schnelle Problemlösungen

### "Kann nicht zum Server verbinden"
```bash
# 1. Server prüfen
curl http://DEINE_IP:5000/api/auftraege

# 2. Firewall prüfen (Server-Rechner)
sudo ufw allow 5000  # Linux
# oder Windows Firewall entsprechend

# 3. IP in App prüfen
# build.gradle.kts → buildConfigField → richtige IP?
```

### "Kamera funktioniert nicht"
```
1. Berechtigung erteilt?
   → Einstellungen → Apps → Radstation → Berechtigungen
2. Kamera von anderer App blockiert?
   → Andere Apps schließen
3. App neu installieren
   → Deinstallieren → Neu installieren
```

### "Aufträge werden nicht geladen"
```
1. Server läuft?
   → python3 app.py im bike-server Ordner
2. Internet/WLAN aktiv?
   → Beide Geräte im selben Netzwerk
3. API erreichbar?
   → curl http://SERVER_IP:5000/api/auftraege
```

---

## 📊 Test-Daten erstellen

### Schnell 5 Test-Aufträge erstellen
```python
# Im bike-server Verzeichnis:
python3

>>> from api.auftraege import *
>>> for i in range(1, 6):
...     create_test_auftrag(i)
>>> exit()
```

### Oder manuell in der App
```
1. FAB (+) 5x klicken
2. Jeweils:
   - Schlüssel: "1", "2", "3", "4", "5"
   - Marke: "Cube", "Trek", "Giant", "Specialized", "Canyon"
   - Mängel: "Reifen platt", "Bremse defekt", etc.
```

---

## 🎨 Design-Anpassungen

### Farben ändern
```kotlin
// presentation/common/theme/Color.kt

val RadstationBlue = Color(0xFFYOUR_HEX)     // Primary
val RadstationOrange = Color(0xFFYOUR_HEX)   // Secondary
```

### App-Name ändern
```xml
<!-- app/src/main/res/values/strings.xml -->
<string name="app_name">Dein Name</string>
```

---

## ✅ Checkliste: Alles funktioniert?

- [ ] App startet ohne Fehler
- [ ] Dashboard zeigt Statistiken
- [ ] "Neuer Auftrag" öffnet sich
- [ ] Kamera kann geöffnet werden
- [ ] Foto wird aufgenommen
- [ ] Auftrag wird erstellt
- [ ] Auftrag erscheint in Liste
- [ ] Status kann geändert werden
- [ ] Flags können geändert werden
- [ ] Zurück-Button funktioniert

**Alle ✅? Perfekt! Du bist startklar! 🎉**

---

## 📚 Weiterführende Docs

- [README.md](README.md) - Vollständige Dokumentation
- [INSTALLATION.md](INSTALLATION.md) - Detaillierte Installation
- [API.md](API.md) - API-Dokumentation *(falls vorhanden)*

---

## 🆘 Support

**Bei Problemen:**
1. Logs checken (Logcat in Android Studio)
2. GitHub Issues durchsuchen
3. Neues Issue erstellen mit:
   - Android Version
   - Fehlermeldung
   - Schritt-für-Schritt Reproduktion

---

**Viel Erfolg! 🚴**

package de.radstation.werkstatt.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Auftrag(
    val auftragsnummer: String,
    val timestamp: String,
    val scanDate: String,              // Reparaturdatum
    val schluesselnummer: String,
    val fahrradmarke: String = "",
    val maengelbeschreibung: String = "",
    val status: AuftragStatus = AuftragStatus.NICHT_BEGONNEN,
    val angerufen: Boolean = false,
    val mailbox: Boolean = false,
    val bezahlt: Boolean = false,
    val confidence: Double = 0.0,
    val photos: List<String> = emptyList()
) : Parcelable {
    
    val formattedScanDate: String
        get() = try {
            // Datum formatieren: "2026-01-29T13:45" -> "29.01.2026 13:45"
            val parts = scanDate.split("T")
            val dateParts = parts[0].split("-")
            val time = if (parts.size > 1) parts[1].substring(0, 5) else ""
            "${dateParts[2]}.${dateParts[1]}.${dateParts[0]} $time"
        } catch (e: Exception) {
            scanDate
        }
    
    val statusColor: Long
        get() = when (status) {
            AuftragStatus.NICHT_BEGONNEN -> 0xFFFF6B6B
            AuftragStatus.IN_BEARBEITUNG -> 0xFFFFA500
            AuftragStatus.FERTIG -> 0xFF4CAF50
            AuftragStatus.ABGEHOLT -> 0xFF808080
        }
}

enum class AuftragStatus {
    NICHT_BEGONNEN,
    IN_BEARBEITUNG,
    FERTIG,
    ABGEHOLT;
    
    val displayName: String
        get() = when (this) {
            NICHT_BEGONNEN -> "Nicht begonnen"
            IN_BEARBEITUNG -> "In Bearbeitung"
            FERTIG -> "Fertig"
            ABGEHOLT -> "Abgeholt"
        }
    
    val icon: String
        get() = when (this) {
            NICHT_BEGONNEN -> "⏳"
            IN_BEARBEITUNG -> "🔧"
            FERTIG -> "✅"
            ABGEHOLT -> "📦"
        }
}

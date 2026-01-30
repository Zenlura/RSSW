package de.radstation.werkstatt.data.remote.dto

import de.radstation.werkstatt.domain.model.Auftrag
import de.radstation.werkstatt.domain.model.AuftragStatus
import de.radstation.werkstatt.domain.model.Teil
import de.radstation.werkstatt.domain.model.Lieferant
import de.radstation.werkstatt.domain.model.Leihrad
import kotlinx.serialization.Serializable

// Aufträge DTOs (Scanner-basiert)
@Serializable
data class AuftraegeResponse(
    val success: Boolean,
    val auftraege: List<AuftragDto>,
    val message: String? = null
)

@Serializable
data class AuftragResponse(
    val success: Boolean,
    val auftrag: AuftragDto,
    val message: String? = null
)

@Serializable
data class AuftragDto(
    val auftragsnummer: String,
    val timestamp: String,
    val scanDate: String,
    val schluesselnummer: String,
    val fahrradmarke: String = "",
    val maengelbeschreibung: String = "",
    val status: String = "NICHT_BEGONNEN",
    val angerufen: Boolean = false,
    val mailbox: Boolean = false,
    val bezahlt: Boolean = false,
    val confidence: Double = 0.0,
    val photos: List<String>? = null
)

fun AuftragDto.toAuftrag(): Auftrag {
    return Auftrag(
        auftragsnummer = auftragsnummer,
        timestamp = timestamp,
        scanDate = scanDate,
        schluesselnummer = schluesselnummer,
        fahrradmarke = fahrradmarke,
        maengelbeschreibung = maengelbeschreibung,
        status = try {
            AuftragStatus.valueOf(status)
        } catch (e: Exception) {
            AuftragStatus.NICHT_BEGONNEN
        },
        angerufen = angerufen,
        mailbox = mailbox,
        bezahlt = bezahlt,
        confidence = confidence,
        photos = photos ?: emptyList()
    )
}

@Serializable
data class CreateAuftragRequest(
    val auftragsnummer: String,
    val reparaturdatum: String,
    val schluesselnummer: String,
    val fahrradmarke: String = "",
    val maengelbeschreibung: String = "",
    val angerufen: Boolean = false,
    val mailbox: Boolean = false,
    val bezahlt: Boolean = false
)

@Serializable
data class StatusUpdateRequest(
    val status: String
)

@Serializable
data class FlagsUpdateRequest(
    val angerufen: Boolean? = null,
    val mailbox: Boolean? = null,
    val bezahlt: Boolean? = null
)

// Teile DTOs - KORRIGIERT FÜR STRING-IDS
@Serializable
data class TeileResponse(
    val success: Boolean,
    val teile: List<TeilDto>,
    val count: Int? = null,
    val message: String? = null
)

@Serializable
data class TeilResponse(
    val success: Boolean,
    val teil: TeilDto,
    val message: String? = null
)

@Serializable
data class TeilDto(
    val id: String,                           // STRING statt Int!
    val artikelnummer: String,
    val bezeichnung: String,
    val kategorie: String,
    val bestand_lager: Int = 0,
    val bestand_werkstatt: Int = 0,
    val bestand_gesamt: Int = 0,
    val einkaufspreis: Double = 0.0,
    val verkaufspreis: Double = 0.0,
    val mindestbestand: Int = 0,
    val hersteller: String? = null,
    val lieferanten: List<LieferantDto>? = null,
    val lagerort: String? = null,
    val notizen: String? = null,
    val einheit: String = "Stück"
)

@Serializable
data class LieferantDto(
    val name: String,
    val artikelnr: String? = null
)

fun TeilDto.toTeil(): Teil {
    return Teil(
        id = id,
        artikelnummer = artikelnummer,
        bezeichnung = bezeichnung,
        kategorie = kategorie,
        bestandLager = bestand_lager,
        bestandWerkstatt = bestand_werkstatt,
        // WICHTIG: bestandGesamt NICHT setzen - wird automatisch berechnet!
        einkaufspreis = einkaufspreis,
        verkaufspreis = verkaufspreis,
        mindestbestand = mindestbestand,
        hersteller = hersteller,
        lieferanten = lieferanten?.map { it.toLieferant() } ?: emptyList(),
        lagerort = lagerort,
        notizen = notizen,
        einheit = einheit
    )
}

fun LieferantDto.toLieferant(): Lieferant {
    return Lieferant(
        name = name,
        artikelnr = artikelnr
    )
}

// Fehlende Teile-DTOs
@Serializable
data class BestandUpdateRequest(
    val typ: String,           // "zugang" oder "abgang"
    val menge: Int,
    val ort: String = "werkstatt",  // "lager" oder "werkstatt"
    val grund: String = ""
)

@Serializable
data class VerbrauchBuchungRequest(
    val auftragsnummer: String,
    val teile: List<VerbrauchTeilItem>
)

@Serializable
data class VerbrauchTeilItem(
    val teil_id: String,
    val menge: Int
)

@Serializable
data class VerbrauchResponse(
    val success: Boolean,
    val message: String,
    val teile: List<VerbrauchTeilErgebnis>? = null
)

@Serializable
data class VerbrauchTeilErgebnis(
    val id: String,
    val name: String,
    val menge: Int,
    val neuer_bestand: Int
)

@Serializable
data class KategorienResponse(
    val success: Boolean,
    val kategorien: List<KategorieDto>,
    val message: String? = null
)

@Serializable
data class KategorieDto(
    val id: String,
    val name: String,
    val icon: String
)

@Serializable
data class TeileStatsResponse(
    val success: Boolean,
    val stats: TeileStatsDto,
    val message: String? = null
)

@Serializable
data class TeileStatsDto(
    val gesamt_teile: Int,
    val low_stock: Int,
    val out_of_stock: Int,
    val gesamtwert_einkauf_gesamt: Double,
    val gesamtwert_verkauf_gesamt: Double
)

// Leihräder DTOs
@Serializable
data class LeihraederResponse(
    val success: Boolean,
    val leihraeder: List<LeihradDto>,
    val message: String? = null
)

@Serializable
data class LeihradDto(
    val id: Int,
    val nummer: String,
    val typ: String,
    val marke: String? = null,
    val farbe: String? = null,
    val status: String,
    val aktuellVerliehen: Boolean,
    val aktuellerKunde: String? = null,
    val verleihDatum: String? = null,
    val rueckgabeDatum: String? = null
)

fun LeihradDto.toLeihrad(): Leihrad {
    return Leihrad(
        id = id,
        nummer = nummer,
        typ = typ,
        marke = marke,
        farbe = farbe,
        status = status,
        aktuellVerliehen = aktuellVerliehen,
        aktuellerKunde = aktuellerKunde,
        verleihDatum = verleihDatum,
        rueckgabeDatum = rueckgabeDatum
    )
}

@Serializable
data class LeihraederStatsResponse(
    val success: Boolean,
    val stats: LeihraederStats,
    val message: String? = null
)

@Serializable
data class LeihraederStats(
    val totalRaeder: Int,
    val verfuegbar: Int,
    val verliehen: Int,
    val inWartung: Int
)

// Upload Response
@Serializable
data class UploadResponse(
    val success: Boolean,
    val filename: String? = null,
    val url: String? = null,
    val message: String? = null
)
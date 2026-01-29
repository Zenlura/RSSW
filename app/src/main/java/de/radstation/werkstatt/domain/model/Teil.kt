package de.radstation.werkstatt.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Teil(
    val id: String,                          // Artikelnummer als ID (z.B. "504b")
    val artikelnummer: String,
    val bezeichnung: String,
    val kategorie: String,
    val bestandLager: Int = 0,
    val bestandWerkstatt: Int = 0,
    val bestandGesamt: Int = 0,
    val einkaufspreis: Double = 0.0,
    val verkaufspreis: Double = 0.0,
    val mindestbestand: Int = 0,
    val hersteller: String? = null,
    val lieferanten: List<Lieferant> = emptyList(),
    val lagerort: String? = null,
    val notizen: String? = null,
    val einheit: String = "Stück"
) : Parcelable {

    val istNiedrigerBestand: Boolean
        get() = bestandGesamt <= mindestbestand && mindestbestand > 0

    val istAusverkauft: Boolean
        get() = bestandGesamt == 0

    val bestandStatus: BestandStatus
        get() = when {
            istAusverkauft -> BestandStatus.AUSVERKAUFT
            istNiedrigerBestand -> BestandStatus.NIEDRIG
            else -> BestandStatus.OK
        }

    val bestandStatusColor: Long
        get() = when (bestandStatus) {
            BestandStatus.OK -> 0xFF4CAF50
            BestandStatus.NIEDRIG -> 0xFFFFA500
            BestandStatus.AUSVERKAUFT -> 0xFFFF6B6B
        }
}

@Parcelize
data class Lieferant(
    val name: String,
    val artikelnr: String? = null
) : Parcelable

enum class BestandStatus {
    OK,
    NIEDRIG,
    AUSVERKAUFT;

    val displayName: String
        get() = when (this) {
            OK -> "Auf Lager"
            NIEDRIG -> "Niedrig"
            AUSVERKAUFT -> "Ausverkauft"
        }

    val icon: String
        get() = when (this) {
            OK -> "✅"
            NIEDRIG -> "⚠️"
            AUSVERKAUFT -> "❌"
        }
}
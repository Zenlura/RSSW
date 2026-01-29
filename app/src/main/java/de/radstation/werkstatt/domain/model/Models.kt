package de.radstation.werkstatt.domain.model

data class Teil(
    val id: Int,
    val name: String,
    val kategorie: String,
    val bestand: Int,
    val mindestbestand: Int,
    val preis: Double,
    val lieferant: String? = null,
    val beschreibung: String? = null,
    val einheit: String = "Stück"
)

data class Leihrad(
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

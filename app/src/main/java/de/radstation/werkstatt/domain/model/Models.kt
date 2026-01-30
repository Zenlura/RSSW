package de.radstation.werkstatt.domain.model

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
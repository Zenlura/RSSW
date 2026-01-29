package de.radstation.werkstatt.data.repository

import de.radstation.werkstatt.data.remote.RadstationApi
import de.radstation.werkstatt.data.remote.dto.BestandUpdateRequest
import de.radstation.werkstatt.data.remote.dto.toTeil
import de.radstation.werkstatt.domain.common.Resource
import de.radstation.werkstatt.domain.model.Teil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeileRepository @Inject constructor(
    private val api: RadstationApi
) {
    suspend fun getTeile(): Resource<List<Teil>> {
        return try {
            val response = api.getTeile()
            if (response.isSuccessful && response.body()?.success == true) {
                val teile = response.body()!!.teile.map { it.toTeil() }
                Resource.Success(teile)
            } else {
                Resource.Error(response.body()?.message ?: "Fehler beim Laden der Teile")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unbekannter Fehler")
        }
    }

    suspend fun getTeilById(id: String): Resource<Teil> {  // ← STRING statt Int!
        return try {
            val response = api.getTeil(id)  // ← Direkt übergeben
            if (response.isSuccessful && response.body()?.success == true) {
                Resource.Success(response.body()!!.teil.toTeil())
            } else {
                Resource.Error(response.body()?.message ?: "Fehler beim Laden des Teils")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unbekannter Fehler")
        }
    }

    suspend fun updateBestand(
        id: String,           // ← STRING statt Int!
        typ: String,          // ← "zugang" oder "abgang"
        menge: Int,           // ← Menge
        ort: String = "werkstatt",  // ← "lager" oder "werkstatt"
        grund: String = ""    // ← Optional
    ): Resource<Unit> {
        return try {
            val request = BestandUpdateRequest(
                typ = typ,
                menge = menge,
                ort = ort,
                grund = grund
            )
            val response = api.updateBestand(id, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.body()?.message ?: "Fehler beim Aktualisieren des Bestands")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unbekannter Fehler")
        }
    }
}
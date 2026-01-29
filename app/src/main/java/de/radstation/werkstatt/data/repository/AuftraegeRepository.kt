package de.radstation.werkstatt.data.repository

import de.radstation.werkstatt.data.remote.RadstationApi
import de.radstation.werkstatt.data.remote.dto.*
import de.radstation.werkstatt.domain.model.Auftrag
import de.radstation.werkstatt.domain.model.AuftragStatus
import de.radstation.werkstatt.domain.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuftraegeRepository @Inject constructor(
    private val api: RadstationApi
) {
    
    fun getAuftraege(): Flow<Resource<List<Auftrag>>> = flow {
        emit(Resource.Loading())
        
        try {
            val response = api.getAuftraege()
            if (response.isSuccessful && response.body()?.success == true) {
                val auftraege = response.body()!!.auftraege.map { it.toAuftrag() }
                emit(Resource.Success(auftraege))
            } else {
                emit(Resource.Error("Fehler beim Laden der Aufträge"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Netzwerkfehler"))
        }
    }
    
    suspend fun uploadAuftragWithPhotos(
        auftragsnummer: String,
        reparaturdatum: String,
        schluesselnummer: String,
        fahrradmarke: String,
        maengelbeschreibung: String,
        photos: List<File>
    ): Resource<String> {
        return try {
            // JSON-Body erstellen
            val auftragJson = """
                {
                    "auftragsnummer": "$auftragsnummer",
                    "reparaturdatum": "$reparaturdatum",
                    "schluesselnummer": "$schluesselnummer",
                    "fahrradmarke": "$fahrradmarke",
                    "maengelbeschreibung": "$maengelbeschreibung"
                }
            """.trimIndent()
            
            val auftragBody = auftragJson.toRequestBody("application/json".toMediaTypeOrNull())
            
            // Fotos als MultipartBody.Part
            val photoParts = photos.mapIndexed { index, file ->
                val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("photos", "photo_$index.jpg", requestBody)
            }
            
            val response = api.uploadAuftrag(auftragBody, photoParts)
            
            if (response.isSuccessful && response.body()?.success == true) {
                Resource.Success(response.body()!!.message ?: "Erfolgreich hochgeladen")
            } else {
                Resource.Error(response.body()?.message ?: "Upload fehlgeschlagen")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Netzwerkfehler")
        }
    }
    
    suspend fun createAuftrag(
        auftragsnummer: String,
        reparaturdatum: String,
        schluesselnummer: String,
        fahrradmarke: String = "",
        maengelbeschreibung: String = "",
        angerufen: Boolean = false,
        mailbox: Boolean = false,
        bezahlt: Boolean = false
    ): Resource<Auftrag> {
        return try {
            val request = CreateAuftragRequest(
                auftragsnummer = auftragsnummer,
                reparaturdatum = reparaturdatum,
                schluesselnummer = schluesselnummer,
                fahrradmarke = fahrradmarke,
                maengelbeschreibung = maengelbeschreibung,
                angerufen = angerufen,
                mailbox = mailbox,
                bezahlt = bezahlt
            )
            
            val response = api.createAuftrag(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val auftrag = response.body()!!.auftrag.toAuftrag()
                Resource.Success(auftrag)
            } else {
                Resource.Error("Fehler beim Erstellen")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Netzwerkfehler")
        }
    }
    
    suspend fun updateStatus(auftragsnummer: String, status: AuftragStatus): Resource<Auftrag> {
        return try {
            val request = StatusUpdateRequest(status = status.name)
            val response = api.updateStatus(auftragsnummer, request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val auftrag = response.body()!!.auftrag.toAuftrag()
                Resource.Success(auftrag)
            } else {
                Resource.Error("Fehler beim Aktualisieren")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Netzwerkfehler")
        }
    }
    
    suspend fun updateFlags(
        auftragsnummer: String,
        angerufen: Boolean? = null,
        mailbox: Boolean? = null,
        bezahlt: Boolean? = null
    ): Resource<Auftrag> {
        return try {
            val request = FlagsUpdateRequest(
                angerufen = angerufen,
                mailbox = mailbox,
                bezahlt = bezahlt
            )
            val response = api.updateFlags(auftragsnummer, request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val auftrag = response.body()!!.auftrag.toAuftrag()
                Resource.Success(auftrag)
            } else {
                Resource.Error("Fehler beim Aktualisieren")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Netzwerkfehler")
        }
    }
}

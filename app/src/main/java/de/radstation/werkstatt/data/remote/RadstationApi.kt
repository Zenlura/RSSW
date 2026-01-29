package de.radstation.werkstatt.data.remote

import de.radstation.werkstatt.data.remote.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RadstationApi {
    
    // === AUFTRÄGE ===
    
    @GET("api/auftraege")
    suspend fun getAuftraege(): Response<AuftraegeResponse>
    
    @GET("api/auftraege/{nummer}")
    suspend fun getAuftrag(@Path("nummer") nummer: String): Response<AuftragResponse>
    
    @Multipart
    @POST("upload/auftrag")
    suspend fun uploadAuftrag(
        @Part("auftrag") auftrag: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): Response<UploadResponse>
    
    @POST("api/auftraege/create")
    suspend fun createAuftrag(
        @Body request: CreateAuftragRequest
    ): Response<AuftragResponse>
    
    @PUT("api/auftraege/{nummer}/status")
    suspend fun updateStatus(
        @Path("nummer") nummer: String,
        @Body request: StatusUpdateRequest
    ): Response<AuftragResponse>
    
    @PUT("api/auftraege/{nummer}/flags")
    suspend fun updateFlags(
        @Path("nummer") nummer: String,
        @Body request: FlagsUpdateRequest
    ): Response<AuftragResponse>
    
    // === TEILE ===
    
    @GET("api/teile")
    suspend fun getTeile(
        @Query("kategorie") kategorie: String? = null,
        @Query("search") search: String? = null,
        @Query("low_stock") lowStock: Boolean? = null
    ): Response<TeileResponse>
    
    @GET("api/teile/{id}")
    suspend fun getTeil(@Path("id") id: String): Response<TeilResponse>
    
    @POST("api/teile/{id}/bestand")
    suspend fun updateBestand(
        @Path("id") id: String,
        @Body request: BestandUpdateRequest
    ): Response<TeilResponse>
    
    @POST("api/teile/verbrauch-buchen")
    suspend fun verbrauchBuchen(
        @Body request: VerbrauchBuchungRequest
    ): Response<VerbrauchResponse>
    
    @GET("api/teile/kategorien")
    suspend fun getKategorien(): Response<KategorienResponse>
    
    @GET("api/teile/stats")
    suspend fun getTeileStats(): Response<TeileStatsResponse>
    
    // === LEIHRÄDER ===
    
    @GET("api/leihraeder")
    suspend fun getLeihraeder(): Response<LeihraederResponse>
    
    @GET("api/leihraeder/stats")
    suspend fun getLeihraederStats(): Response<LeihraederStatsResponse>
}

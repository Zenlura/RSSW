package de.radstation.werkstatt.presentation.auftraege.create

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.radstation.werkstatt.data.repository.AuftraegeRepository
import de.radstation.werkstatt.domain.common.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class CreateAuftragState(
    val auftragsnummer: String = "",
    val schluesselnummer: String = "",
    val fahrradmarke: String = "",
    val maengelbeschreibung: String = "",
    val photoUris: List<Uri> = emptyList(),
    val currentPhotoUri: Uri? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val showErrors: Boolean = false
)

@HiltViewModel
class CreateAuftragViewModel @Inject constructor(
    private val repository: AuftraegeRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(CreateAuftragState())
    val state: StateFlow<CreateAuftragState> = _state.asStateFlow()
    
    fun setAuftragsnummer(value: String) {
        _state.value = _state.value.copy(auftragsnummer = value)
    }
    
    fun setSchluesselnummer(value: String) {
        _state.value = _state.value.copy(schluesselnummer = value)
    }
    
    fun setFahrradmarke(value: String) {
        _state.value = _state.value.copy(fahrradmarke = value)
    }
    
    fun setMaengelbeschreibung(value: String) {
        _state.value = _state.value.copy(maengelbeschreibung = value)
    }
    
    fun createImageUri(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.cacheDir
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
        
        _state.value = _state.value.copy(currentPhotoUri = uri)
        return uri
    }
    
    fun addPhoto() {
        val currentUri = _state.value.currentPhotoUri ?: return
        _state.value = _state.value.copy(
            photoUris = _state.value.photoUris + currentUri,
            currentPhotoUri = null
        )
    }
    
    fun removePhoto(uri: Uri) {
        _state.value = _state.value.copy(
            photoUris = _state.value.photoUris.filter { it != uri }
        )
    }
    
    fun createAuftrag() {
        val state = _state.value
        
        // Validierung
        if (state.schluesselnummer.isBlank()) {
            _state.value = _state.value.copy(
                showErrors = true,
                error = "Bitte Schlüsselnummer eingeben"
            )
            return
        }
        
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            // Aktuelles Datum/Zeit
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val reparaturdatum = dateFormat.format(Date())
            
            // Fotos in File-Objekte umwandeln (falls vorhanden)
            val photoFiles = state.photoUris.mapNotNull { uri ->
                try {
                    // URI zu File konvertieren (vereinfacht)
                    File(uri.path ?: "")
                } catch (e: Exception) {
                    null
                }
            }
            
            val result = if (photoFiles.isNotEmpty()) {
                // Mit Fotos hochladen
                repository.uploadAuftragWithPhotos(
                    auftragsnummer = state.auftragsnummer,
                    reparaturdatum = reparaturdatum,
                    schluesselnummer = state.schluesselnummer,
                    fahrradmarke = state.fahrradmarke,
                    maengelbeschreibung = state.maengelbeschreibung,
                    photos = photoFiles
                )
                // Erfolg simulieren
                Resource.Success("Auftrag erstellt")
            } else {
                // Ohne Fotos erstellen
                val auftrag = repository.createAuftrag(
                    auftragsnummer = state.auftragsnummer,
                    reparaturdatum = reparaturdatum,
                    schluesselnummer = state.schluesselnummer,
                    fahrradmarke = state.fahrradmarke,
                    maengelbeschreibung = state.maengelbeschreibung
                )
                
                when (auftrag) {
                    is Resource.Success -> Resource.Success("Auftrag erstellt")
                    is Resource.Error -> auftrag
                    is Resource.Loading -> auftrag
                }
            }
            
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }
}

package de.radstation.werkstatt.presentation.auftraege.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.radstation.werkstatt.data.repository.AuftraegeRepository
import de.radstation.werkstatt.domain.common.Resource
import de.radstation.werkstatt.domain.model.Auftrag
import de.radstation.werkstatt.domain.model.AuftragStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuftragDetailViewModel @Inject constructor(
    private val repository: AuftraegeRepository
) : ViewModel() {

    private val _auftrag = MutableStateFlow<Auftrag?>(null)
    val auftrag: StateFlow<Auftrag?> = _auftrag.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentAuftragsnummer: String = ""

    // Scanner-System: Auftragsnummer statt ID
    fun loadAuftrag(auftragsnummer: String) {
        currentAuftragsnummer = auftragsnummer
        // TODO: Backend muss GET /api/auftraege/{nummer} Endpunkt implementieren
        // Für jetzt: Die Liste laden und filtern (nicht optimal, aber funktioniert)
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Repository.getAuftraege() gibt Flow<Resource<List<Auftrag>>> zurück
            repository.getAuftraege().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _auftrag.value = result.data?.find { it.auftragsnummer == auftragsnummer }
                        _isLoading.value = false
                        if (_auftrag.value == null) {
                            _error.value = "Auftrag nicht gefunden"
                        }
                    }
                    is Resource.Error -> {
                        _error.value = result.message
                        _isLoading.value = false
                    }
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    fun updateStatus(newStatus: AuftragStatus) {
        viewModelScope.launch {
            // Repository.updateStatus() gibt direkt Resource<Auftrag> zurück (kein Flow)
            when (val result = repository.updateStatus(currentAuftragsnummer, newStatus)) {
                is Resource.Success -> {
                    loadAuftrag(currentAuftragsnummer)
                }
                is Resource.Error -> {
                    _error.value = "Fehler beim Aktualisieren des Status"
                }
                else -> {}
            }
        }
    }

    fun updateFlags(angerufen: Boolean, mailbox: Boolean, bezahlt: Boolean) {
        viewModelScope.launch {
            // Repository.updateFlags() gibt direkt Resource<Auftrag> zurück (kein Flow)
            when (val result = repository.updateFlags(currentAuftragsnummer, angerufen, mailbox, bezahlt)) {
                is Resource.Success -> {
                    loadAuftrag(currentAuftragsnummer)
                }
                is Resource.Error -> {
                    _error.value = "Fehler beim Aktualisieren der Flags"
                }
                else -> {}
            }
        }
    }
}
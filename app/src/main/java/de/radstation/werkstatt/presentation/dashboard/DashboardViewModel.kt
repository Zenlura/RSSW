package de.radstation.werkstatt.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.radstation.werkstatt.data.repository.AuftraegeRepository
import de.radstation.werkstatt.domain.model.Auftrag
import de.radstation.werkstatt.domain.model.AuftragStatus
import de.radstation.werkstatt.domain.common.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardState(
    val auftraege: List<Auftrag> = emptyList(),
    val stats: Stats = Stats(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class Stats(
    val gesamt: Int = 0,
    val nichtBegonnen: Int = 0,
    val inBearbeitung: Int = 0,
    val fertig: Int = 0
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val auftraegeRepository: AuftraegeRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()
    
    init {
        loadAuftraege()
    }
    
    fun loadAuftraege() {
        viewModelScope.launch {
            auftraegeRepository.getAuftraege().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        val auftraege = resource.data ?: emptyList()
                        val stats = Stats(
                            gesamt = auftraege.size,
                            nichtBegonnen = auftraege.count { it.status == AuftragStatus.NICHT_BEGONNEN },
                            inBearbeitung = auftraege.count { it.status == AuftragStatus.IN_BEARBEITUNG },
                            fertig = auftraege.count { it.status == AuftragStatus.FERTIG }
                        )
                        _state.value = _state.value.copy(
                            auftraege = auftraege.sortedByDescending { it.timestamp },
                            stats = stats,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                    }
                }
            }
        }
    }
}

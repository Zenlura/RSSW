package de.radstation.werkstatt.presentation.auftraege.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.radstation.werkstatt.data.repository.AuftraegeRepository
import de.radstation.werkstatt.domain.common.Resource
import de.radstation.werkstatt.domain.model.Auftrag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuftraegeListViewModel @Inject constructor(
    private val repository: AuftraegeRepository
) : ViewModel() {

    private val _auftraege = MutableStateFlow<List<Auftrag>>(emptyList())
    val auftraege: StateFlow<List<Auftrag>> = _auftraege.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAuftraege()
    }

    fun loadAuftraege() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Repository.getAuftraege() gibt Flow<Resource<List<Auftrag>>> zurück
            repository.getAuftraege().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _auftraege.value = result.data ?: emptyList()
                        _isLoading.value = false
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
}
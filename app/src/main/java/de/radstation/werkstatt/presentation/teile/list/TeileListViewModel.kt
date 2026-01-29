package de.radstation.werkstatt.presentation.teile.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.radstation.werkstatt.data.repository.TeileRepository
import de.radstation.werkstatt.domain.common.Resource
import de.radstation.werkstatt.domain.model.Teil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeileListViewModel @Inject constructor(
    private val repository: TeileRepository
) : ViewModel() {

    private val _teile = MutableStateFlow<List<Teil>>(emptyList())
    val teile: StateFlow<List<Teil>> = _teile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadTeile()
    }

    fun loadTeile() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Repository.getTeile() gibt direkt Resource<List<Teil>> zurück (kein Flow)
            when (val result = repository.getTeile()) {
                is Resource.Success -> {
                    _teile.value = result.data ?: emptyList()
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

    fun updateBestand(id: Int, neuerBestand: Int) {
        viewModelScope.launch {
            // Repository.updateBestand() gibt direkt Resource<Unit> zurück
            when (repository.updateBestand(id, neuerBestand)) {
                is Resource.Success -> {
                    loadTeile()
                }
                is Resource.Error -> {
                    _error.value = "Fehler beim Aktualisieren des Bestands"
                }
                else -> {}
            }
        }
    }
}
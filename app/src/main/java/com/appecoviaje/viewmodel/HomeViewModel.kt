package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HomeUiState(
    val welcomeMessage: String = "Bienvenido a EcoViaje 🌱"
)

class HomeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Escucha el DataStore y actualiza el mensaje
        viewModelScope.launch {
            userPreferencesRepository.userToken.collectLatest { token ->
                val name = token?.takeIf { it.isNotBlank() } ?: "Viajero"
                _uiState.value = HomeUiState(welcomeMessage = "Bienvenido, $name 👋")
            }
        }
    }
}

package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val welcomeMessage: String = "Pantalla Principal"
)

class HomeViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.userToken.collect { token ->
                _uiState.update {
                    it.copy(welcomeMessage = "Bienvenido, Usuario ${token ?: "Invitado"}")
                }
            }
        }
    }
}

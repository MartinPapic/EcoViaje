package com.appecoviaje.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username = "",
    val password = "",
    val isLoading = false,
    val errorMessage = "",
    val loginSuccess = false
)

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { currentState ->
            currentState.copy(username = username)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(password = password)
        }
    }

    fun onLoginClick() {
        if (!isInputValid()) {
            _uiState.update { it.copy(errorMessage = "Please enter both username and password.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Simulate network request
            kotlinx.coroutines.delay(1000)
            userPreferencesRepository.saveUserToken("dummy_token")
            _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
        }
    }

    private fun isInputValid(): Boolean {
        return uiState.value.username.isNotBlank() && uiState.value.password.isNotBlank()
    }
}

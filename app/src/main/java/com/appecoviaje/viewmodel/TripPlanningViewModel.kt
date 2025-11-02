package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.Trip
import com.appecoviaje.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class TripPlanningUiState(
    val trips: List<Trip> = emptyList()
)

class TripPlanningViewModel(private val tripRepository: TripRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TripPlanningUiState())
    val uiState: StateFlow<TripPlanningUiState> = _uiState.asStateFlow()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        tripRepository.getAllTrips()
            .onEach { trips ->
                _uiState.value = TripPlanningUiState(trips = trips)
            }
            .launchIn(viewModelScope)
    }
}

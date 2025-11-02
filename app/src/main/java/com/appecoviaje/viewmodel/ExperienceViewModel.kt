package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.Experience
import com.appecoviaje.data.ExperienceRepository
import com.appecoviaje.data.Trip
import com.appecoviaje.data.TripRepository
import com.appecoviaje.data.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExperienceViewModel(
    private val experienceRepository: ExperienceRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _selectedTripId = MutableStateFlow<Int?>(null)
    val selectedTripId: StateFlow<Int?> = _selectedTripId

    val experiences: StateFlow<List<Experience>> = selectedTripId
        .flatMapLatest { tripId ->
            if (tripId != null) {
                experienceRepository.getExperiencesForTrip(tripId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val trips: StateFlow<List<Trip>> = tripRepository.getAllTrips()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setSelectedTripId(tripId: Int) {
        _selectedTripId.value = tripId
    }

    fun addExperience(experience: Experience) {
        viewModelScope.launch {
            experienceRepository.insert(experience)
        }
    }

    fun deleteExperience(experienceId: Int) {
        viewModelScope.launch {
            experienceRepository.delete(experienceId)
        }
    }
}

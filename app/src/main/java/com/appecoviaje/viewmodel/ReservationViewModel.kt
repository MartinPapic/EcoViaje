package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.Reservation
import com.appecoviaje.data.ReservationRepository
import com.appecoviaje.data.Trip
import com.appecoviaje.data.TripRepository
import com.appecoviaje.data.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReservationViewModel(
    private val reservationRepository: ReservationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val tripRepository: TripRepository
) : ViewModel() {

    // All reservations for the current user
    val reservations: StateFlow<List<Reservation>> = userPreferencesRepository.userToken
        .flatMapLatest { token ->
            val userId = token?.toIntOrNull() ?: -1
            reservationRepository.getReservationsForUser(userId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All trips
    val trips: StateFlow<List<Trip>> = tripRepository.getAllTrips()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Add a reservation for a given trip (ViewModel handles user ID and timestamp)
    fun addReservationForTrip(tripId: Int) {
        viewModelScope.launch {
            val userId = userPreferencesRepository.userToken.firstOrNull()?.toIntOrNull() ?: return@launch
            val reservation = Reservation(
                tripId = tripId,
                userId = userId,
                reservationDate = System.currentTimeMillis()
            )
            reservationRepository.insert(reservation)
        }
    }

    fun deleteReservation(reservationId: Int) {
        viewModelScope.launch {
            reservationRepository.delete(reservationId)
        }
    }
}

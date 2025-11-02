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

    val reservations: StateFlow<List<Reservation>> = userPreferencesRepository.userToken
        .flatMapLatest { userToken ->
            val userId = userToken?.toIntOrNull() ?: -1
            reservationRepository.getReservationsForUser(userId)
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

    fun addReservation(reservation: Reservation) {
        viewModelScope.launch {
            reservationRepository.insert(reservation)
        }
    }

    fun deleteReservation(reservationId: Int) {
        viewModelScope.launch {
            reservationRepository.delete(reservationId)
        }
    }
}

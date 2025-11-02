package com.appecoviaje.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appecoviaje.data.Reservation
import com.appecoviaje.data.UserPreferencesRepository
import com.appecoviaje.viewmodel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun ReservationScreen(
    reservationViewModel: ReservationViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val reservations by reservationViewModel.reservations.collectAsState()
    val trips by reservationViewModel.trips.collectAsState()
    val context = LocalContext.current
    val userPreferencesRepository = UserPreferencesRepository(context.dataStore)
    var selectedTripId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Reservas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedTripId?.let { tripId ->
                    runBlocking {
                        val userId = userPreferencesRepository.userToken.first()?.toIntOrNull()
                        if (userId != null) {
                            val newReservation = Reservation(
                                tripId = tripId,
                                userId = userId,
                                reservationDate = System.currentTimeMillis()
                            )
                            reservationViewModel.addReservation(newReservation)
                        }
                    }
                }
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Dropdown for trip selection
            if (trips.isNotEmpty()) {
                selectedTripId = trips.first().id
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = {}
                ) {
                    TextField(
                        value = trips.find { it.id == selectedTripId }?.title ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select a trip") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = false,
                        onDismissRequest = {}
                    ) {
                        trips.forEach { trip ->
                            DropdownMenuItem(
                                text = { Text(trip.title) },
                                onClick = { selectedTripId = trip.id }
                            )
                        }
                    }
                }
            }

            LazyColumn {
                items(reservations) { reservation ->
                    ReservationItem(reservation = reservation) {
                        reservationViewModel.deleteReservation(reservation.id)
                    }
                }
            }
        }
    }
}

@Composable
fun ReservationItem(reservation: Reservation, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Reserva #${reservation.id}")
                Text(text = "Viaje #${reservation.tripId}")
                Text(text = "Fecha: ${formatDate(reservation.reservationDate)}")
            }
            Button(onClick = onDelete) {
                Text("Eliminar")
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

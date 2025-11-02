package com.appecoviaje.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appecoviaje.data.Reservation
import com.appecoviaje.viewmodel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    navController: NavController,
    reservationViewModel: ReservationViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val reservations by reservationViewModel.reservations.collectAsState()
    val trips by reservationViewModel.trips.collectAsState()
    var selectedTripId by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Reservas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedTripId?.let { tripId ->
                    reservationViewModel.addReservation(tripId)
                }
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (trips.isEmpty()) {
                Text("No hay viajes disponibles para reservar.")
            } else {
                if (selectedTripId == null) {
                    selectedTripId = trips.firstOrNull()?.id
                }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = trips.find { it.id == selectedTripId }?.title ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select a trip") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        trips.forEach { trip ->
                            DropdownMenuItem(
                                text = { Text(trip.title) },
                                onClick = {
                                    selectedTripId = trip.id
                                    expanded = false
                                }
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

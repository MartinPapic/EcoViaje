package com.appecoviaje.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appecoviaje.data.Reservation
import com.appecoviaje.viewmodel.ReservationViewModel
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    reservationViewModel: ReservationViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val reservations by reservationViewModel.reservations.collectAsState()
    val trips by reservationViewModel.trips.collectAsState()
    var selectedTripId by remember { mutableStateOf<Int?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Reservas") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedTripId?.let { tripId ->
                        reservationViewModel.addReservationForTrip(tripId)
                    }
                }
            ) { Text("+") }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // Trip selection dropdown
            if (trips.isNotEmpty()) {
                selectedTripId = selectedTripId ?: trips.first().id
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    TextField(
                        value = trips.find { it.id == selectedTripId }?.title ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Selecciona un viaje") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        trips.forEach { trip ->
                            DropdownMenuItem(
                                text = { Text(trip.title) },
                                onClick = {
                                    selectedTripId = trip.id
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Reservations list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(reservations) { reservation ->
                    ReservationItem(reservation) {
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
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Reserva #${reservation.id}")
                Text("Viaje #${reservation.tripId}")
                Text("Fecha: ${formatDate(reservation.reservationDate)}")
            }
            Button(onClick = onDelete) { Text("Eliminar") }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

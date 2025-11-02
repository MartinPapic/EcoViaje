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
<<<<<<< HEAD
import androidx.compose.ui.platform.LocalContext
=======
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
>>>>>>> ccb8eb34d0ac4c92d9550c9ef38bc4cf798e4c17

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    reservationViewModel: ReservationViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val reservations by reservationViewModel.reservations.collectAsState()
    val trips by reservationViewModel.trips.collectAsState()
<<<<<<< HEAD
    var selectedTripId by remember { mutableStateOf<Int?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }
=======
    val context = LocalContext.current
    val userPreferencesRepository = UserPreferencesRepository(context.dataStore)
    val coroutineScope = rememberCoroutineScope()
    var selectedTripId by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }
>>>>>>> ccb8eb34d0ac4c92d9550c9ef38bc4cf798e4c17

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Reservas") }) },
        floatingActionButton = {
<<<<<<< HEAD
            FloatingActionButton(
                onClick = {
                    selectedTripId?.let { tripId ->
                        reservationViewModel.addReservationForTrip(tripId)
=======
            FloatingActionButton(onClick = {
                selectedTripId?.let { tripId ->
                    coroutineScope.launch {
                        val userId = userPreferencesRepository.userToken.first()?.toIntOrNull()
                        if (userId != null) {
                            val newReservation = Reservation(
                                tripId = tripId,
                                userId = userId,
                                reservationDate = System.currentTimeMillis()
                            )
                            reservationViewModel.addReservation(newReservation)
                        }
>>>>>>> ccb8eb34d0ac4c92d9550c9ef38bc4cf798e4c17
                    }
                }
            ) { Text("+") }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
<<<<<<< HEAD

            // Trip selection dropdown
            if (trips.isNotEmpty()) {
                selectedTripId = selectedTripId ?: trips.first().id
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
=======
            if (trips.isNotEmpty()) {
                if (selectedTripId == null) {
                    selectedTripId = trips.first().id
                }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
>>>>>>> ccb8eb34d0ac4c92d9550c9ef38bc4cf798e4c17
                ) {
                    TextField(
                        value = trips.find { it.id == selectedTripId }?.title ?: "",
                        onValueChange = {},
                        readOnly = true,
<<<<<<< HEAD
                        label = { Text("Selecciona un viaje") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
=======
                        label = { Text("Select a trip") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
>>>>>>> ccb8eb34d0ac4c92d9550c9ef38bc4cf798e4c17
                    ) {
                        trips.forEach { trip ->
                            DropdownMenuItem(
                                text = { Text(trip.title) },
                                onClick = {
                                    selectedTripId = trip.id
<<<<<<< HEAD
                                    dropdownExpanded = false
=======
                                    expanded = false
>>>>>>> ccb8eb34d0ac4c92d9550c9ef38bc4cf798e4c17
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

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
import com.appecoviaje.data.Experience
import com.appecoviaje.data.UserPreferencesRepository
import com.appecoviaje.viewmodel.ExperienceViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceScreen(
    experienceViewModel: ExperienceViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val experiences by experienceViewModel.experiences.collectAsState()
    val trips by experienceViewModel.trips.collectAsState()
    val selectedTripId by experienceViewModel.selectedTripId.collectAsState()
    val context = LocalContext.current
    val userPreferencesRepository = UserPreferencesRepository(context.dataStore)
    val coroutineScope = rememberCoroutineScope()

    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0f) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Intercambio de Experiencias") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (trips.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    val selectedTripTitle = trips.find { it.id == selectedTripId }?.title ?: "Selecciona un viaje"
                    TextField(
                        value = selectedTripTitle,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Viaje") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                                    experienceViewModel.setSelectedTripId(trip.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTripId != null) {
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Comentario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Slider(
                    value = rating,
                    onValueChange = { rating = it },
                    valueRange = 0f..5f,
                    steps = 4
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val userId = userPreferencesRepository.userToken.first()?.toIntOrNull()
                            if (userId != null && selectedTripId != null) {
                                val newExperience = Experience(
                                    tripId = selectedTripId!!,
                                    userId = userId,
                                    rating = rating,
                                    comment = comment
                                )
                                experienceViewModel.addExperience(newExperience)
                                // Reset form
                                comment = ""
                                rating = 0f
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Compartir Experiencia")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(experiences) { experience ->
                    ExperienceItem(experience = experience) {
                        experienceViewModel.deleteExperience(experience.id)
                    }
                }
            }
        }
    }
}

@Composable
fun ExperienceItem(experience: Experience, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Usuario #${experience.userId}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Calificación: ${experience.rating}/5")
            Text(text = experience.comment)
            Button(onClick = onDelete) {
                Text("Eliminar")
            }
        }
    }
}

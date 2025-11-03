package com.appecoviaje.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.appecoviaje.data.Experience
import com.appecoviaje.viewmodel.ExperienceViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceScreen(
    navController: NavController,
    experienceViewModel: ExperienceViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val experiences by experienceViewModel.experiences.collectAsState()
    val trips by experienceViewModel.trips.collectAsState()
    val selectedTripId by experienceViewModel.selectedTripId.collectAsState()

    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0f) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val file = File(context.cacheDir, "camera_photo.jpg")
    val cameraImageUri = FileProvider.getUriForFile(context, "com.appecoviaje.fileprovider", file)

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri = cameraImageUri
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Intercambio de Experiencias") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Volver", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (trips.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    val selectedTripTitle =
                        trips.find { it.id == selectedTripId }?.title ?: "Selecciona un viaje"
                    TextField(
                        value = selectedTripTitle,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Viaje") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
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
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = rating,
                    onValueChange = { rating = it },
                    valueRange = 0f..5f,
                    steps = 4
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Button(onClick = { cameraLauncher.launch(cameraImageUri) }) {
                        Text("Tomar Foto")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { galleryLauncher.launch("image/*") }) {
                        Text("Galería")
                    }
                }

                imageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Selected image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        experienceViewModel.addExperience(comment, rating, imageUri?.toString())
                        comment = ""
                        rating = 0f
                        imageUri = null
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
            Text(
                text = "Usuario #${experience.userId}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Calificación: ${experience.rating}/5")
            Text(text = experience.comment)

            experience.photoUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Experience image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Button(onClick = onDelete) {
                Text("Eliminar")
            }
        }
    }
}

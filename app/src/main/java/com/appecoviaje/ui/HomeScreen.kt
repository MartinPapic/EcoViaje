package com.appecoviaje.ui // ✅ el mismo namespace del proyecto

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.* // ✅ import correcto para Column, padding, fillMaxSize, etc.
import androidx.compose.material3.* // ✅ para Scaffold, TopAppBar, Button, Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appecoviaje.R // ✅ usa tu paquete correcto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // 🧭 Estructura principal de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AppEcoViaje") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        // 📐 Contenedor principal con espaciado y padding
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp), // ✅ Espaciado uniforme
            horizontalAlignment = Alignment.CenterHorizontally // ✅ Centrado horizontal
        ) {
            // 📝 Texto de bienvenida
            Text(
                text = "¡Bienvenido!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // 🔘 Botón con color del tema
            Button(
                onClick = { /* Acción futura */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Presióname")
            }

            // 🖼️ Imagen del logo
            Image(
                painter = painterResource(id = R.drawable.ecoviajelogo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )

            // ➕ Elemento extra para probar: texto adicional
            Text(
                text = "Explora nuestros destinos ecológicos 🌿",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            // ➕ Otro elemento extra: botón secundario
            OutlinedButton(onClick = { /* Navegar a otra vista */ }) {
                Text("Ver destinos")
            }
        }
    }
}

// 👀 Vista previa en Android Studio
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}

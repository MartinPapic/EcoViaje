package com.appecoviaje.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("planning") { TripPlanningScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("experiences") { ExperienceScreen() }
        composable("reservations") { ReservationScreen() }
    }
}

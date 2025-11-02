package com.appecoviaje.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Make sure you have the correct imports for all your screens
import com.appecoviaje.ui.LoginScreen
import com.appecoviaje.ui.RegistrationScreen
import com.appecoviaje.ui.HomeScreen
import com.appecoviaje.ui.TripPlanningScreen
import com.appecoviaje.ui.SettingsScreen
import com.appecoviaje.ui.ExperienceScreen
import com.appecoviaje.ui.ReservationScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController)
        }

        composable("registration") {
            RegistrationScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("planning") {
            TripPlanningScreen(navController)
        }

        composable("settings") {
            SettingsScreen(navController)
        }

        composable("experiences") {
            ExperienceScreen()
        }

        // Fully qualified to avoid ambiguity
        composable("reservations") {
            com.appecoviaje.ui.ReservationScreen()
        }
    }
}

package com.appecoviaje.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appecoviaje.data.AppDatabase
import com.appecoviaje.data.UserPreferencesRepository
import com.appecoviaje.data.TripRepository
import com.appecoviaje.viewmodel.LoginViewModel
import com.appecoviaje.viewmodel.RegistrationViewModel
import com.appecoviaje.viewmodel.SettingsViewModel
import com.appecoviaje.viewmodel.TripPlanningViewModel
import java.lang.IllegalArgumentException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val userPreferencesRepository = UserPreferencesRepository(context.dataStore)
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userPreferencesRepository) as T
        }
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            val userPreferencesRepository = UserPreferencesRepository(context.dataStore)
            @Suppress("UNCHECKED_CAST")
            return RegistrationViewModel(userPreferencesRepository) as T
        }
        if (modelClass.isAssignableFrom(TripPlanningViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context)
            val tripRepository = TripRepository(database.tripDao())
            @Suppress("UNCHECKED_CAST")
            return TripPlanningViewModel(tripRepository) as T
        }
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            val userPreferencesRepository = UserPreferencesRepository(context.dataStore)
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.appecoviaje.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appecoviaje.data.UserPreferencesRepository
import com.appecoviaje.viewmodel.LoginViewModel
import com.appecoviaje.viewmodel.RegistrationViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val repository = UserPreferencesRepository(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(repository) as T
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) ->
                RegistrationViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

package com.appecoviaje.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appecoviaje.data.UserPreferencesRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(UserPreferencesRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

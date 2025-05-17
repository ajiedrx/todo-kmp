package com.adr.todo.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class NavigationManager {
    sealed class Destination {
        data class TodoDetail(val todoId: Long) : Destination()
    }

    private val _currentDestination = mutableStateOf<Destination?>(null)
    val currentDestination: State<Destination?> = _currentDestination

    fun navigateTo(destination: Destination) {
        _currentDestination.value = destination
    }

    fun clearDestination() {
        _currentDestination.value = null
    }

    companion object {
        val instance = NavigationManager()
    }
}
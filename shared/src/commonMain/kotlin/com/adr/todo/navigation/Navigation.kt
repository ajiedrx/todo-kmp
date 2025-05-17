package com.adr.todo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.adr.todo.presentation.ui.detail.DetailScreen
import com.adr.todo.presentation.ui.detail.DetailViewModel
import com.adr.todo.presentation.ui.history.HistoryScreen
import com.adr.todo.presentation.ui.history.HistoryViewModel
import com.adr.todo.presentation.ui.main.MainScreen
import com.adr.todo.presentation.ui.main.MainViewModel
import org.koin.compose.koinInject

sealed class Screen {
    data object Main : Screen()
    data class Detail(val todoId: Long) : Screen()
    data object Create : Screen()
    data object History : Screen()
}

@Composable
fun AppNavigation(initialTodoId: Long? = null) {
    var currentScreen by remember {
        mutableStateOf<Screen>(
            if (initialTodoId != null) Screen.Detail(initialTodoId) else Screen.Main
        )
    }

    val mainViewModel = koinInject<MainViewModel>()
    val detailViewModel = koinInject<DetailViewModel>()
    val historyViewModel = koinInject<HistoryViewModel>()

    val navigationDestination by NavigationManager.instance.currentDestination

    LaunchedEffect(navigationDestination) {
        navigationDestination?.let { destination ->
            when (destination) {
                is NavigationManager.Destination.TodoDetail -> {
                    detailViewModel.loadTodo(destination.todoId)
                    currentScreen = Screen.Detail(destination.todoId)
                    NavigationManager.instance.clearDestination()
                }
            }
        }
    }

    when (val screen = currentScreen) {
        is Screen.Main -> {
            MainScreen(
                viewModel = mainViewModel,
                onNavigateToDetail = { todo ->
                    detailViewModel.loadTodo(todo.id)
                    currentScreen = Screen.Detail(todo.id)
                },
                onNavigateToCreate = {
                    detailViewModel.startCreateMode()
                    currentScreen = Screen.Create
                },
                onNavigateToHistory = {
                    currentScreen = Screen.History
                }
            )
        }

        is Screen.Detail -> {
            LaunchedEffect(screen.todoId) {
                detailViewModel.loadTodo(screen.todoId)
            }

            DetailScreen(
                viewModel = detailViewModel,
                onNavigateBack = {
                    currentScreen = Screen.Main
                },
                onNavigateBackAfterDelete = {
                    currentScreen = Screen.Main
                }
            )
        }

        is Screen.Create -> {
            DetailScreen(
                viewModel = detailViewModel,
                onNavigateBack = {
                    currentScreen = Screen.Main
                },
                onNavigateBackAfterDelete = {
                    currentScreen = Screen.Main
                }
            )
        }

        is Screen.History -> {
            HistoryScreen(
                viewModel = historyViewModel,
                onNavigateBack = {
                    currentScreen = Screen.Main
                },
                onNavigateToCreate = { todo ->
                    detailViewModel.restoreFromHistory(todo)
                    currentScreen = Screen.Create
                }
            )
        }
    }
}
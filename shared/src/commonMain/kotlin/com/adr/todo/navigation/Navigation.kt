package com.adr.todo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.adr.todo.ContextFactory
import com.adr.todo.presentation.ui.detail.DetailScreen
import com.adr.todo.presentation.ui.detail.DetailViewModel
import com.adr.todo.presentation.ui.history.HistoryScreen
import com.adr.todo.presentation.ui.history.HistoryViewModel
import com.adr.todo.presentation.ui.main.MainScreen
import com.adr.todo.presentation.ui.main.MainViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

sealed class Screen {
    @Serializable
    data object Main : Screen()

    @Serializable
    data class Detail(val todoId: Long? = null) : Screen()

    @Serializable
    data object History : Screen()
}

@Composable
fun AppNavigation(platformContext: ContextFactory, initialTodoId: Long? = null) {
    val navController: NavHostController = rememberNavController()

    val mainViewModel = koinInject<MainViewModel>()
    val detailViewModel = koinInject<DetailViewModel>()
    val historyViewModel = koinInject<HistoryViewModel>()

    NavHost(
        navController = navController,
        startDestination =
            if (initialTodoId != null) Screen.Detail(todoId = initialTodoId)
            else Screen.Main
    ) {
        composable<Screen.Main> {
            MainScreen(
                viewModel = mainViewModel,
                onNavigateToDetail = { todo ->
                    detailViewModel.loadTodo(todo.id)
                    navController.navigate(Screen.Detail(todoId = todo.id))
                },
                onNavigateToCreate = {
                    detailViewModel.startCreateMode()
                    navController.navigate(Screen.Detail())
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History)
                }
            )
        }
        composable<Screen.Detail> { navBackStackEntry ->
            val todoId = navBackStackEntry.toRoute<Screen.Detail>().todoId

            todoId?.let {
                detailViewModel.loadTodo(it)
            }

            DetailScreen(
                contextFactory = platformContext,
                viewModel = detailViewModel,
                onNavigateBack = {
                    navController.navigate(Screen.Main)
                },
                onNavigateBackAfterDelete = {
                    navController.navigate(Screen.Main)
                }
            )
        }
        composable<Screen.History> {
            HistoryScreen(
                viewModel = historyViewModel,
                onNavigateBack = {
                    navController.navigate(Screen.Main)
                },
                onNavigateToCreate = { todo ->
                    detailViewModel.restoreFromHistory(todo)
                    navController.navigate(Screen.Detail())
                }
            )
        }
    }
}
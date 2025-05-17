package com.adr.todo.app

import androidx.compose.runtime.Composable
import com.adr.todo.di.appModule
import com.adr.todo.navigation.AppNavigation
import com.adr.todo.presentation.theme.AppTheme
import org.koin.compose.KoinApplication

@Composable
fun App(initialTodoId: Long? = null) {
    KoinApplication(application = {
        modules(appModule)
    }) {
        AppTheme {
            AppNavigation(initialTodoId)
        }
    }
}
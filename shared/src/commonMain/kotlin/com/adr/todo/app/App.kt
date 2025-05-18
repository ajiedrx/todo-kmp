package com.adr.todo.app

import androidx.compose.runtime.Composable
import com.adr.todo.ContextFactory
import com.adr.todo.navigation.AppNavigation
import com.adr.todo.presentation.theme.AppTheme

@Composable
fun App(platformContext: ContextFactory, initialTodoId: Long? = null) {
    AppTheme {
        AppNavigation(platformContext, initialTodoId)
    }
}
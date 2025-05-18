package com.adr.todo.app

import androidx.compose.ui.window.ComposeUIViewController
import com.adr.todo.ContextFactory
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    val appDelegate = (UIApplication.sharedApplication.delegate as? AppDelegate)
    val initialTodoId = appDelegate?.getInitialTodoId()

    return ComposeUIViewController {
        App(ContextFactory(), initialTodoId)
    }
}
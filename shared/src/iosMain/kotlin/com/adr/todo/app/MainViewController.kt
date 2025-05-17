package com.adr.todo.app

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    val appDelegate = (UIApplication.sharedApplication.delegate as? AppDelegate)
    val initialTodoId = appDelegate?.getInitialTodoId()

    return ComposeUIViewController {
        App(initialTodoId)
    }
}
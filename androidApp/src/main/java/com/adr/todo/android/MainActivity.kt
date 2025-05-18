package com.adr.todo.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.adr.todo.ContextFactory
import com.adr.todo.app.App
import com.adr.todo.util.Constants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialTodoId = intent?.extras?.getLong(Constants.EXTRA_TODO_ID)

        setContent {
            App(ContextFactory(this@MainActivity), initialTodoId)
        }
    }
}
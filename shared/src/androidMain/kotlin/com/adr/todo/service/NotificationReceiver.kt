package com.adr.todo.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.adr.todo.domain.usecase.TodoUseCases
import com.adr.todo.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationReceiver : BroadcastReceiver(), KoinComponent {
    private val todoUseCases: TodoUseCases by inject()
    private val notificationService: NotificationService by inject()

    override fun onReceive(context: Context, intent: Intent) {
        val todoId = intent.getLongExtra(Constants.EXTRA_TODO_ID, -1)
        if (todoId == -1L) return

        // Show the notification
        CoroutineScope(Dispatchers.IO).launch {
            val todo = todoUseCases.getTodoDetail.execute(todoId)
            if (todo != null) {
                notificationService.showNotification(todo)
            }
        }
    }
}
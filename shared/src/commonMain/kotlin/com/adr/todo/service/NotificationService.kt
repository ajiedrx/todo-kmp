package com.adr.todo.service

import com.adr.todo.domain.model.Todo

expect class NotificationService {
    fun scheduleNotification(todo: Todo)
    fun cancelNotification(todoId: Long)
    fun showNotification(todo: Todo)
}
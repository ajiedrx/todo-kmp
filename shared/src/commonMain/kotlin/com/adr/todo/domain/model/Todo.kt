package com.adr.todo.domain.model

import kotlinx.datetime.Instant

data class Todo(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val dueDateTime: Instant? = null,
    val isCompleted: Boolean = false,
    val reminderDateTime: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)
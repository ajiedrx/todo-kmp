package com.adr.todo.data.model

import kotlinx.datetime.Instant

@Entity(tableName = "todos")
@TypeConverters(InstantConverter::class)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val dueDateTime: Instant? = null,
    val isCompleted: Boolean = false,
    val reminderDateTime: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)
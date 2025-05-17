package com.adr.todo.data.mapper

import com.adr.todo.data.model.TodoEntity
import com.adr.todo.domain.model.Todo

fun Todo.toEntity(): TodoEntity {
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        dueDateTime = dueDateTime,
        isCompleted = isCompleted,
        reminderDateTime = reminderDateTime,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun TodoEntity.toDomain(): Todo {
    return Todo(
        id = id,
        title = title,
        description = description,
        dueDateTime = dueDateTime,
        isCompleted = isCompleted,
        reminderDateTime = reminderDateTime,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
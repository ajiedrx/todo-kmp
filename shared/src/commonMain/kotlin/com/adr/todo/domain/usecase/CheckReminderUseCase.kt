package com.adr.todo.domain.usecase

import com.adr.todo.data.TodoRepository
import com.adr.todo.domain.model.Todo
import kotlinx.datetime.Clock

class CheckRemindersUseCase(private val repository: TodoRepository) {
    suspend fun execute(): List<Todo> {
        val currentTime = Clock.System.now()
        return repository.getTodosWithDueReminders(currentTime)
    }
}
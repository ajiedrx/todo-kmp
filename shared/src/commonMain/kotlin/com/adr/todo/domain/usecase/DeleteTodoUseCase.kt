package com.adr.todo.domain.usecase

import com.adr.todo.data.TodoRepository

class DeleteTodoUseCase(private val repository: TodoRepository) {
    suspend fun execute(id: Long) = repository.deleteTodo(id)
}
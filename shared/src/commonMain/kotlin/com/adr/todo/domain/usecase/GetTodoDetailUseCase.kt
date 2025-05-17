package com.adr.todo.domain.usecase

import com.adr.todo.data.repository.TodoRepository
import com.adr.todo.domain.model.Todo

class GetTodoDetailUseCase(private val repository: TodoRepository) {
    suspend fun execute(id: Long): Todo? = repository.getTodoById(id)
}
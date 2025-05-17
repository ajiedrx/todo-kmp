package com.adr.todo.domain.usecase

import com.adr.todo.data.repository.TodoRepository
import com.adr.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

class GetCompletedTodosUseCase(private val repository: TodoRepository) {
    suspend fun execute(): Flow<List<Todo>> {
        return repository.observeCompletedTodos()
    }
}
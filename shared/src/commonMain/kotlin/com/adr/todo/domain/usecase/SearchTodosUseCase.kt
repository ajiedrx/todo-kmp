package com.adr.todo.domain.usecase

import com.adr.todo.data.repository.TodoRepository
import com.adr.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

class SearchTodosUseCase(private val repository: TodoRepository) {
    fun execute(query: String): Flow<List<Todo>> = repository.searchTodos(query)
}
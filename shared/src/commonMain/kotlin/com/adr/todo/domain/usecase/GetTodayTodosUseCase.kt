package com.adr.todo.domain.usecase

import com.adr.todo.data.repository.TodoRepository
import com.adr.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

class GetTodayTodosUseCase(private val repository: TodoRepository) {
    fun execute(): Flow<List<Todo>> = repository.observeTodayTodos()
}
package com.adr.todo.domain.usecase

import com.adr.todo.data.TodoRepository
import com.adr.todo.domain.model.Todo

class SaveTodoUseCase(private val repository: TodoRepository) {
    suspend fun execute(todo: Todo): Long {
        return if (todo.id == 0L) {
            repository.insertTodo(todo)
        } else {
            repository.updateTodo(todo)
            todo.id
        }
    }
}
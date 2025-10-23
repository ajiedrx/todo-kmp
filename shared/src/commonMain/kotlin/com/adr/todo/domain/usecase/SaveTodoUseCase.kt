package com.adr.todo.domain.usecase

import com.adr.todo.data.repository.TodoRepository
import com.adr.todo.domain.model.Todo

class SaveTodoUseCase(private val repository: TodoRepository) {
    suspend fun execute(todo: Todo): Long {
        val existingTodo = repository.getTodoById(todo.id)

        return if (existingTodo == null) {
            repository.insertTodo(todo)
        } else {
            repository.updateTodo(todo)
            todo.id
        }
    }
}
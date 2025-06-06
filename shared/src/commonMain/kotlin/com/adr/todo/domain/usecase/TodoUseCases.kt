package com.adr.todo.domain.usecase

import com.adr.todo.data.repository.TodoRepository

class TodoUseCases(repository: TodoRepository) {
    val getTodayTodos = GetTodayTodosUseCase(repository)
    val getLaterTodos = GetLaterTodosUseCase(repository)
    val getCompletedTodos = GetCompletedTodosUseCase(repository)
    val searchTodos = SearchTodosUseCase(repository)
    val saveTodo = SaveTodoUseCase(repository)
    val deleteTodo = DeleteTodoUseCase(repository)
    val getTodoDetail = GetTodoDetailUseCase(repository)
    val checkReminders = CheckRemindersUseCase(repository)
}
package com.adr.todo.data.repository

import com.adr.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface TodoRepository {
    suspend fun getTodoById(id: Long): Todo?
    fun observeTodayTodos(): Flow<List<Todo>>
    fun observeLaterTodos(): Flow<List<Todo>>
    fun observeCompletedTodos(): Flow<List<Todo>>
    fun searchTodos(query: String): Flow<List<Todo>>
    suspend fun insertTodo(todo: Todo): Long
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteTodo(id: Long)
    suspend fun getTodosWithDueReminders(currentTime: Instant): List<Todo>
}
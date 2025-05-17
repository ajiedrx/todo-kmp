package com.adr.todo.data.repository

import com.adr.todo.data.db.dao.TodoDao
import com.adr.todo.data.mapper.toDomain
import com.adr.todo.data.mapper.toEntity
import com.adr.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

class TodoRepositoryImpl(
    private val todoDao: TodoDao,
) : TodoRepository {

    override suspend fun getTodoById(id: Long): Todo? {
        return todoDao.getTodoById(id)?.toDomain()
    }

    override fun observeTodayTodos(): Flow<List<Todo>> {
        return todoDao.observeTodayTodos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeLaterTodos(): Flow<List<Todo>> {
        return todoDao.observeLaterTodos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchTodos(query: String): Flow<List<Todo>> {
        return todoDao.searchTodos(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertTodo(todo: Todo): Long {
        return todoDao.insertTodo(todo.toEntity())
    }

    override suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo.toEntity())
    }

    override suspend fun deleteTodo(id: Long) {
        todoDao.deleteTodo(id)
    }

    override suspend fun getTodosWithDueReminders(currentTime: Instant): List<Todo> {
        return todoDao.getTodosWithDueReminders(currentTime).map { it.toDomain() }
    }
}
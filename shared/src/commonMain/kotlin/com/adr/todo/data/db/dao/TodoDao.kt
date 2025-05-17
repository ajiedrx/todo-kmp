package com.adr.todo.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.adr.todo.data.db.data_converter.InstantConverter
import com.adr.todo.data.model.TodoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
@TypeConverters(InstantConverter::class)
interface TodoDao {
    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Long): TodoEntity?

    @Query("SELECT * FROM todos WHERE dueDateTime IS NOT NULL AND date(dueDateTime) = date('now') AND isCompleted = 0 ORDER BY dueDateTime ASC")
    fun observeTodayTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE dueDateTime IS NOT NULL AND date(dueDateTime) > date('now') AND isCompleted = 0 ORDER BY dueDateTime ASC")
    fun observeLaterTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE isCompleted = 1 ORDER BY dueDateTime ASC")
    fun observeCompletedTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY dueDateTime ASC")
    fun searchTodos(query: String): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity): Long

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTodo(id: Long)

    @Query("SELECT * FROM todos WHERE reminderDateTime IS NOT NULL AND reminderDateTime <= :currentTime AND isCompleted = 0")
    suspend fun getTodosWithDueReminders(currentTime: Instant): List<TodoEntity>
}
package com.adr.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.adr.todo.data.db.data_converter.InstantConverter
import kotlinx.datetime.Instant

@Entity(tableName = "todos")
@TypeConverters(InstantConverter::class)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val dueDateTime: Instant? = null,
    val isCompleted: Boolean = false,
    val reminderDateTime: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)
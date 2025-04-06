package com.adr.todo.domain.model

import kotlinx.datetime.Instant

data class Todo(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val dueDateTime: Instant? = null,
    val isCompleted: Boolean = false,
    val reminderDateTime: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class ReminderOption(val displayName: String, val timeBeforeDueInHours: Int?) {
    THREE_DAYS_BEFORE("3 days before", 72),
    ONE_DAY_BEFORE("1 day before", 24),
    SIX_HOURS_BEFORE("6 hours before", 6),
    THREE_HOURS_BEFORE("3 hours before", 3),
    ONE_HOUR_BEFORE("1 hour before", 1),
    CUSTOM("Custom", null); // For custom date and time
}
package com.adr.todo.util

import com.adr.todo.ContextFactory
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

expect class DateTimeWrapper {
    fun showDatePicker(context: ContextFactory, onDateSelected: (LocalDate?) -> Unit)
    fun showTimePicker(context: ContextFactory, onTimeSelected: (LocalTime?) -> Unit)
    fun getDate(): LocalDate
    fun getTime(): LocalTime
    fun combineDateTime(date: LocalDate, time: LocalTime): Instant

    companion object {
        fun fromInstant(instant: Instant): DateTimeWrapper
    }
}
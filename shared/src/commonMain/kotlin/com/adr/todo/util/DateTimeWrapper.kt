package com.adr.todo.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

expect class DateTimeWrapper {
    fun showDatePicker(onDateSelected: (LocalDate?) -> Unit)
    fun showTimePicker(onTimeSelected: (LocalTime?) -> Unit)
    fun getDate(): LocalDate
    fun getTime(): LocalTime
    fun combineDateTime(date: LocalDate, time: LocalTime): Instant

    companion object {
        fun fromInstant(instant: Instant): DateTimeWrapper
    }
}
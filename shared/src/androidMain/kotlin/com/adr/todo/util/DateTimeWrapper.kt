package com.adr.todo.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

actual class DateTimeWrapper private constructor(
    private val calendar: Calendar,
) {
    private val context: Context by inject()

    actual fun showDatePicker(onDateSelected: (LocalDate?) -> Unit) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selectedDate = LocalDate(year, month + 1, day)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnCancelListener { onDateSelected(null) }
        }.show()
    }

    actual fun showTimePicker(onTimeSelected: (LocalTime?) -> Unit) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val selectedTime = LocalTime(hour, minute)
                onTimeSelected(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).apply {
            setOnCancelListener { onTimeSelected(null) }
        }.show()
    }

    actual fun getDate(): LocalDate = LocalDate(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    actual fun getTime(): LocalTime = LocalTime(
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE)
    )

    actual fun combineDateTime(date: LocalDate, time: LocalTime): Instant {
        val localDateTime = LocalDateTime(
            date.year,
            date.monthNumber,
            date.dayOfMonth,
            time.hour,
            time.minute
        )
        return localDateTime.toInstant(TimeZone.currentSystemDefault())
    }

    actual companion object : KoinComponent {
        actual fun fromInstant(instant: Instant): DateTimeWrapper {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = instant.toEpochMilliseconds()
            return DateTimeWrapper(calendar)
        }

        fun createPlatformInstance(): DateTimeWrapper {
            return DateTimeWrapper(Calendar.getInstance())
        }
    }
}
package com.adr.todo.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

object DateTimeFormatter {
    fun formatDate(instant: Instant): String {
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = when (localDateTime.month.number) {
            1 -> Constants.JAN
            2 -> Constants.FEB
            3 -> Constants.MAR
            4 -> Constants.APR
            5 -> Constants.MAY
            6 -> Constants.JUN
            7 -> Constants.JUL
            8 -> Constants.AUG
            9 -> Constants.SEP
            10 -> Constants.OCT
            11 -> Constants.NOV
            12 -> Constants.DEC
            else -> Constants.MONTH_UNKNOWN
        }
        return "$month ${localDateTime.dayOfMonth}${Constants.COMMA}${Constants.SPACE}${localDateTime.year}"
    }

    fun formatTime(instant: Instant): String {
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val hour = when {
            localDateTime.hour == 0 -> 12
            localDateTime.hour > 12 -> localDateTime.hour - 12
            else -> localDateTime.hour
        }
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val amPm = if (localDateTime.hour < 12) Constants.AM else Constants.PM
        return "$hour${Constants.COLON}$minute $amPm"
    }

    fun formatDateTime(instant: Instant): String {
        return "${formatDate(instant)} ${formatTime(instant)}"
    }

    fun getGreeting(): String {
        val now = Clock.System.now()
        val hour = now.toLocalDateTime(TimeZone.currentSystemDefault()).hour

        return when {
            hour < 12 -> Constants.GREETING_MORNING
            hour < 18 -> Constants.GREETING_AFTERNOON
            else -> Constants.GREETING_EVENING
        }
    }

    fun formatDateRelativeToToday(instant: Instant): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

        return when {
            date == today -> Constants.TODAY
            date == today.plus(1, DateTimeUnit.DAY) -> Constants.TOMORROW
            date == today.minus(1, DateTimeUnit.DAY) -> Constants.YESTERDAY
            else -> formatDate(instant)
        }
    }

    fun getGroupDate(instant: Instant): String {
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        return when {
            localDate == today -> Constants.TODAY
            localDate == today.minus(1, DateTimeUnit.DAY) -> Constants.YESTERDAY
            localDate > today.minus(7, DateTimeUnit.DAY) -> Constants.LAST_7_DAYS
            localDate > today.minus(30, DateTimeUnit.DAY) -> Constants.LAST_30_DAYS
            localDate.year == today.year -> Constants.EARLIER_THIS_YEAR
            else -> Constants.OLDER
        }
    }
}
package com.adr.todo.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adr.todo.domain.model.ReminderOption
import com.adr.todo.util.Constants
import com.adr.todo.util.DateTimeFormatter
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.minus

@Composable
fun ReminderOptionPicker(
    dueDateTime: Instant?,
    currentReminderDateTime: Instant?,
    onReminderDateTimeSelected: (Instant?) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<ReminderOption?>(null) }
    var customDateTime by remember { mutableStateOf<Instant?>(currentReminderDateTime) }

    LaunchedEffect(dueDateTime, currentReminderDateTime) {
        if (dueDateTime != null && currentReminderDateTime != null) {
            val diffInHours =
                (dueDateTime.toEpochMilliseconds() - currentReminderDateTime.toEpochMilliseconds()) / (1000 * 60 * 60)
            selectedOption =
                ReminderOption.entries.find { it.timeBeforeDueInHours == diffInHours.toInt() }
                    ?: ReminderOption.CUSTOM
        } else {
            selectedOption = null
            customDateTime = null
        }
    }

    Column(modifier = modifier) {
        Text(
            text = Constants.REMINDER_LABEL,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(Constants.SMALL_PADDING.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = dueDateTime != null && isEnabled,
                    onClick = { expanded = true }
                ),
            color = if (dueDateTime == null)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = Constants.SURFACE_VARIANT_LOW_ALPHA)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = Constants.SURFACE_VARIANT_MEDIUM_ALPHA),
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.LARGE_PADDING.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        dueDateTime == null -> Constants.SET_DUE_DATE_FIRST
                        selectedOption == null -> Constants.NO_REMINDER
                        selectedOption == ReminderOption.CUSTOM && customDateTime != null ->
                            "${Constants.CUSTOM_PREFIX}${
                                DateTimeFormatter.formatDateTime(
                                    customDateTime!!
                                )
                            }"

                        selectedOption != null -> selectedOption!!.displayName
                        else -> Constants.SELECT_REMINDER
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (dueDateTime == null)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                if (dueDateTime != null) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = Constants.SELECT_REMINDER
                    )
                }
            }
        }

        if (selectedOption != null) {
            TextButton(
                onClick = {
                    selectedOption = null
                    customDateTime = null
                    onReminderDateTimeSelected(null)
                },
                contentPadding = PaddingValues(horizontal = Constants.MEDIUM_PADDING.dp),
                enabled = isEnabled
            ) {
                Text(Constants.CLEAR)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(280.dp)
        ) {
            DropdownMenuItem(
                text = { Text(Constants.NO_REMINDER) },
                onClick = {
                    selectedOption = null
                    customDateTime = null
                    onReminderDateTimeSelected(null)
                    expanded = false
                }
            )

            ReminderOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    onClick = {
                        selectedOption = option

                        if (option != ReminderOption.CUSTOM && dueDateTime != null && option.timeBeforeDueInHours != null) {
                            val reminderDateTime = dueDateTime.minus(
                                option.timeBeforeDueInHours.toLong(),
                                DateTimeUnit.HOUR
                            )
                            customDateTime = reminderDateTime
                            onReminderDateTimeSelected(reminderDateTime)
                        } else if (option == ReminderOption.CUSTOM) {
                            expanded = false
                            if (customDateTime == null && dueDateTime != null) {
                                customDateTime = dueDateTime.minus(1, DateTimeUnit.HOUR)
                                onReminderDateTimeSelected(customDateTime)
                            }
                        }

                        expanded = false
                    }
                )
            }
        }

        if (selectedOption == ReminderOption.CUSTOM && dueDateTime != null) {
            Spacer(Modifier.height(Constants.MEDIUM_PADDING.dp))
            DateTimePicker(
                dateTime = customDateTime,
                onDateTimeSelected = {
                    customDateTime = it
                    onReminderDateTimeSelected(it)
                },
                label = Constants.CUSTOM_REMINDER,
                isEnabled = isEnabled
            )
        }
    }
}
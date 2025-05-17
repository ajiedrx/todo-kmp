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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adr.todo.util.Constants
import com.adr.todo.util.DateTimeFormatter
import com.adr.todo.util.DateTimeWrapper
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Composable
fun DateTimePicker(
    dateTime: Instant?,
    onDateTimeSelected: (Instant?) -> Unit,
    label: String,
    isRequired: Boolean = false,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    val dateTimeWrapper = remember(dateTime) {
        DateTimeWrapper.fromInstant(dateTime ?: Clock.System.now())
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = if (isRequired) "$label${Constants.REQUIRED_SUFFIX}" else label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(Constants.SMALL_PADDING.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = Constants.SURFACE_VARIANT_MEDIUM_ALPHA),
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = isEnabled) { showDatePicker = true }
                    .padding(Constants.LARGE_PADDING.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (dateTime == null) {
                    Text(
                        text = Constants.SELECT_DATE_TIME,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = DateTimeFormatter.formatDateTime(dateTime),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row {
                    IconButton(
                        onClick = { showDatePicker = true },
                        enabled = isEnabled
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date"
                        )
                    }

                    IconButton(
                        onClick = { showTimePicker = true },
                        enabled = isEnabled
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Select Time"
                        )
                    }
                }
            }
        }

        if (dateTime != null) {
            TextButton(
                onClick = { onDateTimeSelected(null) },
                contentPadding = PaddingValues(horizontal = Constants.MEDIUM_PADDING.dp),
                enabled = isEnabled
            ) {
                Text(Constants.CLEAR)
            }
        }
    }

    if (showDatePicker) {
        dateTimeWrapper.showDatePicker { selectedDate ->
            if (selectedDate != null) {
                val newDateTime = dateTimeWrapper.combineDateTime(
                    date = selectedDate,
                    time = dateTime?.let { DateTimeWrapper.fromInstant(it).getTime() }
                        ?: DateTimeWrapper.fromInstant(Clock.System.now()).getTime()
                )
                onDateTimeSelected(newDateTime)
            }
            showDatePicker = false
        }
    }

    if (showTimePicker) {
        dateTimeWrapper.showTimePicker { selectedTime ->
            if (selectedTime != null) {
                val newDateTime = dateTimeWrapper.combineDateTime(
                    date = dateTime?.let { DateTimeWrapper.fromInstant(it).getDate() }
                        ?: DateTimeWrapper.fromInstant(Clock.System.now()).getDate(),
                    time = selectedTime
                )
                onDateTimeSelected(newDateTime)
            }
            showTimePicker = false
        }
    }
}
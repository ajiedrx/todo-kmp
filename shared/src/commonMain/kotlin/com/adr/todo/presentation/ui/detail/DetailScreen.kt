package com.adr.todo.presentation.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adr.todo.presentation.ui.components.DateTimePicker
import com.adr.todo.presentation.ui.components.ReminderOptionPicker
import com.adr.todo.util.Constants
import com.adr.todo.util.DateTimeFormatter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateBackAfterDelete: () -> Unit,
) {
    val detailScreenState by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val isCreateMode = detailScreenState.isCreateMode
    val isEditMode = detailScreenState.isEditMode
    val isViewMode = !isCreateMode && !isEditMode
    val isLoading = detailScreenState.isLoading
    val error = detailScreenState.error

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(Constants.DELETE_CONFIRMATION_TITLE) },
            text = { Text(Constants.DELETE_CONFIRMATION_MESSAGE) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        detailScreenState.todo?.let {
                            viewModel.onDeleteTodo(it)
                            scope.launch {
                                snackbarHostState.showSnackbar(Constants.TASK_DELETED)
                                onNavigateBackAfterDelete()
                            }
                        }
                    }
                ) {
                    Text(Constants.DELETE_TASK)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(Constants.CANCEL)
                }
            }
        )
    }

    var showDiscardConfirmation by remember { mutableStateOf(false) }

    if (showDiscardConfirmation) {
        AlertDialog(
            onDismissRequest = { showDiscardConfirmation = false },
            title = { Text(Constants.DISCARD_CONFIRMATION_TITLE) },
            text = { Text(Constants.DISCARD_CONFIRMATION_MESSAGE) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDiscardConfirmation = false
                        viewModel.cancelEdit()
                        onNavigateBack()
                    }
                ) {
                    Text(Constants.DISCARD)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardConfirmation = false }) {
                    Text(Constants.KEEP_EDITING)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when {
                            isCreateMode -> Constants.CREATE_MODE_TITLE
                            isEditMode -> Constants.EDIT_MODE_TITLE
                            else -> Constants.VIEW_MODE_TITLE
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isEditMode && detailScreenState.hasChanged()) {
                            showDiscardConfirmation = true
                        } else if (isEditMode) {
                            viewModel.cancelEdit()
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = Constants.BACK
                        )
                    }
                },
                actions = {
                    if (isViewMode) {
                        IconButton(onClick = { viewModel.startEditMode() }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = Constants.EDIT
                            )
                        }

                        IconButton(
                            onClick = { showDeleteConfirmation = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = Constants.DELETE_TASK
                            )
                        }
                    }

                    if (isEditMode || isCreateMode) {
                        IconButton(
                            onClick = { viewModel.saveTodo() },
                            enabled = !isLoading
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = Constants.SAVE
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Constants.LARGE_PADDING.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(Constants.LARGE_PADDING.dp)
            ) {
                Spacer(modifier = Modifier.height(Constants.MEDIUM_PADDING.dp))

                AnimatedVisibility(visible = error != null) {
                    error?.let {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Constants.LARGE_PADDING.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(Constants.MEDIUM_PADDING.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = Constants.ERROR_TEXT,
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )

                                    Text(
                                        text = it,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }

                                IconButton(onClick = { viewModel.dismissError() }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = Constants.DISMISS_TEXT,
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }
                }

                if (isViewMode) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = detailScreenState.isCompleted,
                            onCheckedChange = { isChecked ->
                                detailScreenState.todo?.let {
                                    viewModel.onCompleteTodo(it.id, isChecked)
                                }
                            }
                        )

                        Text(
                            text = if (detailScreenState.isCompleted) Constants.COMPLETED else Constants.MARK_COMPLETED,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    HorizontalDivider()
                }

                if (isEditMode || isCreateMode) {
                    OutlinedTextField(
                        value = detailScreenState.title,
                        onValueChange = { viewModel.updateTitle(it) },
                        label = { Text("${Constants.TITLE_LABEL}${Constants.REQUIRED_SUFFIX}") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = detailScreenState.titleError != null,
                        supportingText = detailScreenState.titleError?.let { { Text(it) } },
                        enabled = !isLoading
                    )
                } else {
                    Column {
                        Text(
                            text = Constants.TITLE_LABEL,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = detailScreenState.title,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = Constants.MEDIUM_PADDING.dp)
                        )
                    }
                }

                if (isEditMode || isCreateMode) {
                    OutlinedTextField(
                        value = detailScreenState.description,
                        onValueChange = { viewModel.updateDescription(it) },
                        label = { Text(Constants.DESCRIPTION_LABEL) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Constants.TEXT_FIELD_HEIGHT.dp),
                        maxLines = 5,
                        enabled = !isLoading
                    )
                } else if (detailScreenState.description.isNotEmpty()) {
                    Column {
                        Text(
                            text = Constants.DESCRIPTION_LABEL,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = detailScreenState.description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = Constants.MEDIUM_PADDING.dp)
                        )
                    }
                }

                if (isEditMode || isCreateMode) {
                    DateTimePicker(
                        dateTime = detailScreenState.dueDateTime,
                        onDateTimeSelected = { viewModel.updateDueDateTime(it) },
                        label = Constants.DUE_DATE_LABEL,
                        isRequired = true,
                        modifier = Modifier.fillMaxWidth(),
                        isEnabled = !isLoading
                    )

                    if (detailScreenState.dueDateTimeError != null) {
                        Text(
                            text = detailScreenState.dueDateTimeError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                } else {
                    detailScreenState.dueDateTime?.let {
                        Column {
                            Text(
                                text = Constants.DUE_DATE_LABEL,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = DateTimeFormatter.formatDateTime(it),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = Constants.MEDIUM_PADDING.dp)
                            )
                        }
                    }
                }

                if (isEditMode || isCreateMode) {
                    ReminderOptionPicker(
                        dueDateTime = detailScreenState.dueDateTime,
                        currentReminderDateTime = detailScreenState.reminderDateTime,
                        onReminderDateTimeSelected = { viewModel.updateReminderDateTime(it) },
                        modifier = Modifier.fillMaxWidth(),
                        isEnabled = !isLoading
                    )

                    if (detailScreenState.reminderDateTimeError != null) {
                        Text(
                            text = detailScreenState.reminderDateTimeError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                } else {
                    detailScreenState.reminderDateTime?.let {
                        Column {
                            Text(
                                text = Constants.REMINDER_LABEL,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = DateTimeFormatter.formatDateTime(it),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = Constants.MEDIUM_PADDING.dp)
                            )
                        }
                    }
                }

                detailScreenState.todo?.let { todo ->
                    Spacer(modifier = Modifier.height(Constants.MEDIUM_PADDING.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(Constants.MEDIUM_PADDING.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = Constants.CREATED,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = DateTimeFormatter.formatDateTime(todo.createdAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = Constants.LAST_UPDATED,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = DateTimeFormatter.formatDateTime(todo.updatedAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (isEditMode || isCreateMode) {
                    Spacer(modifier = Modifier.height(Constants.LARGE_PADDING.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Constants.MEDIUM_PADDING.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (detailScreenState.hasChanged()) {
                                    showDiscardConfirmation = true
                                } else {
                                    viewModel.cancelEdit()
                                    if (isCreateMode) {
                                        onNavigateBack()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        ) {
                            Text(Constants.CANCEL)
                        }

                        Button(
                            onClick = { viewModel.saveTodo() },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(Constants.ICON_SIZE.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(Constants.SAVE)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Constants.XLARGE_PADDING.dp))
            }

            if (isLoading) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = Constants.SURFACE_ALPHA),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
package com.adr.todo.presentation.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adr.todo.domain.model.Todo
import com.adr.todo.presentation.ui.components.TodoListItem
import com.adr.todo.util.Constants
import com.adr.todo.util.DateTimeFormatter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToDetail: (Todo) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToHistory: () -> Unit,
) {
    val screenState by viewModel.state.collectAsState()
    val todayTodos = screenState.todayTodos
    val laterTodos = screenState.laterTodos
    val isLoading = screenState.isLoading
    val error = screenState.error

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(Constants.TASKS_TITLE) },
                actions = {
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = Constants.COMPLETED_TASKS
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = Constants.CREATE_TASK,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Constants.LARGE_PADDING.dp),
                verticalArrangement = Arrangement.spacedBy(Constants.MEDIUM_PADDING.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(Constants.LARGE_PADDING.dp))
                    Text(
                        text = DateTimeFormatter.getGreeting(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(Constants.XLARGE_PADDING.dp))
                }

                item {
                    Text(
                        text = Constants.TODAY_SECTION,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(Constants.MEDIUM_PADDING.dp))
                }

                if (todayTodos.isEmpty() && !isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Constants.MIN_HEIGHT_BOX.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = Constants.NO_TASKS_TODAY,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else if (!isLoading) {
                    items(todayTodos) { todo ->
                        TodoListItem(
                            todo = todo,
                            onClick = { onNavigateToDetail(todo) },
                            onDelete = {
                                viewModel.onDeleteTodo(todo)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = Constants.TASK_DELETED,
                                        actionLabel = Constants.UNDO,
                                        duration = SnackbarDuration.Short
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        val undoSuccessful = viewModel.undoDeleteTodo()
                                        if (undoSuccessful) {
                                            snackbarHostState.showSnackbar(Constants.TASK_RESTORED)
                                        }
                                    }
                                }
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(Constants.XLARGE_PADDING.dp))
                    Text(
                        text = Constants.LATER_SECTION,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(Constants.MEDIUM_PADDING.dp))
                }

                if (laterTodos.isEmpty() && !isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Constants.MIN_HEIGHT_BOX.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = Constants.NO_UPCOMING_TASKS,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else if (!isLoading) {
                    items(laterTodos) { todo ->
                        TodoListItem(
                            todo = todo,
                            onClick = { onNavigateToDetail(todo) },
                            onDelete = {
                                viewModel.onDeleteTodo(todo)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = Constants.TASK_DELETED,
                                        actionLabel = Constants.UNDO,
                                        duration = SnackbarDuration.Short
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        val undoSuccessful = viewModel.undoDeleteTodo()
                                        if (undoSuccessful) {
                                            snackbarHostState.showSnackbar(Constants.TASK_RESTORED)
                                        }
                                    }
                                }
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(Constants.FAB_SPACING.dp))
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(Constants.LOADING_INDICATOR_SIZE.dp)
                        .align(Alignment.Center)
                )
            }

            AnimatedVisibility(
                visible = error != null,
                enter = fadeIn() + slideInVertically { -it },
                exit = fadeOut() + slideOutVertically { -it },
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                error?.let {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        tonalElevation = 4.dp,
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
        }
    }
}
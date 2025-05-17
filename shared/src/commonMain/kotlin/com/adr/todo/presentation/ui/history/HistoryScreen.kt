package com.adr.todo.presentation.ui.history

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adr.todo.domain.model.Todo
import com.adr.todo.util.Constants
import com.adr.todo.util.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCreate: (Todo) -> Unit,
) {
    val screenState by viewModel.state.collectAsState()
    val groupedTodos = screenState.groupedTodos
    val isLoading = screenState.isLoading
    val error = screenState.error

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Constants.COMPLETED_TASKS) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = Constants.BACK
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(Constants.LOADING_INDICATOR_SIZE.dp)
                        .align(Alignment.Center)
                )
            } else if (groupedTodos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = Constants.NO_COMPLETED_TASKS,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Constants.LARGE_PADDING.dp),
                    verticalArrangement = Arrangement.spacedBy(Constants.MEDIUM_PADDING.dp)
                ) {
                    groupedTodos.forEach { (group, todos) ->
                        item {
                            Spacer(modifier = Modifier.height(Constants.LARGE_PADDING.dp))
                            Text(
                                text = group,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(Constants.MEDIUM_PADDING.dp))
                        }

                        items(todos) { todo ->
                            HistoryTaskItem(
                                todo = todo,
                                onRestore = { onNavigateToCreate(todo) }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(Constants.XLARGE_PADDING.dp))
                    }
                }
            }

            error?.let { errorMessage ->
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
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
                                text = errorMessage,
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

@Composable
fun HistoryTaskItem(
    todo: Todo,
    onRestore: (Todo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Constants.CARD_VERTICAL_PADDING.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = Constants.SURFACE_VARIANT_HIGH_ALPHA)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Constants.LARGE_PADDING.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = Constants.TEXT_HIGH_ALPHA)
                )

                if (todo.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(Constants.SMALL_PADDING.dp))
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = Constants.TEXT_HIGH_ALPHA)
                    )
                }

                todo.dueDateTime?.let {
                    Spacer(modifier = Modifier.height(Constants.SMALL_PADDING.dp))
                    Text(
                        text = "${Constants.DUE_TEXT}${DateTimeFormatter.formatDateTime(it)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = Constants.TEXT_HIGH_ALPHA)
                    )
                }
            }

            IconButton(
                onClick = { onRestore(todo) }
            ) {
                Icon(
                    imageVector = Icons.Default.RestoreFromTrash,
                    contentDescription = Constants.RESTORE,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
package com.adr.todo.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults.suggestionChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adr.todo.domain.model.Todo
import com.adr.todo.util.Constants
import com.adr.todo.util.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoListItem(
    todo: Todo,
    onClick: (Todo) -> Unit,
    onDelete: (Todo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                onDelete(todo)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> MaterialTheme.colorScheme.background
                    DismissValue.DismissedToStart -> Color.Red.copy(alpha = Constants.ERROR_ALPHA)
                    else -> MaterialTheme.colorScheme.background
                }
            )

            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            val alignment = Alignment.CenterEnd

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = Constants.LARGE_PADDING.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    modifier = Modifier.scale(scale),
                    imageVector = Icons.Default.Delete,
                    contentDescription = Constants.DELETE_TASK,
                    tint = Color.White
                )
            }
        },
        dismissContent = {
            TodoCard(todo, onClick, modifier)
        },
        directions = setOf(DismissDirection.EndToStart)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoCard(
    todo: Todo,
    onClick: (Todo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(todo) }
            .padding(vertical = Constants.CARD_VERTICAL_PADDING.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Constants.CARD_ELEVATION.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.isCompleted) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = Constants.SURFACE_VARIANT_HIGH_ALPHA)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(Constants.LARGE_PADDING.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
                    color = if (todo.isCompleted)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = Constants.TEXT_MEDIUM_ALPHA)
                    else
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                todo.dueDateTime?.let {
                    Spacer(modifier = Modifier.width(Constants.MEDIUM_PADDING.dp))
                    Chip(
                        onClick = { },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        content = {
                            Text(
                                text = DateTimeFormatter.formatDate(it),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }

            if (todo.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Constants.SMALL_PADDING.dp))
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            todo.reminderDateTime?.let {
                Spacer(modifier = Modifier.height(Constants.MEDIUM_PADDING.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SuggestionChip(
                        onClick = { },
                        colors = suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        label = {
                            Text(
                                text = "${Constants.REMINDER_TEXT}${
                                    DateTimeFormatter.formatDateTime(
                                        it
                                    )
                                }",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        }
    }
}
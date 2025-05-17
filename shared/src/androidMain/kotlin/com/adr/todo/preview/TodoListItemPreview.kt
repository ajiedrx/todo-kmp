package com.adr.todo.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adr.todo.domain.model.Todo
import com.adr.todo.presentation.ui.components.TodoListItem
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Composable
@Preview(showBackground = true)
fun TodoListItemPreview() {
    val sampleTodo = Todo(
        id = 1,
        title = "Complete project proposal",
        description = "Finish the draft and send it to the team for review before the meeting.",
        dueDateTime = Clock.System.now().plus(48, DateTimeUnit.HOUR),
        isCompleted = false,
        reminderDateTime = Clock.System.now().plus(24, DateTimeUnit.HOUR),
        createdAt = Clock.System.now().minus(24, DateTimeUnit.HOUR),
        updatedAt = Clock.System.now()
    )

    MaterialTheme {
        Surface {
            TodoListItem(
                todo = sampleTodo,
                onClick = {},
                onDelete = {},
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CompletedTodoListItemPreview() {
    val sampleTodo = Todo(
        id = 2,
        title = "Review pull request",
        description = "Check code changes and provide feedback",
        isCompleted = true,
        createdAt = Clock.System.now().minus(48, DateTimeUnit.HOUR),
        updatedAt = Clock.System.now().minus(1, DateTimeUnit.HOUR)
    )

    MaterialTheme {
        Surface {
            TodoListItem(
                todo = sampleTodo,
                onClick = {},
                onDelete = {},
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
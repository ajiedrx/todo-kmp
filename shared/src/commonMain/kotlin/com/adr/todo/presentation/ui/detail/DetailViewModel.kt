package com.adr.todo.presentation.ui.detail

import com.adr.todo.base.BaseViewModel
import com.adr.todo.domain.model.Todo
import com.adr.todo.domain.usecase.TodoUseCases
import com.adr.todo.service.NotificationService
import com.adr.todo.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class DetailViewModel(
    private val todoUseCases: TodoUseCases,
    private val notificationService: NotificationService,
) : BaseViewModel() {

    private val _state = MutableStateFlow(DetailScreenState())
    val state: StateFlow<DetailScreenState> = _state.asStateFlow()

    fun loadTodo(todoId: Long) {
        launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val loadedTodo = todoUseCases.getTodoDetail.execute(todoId)
                if (loadedTodo != null) {
                    _state.update {
                        DetailScreenState(
                            todo = loadedTodo,
                            title = loadedTodo.title,
                            description = loadedTodo.description,
                            dueDateTime = loadedTodo.dueDateTime,
                            reminderDateTime = loadedTodo.reminderDateTime,
                            isCompleted = loadedTodo.isCompleted,
                            isEditMode = false,
                            isCreateMode = false,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = Constants.TASK_NOT_FOUND
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = Constants.ERROR_LOADING_DETAILS + (e.message ?: e.toString())
                    )
                }
            }
        }
    }

    fun onCompleteTodo(todoId: Long, isCompleted: Boolean) {
        launch {
            try {
                val currentTodo = todoUseCases.getTodoDetail.execute(todoId) ?: return@launch
                val updatedTodo = currentTodo.copy(
                    isCompleted = isCompleted,
                    updatedAt = Clock.System.now()
                )
                todoUseCases.saveTodo.execute(updatedTodo)

                if (isCompleted) {
                    notificationService.cancelNotification(todoId)
                } else if (updatedTodo.reminderDateTime != null &&
                    updatedTodo.reminderDateTime > Clock.System.now()
                ) {
                    notificationService.scheduleNotification(updatedTodo)
                }

                _state.update {
                    it.copy(
                        todo = updatedTodo,
                        isCompleted = isCompleted
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = Constants.ERROR_UPDATING_TASK + (e.message ?: e.toString())
                    )
                }
            }
        }
    }

    fun onDeleteTodo(todo: Todo) {
        launch {
            try {
                todoUseCases.deleteTodo.execute(todo.id)
                notificationService.cancelNotification(todo.id)
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = Constants.ERROR_DELETING_TASK + (e.message ?: e.toString())
                    )
                }
            }
        }
    }

    fun startCreateMode() {
        _state.update {
            DetailScreenState(
                todo = null,
                title = Constants.EMPTY_STRING,
                description = Constants.EMPTY_STRING,
                dueDateTime = null,
                reminderDateTime = null,
                isCompleted = false,
                isEditMode = true,
                isCreateMode = true
            )
        }
    }

    fun startEditMode() {
        _state.update { it.copy(isEditMode = true) }
    }

    fun updateTitle(newTitle: String) {
        _state.update {
            it.copy(
                title = newTitle,
                titleError = if (newTitle.isBlank()) Constants.TITLE_REQUIRED else null
            )
        }
    }

    fun updateDescription(newDescription: String) {
        _state.update { it.copy(description = newDescription) }
    }

    fun updateDueDateTime(newDueDateTime: Instant?) {
        val currentState = _state.value

        val updatedReminderDateTime = if (newDueDateTime != null &&
            currentState.reminderDateTime != null &&
            currentState.reminderDateTime > newDueDateTime
        ) {
            null
        } else {
            currentState.reminderDateTime
        }

        _state.update {
            it.copy(
                dueDateTime = newDueDateTime,
                reminderDateTime = updatedReminderDateTime,
                dueDateTimeError = if (newDueDateTime == null) Constants.DUE_DATE_REQUIRED else null
            )
        }
    }

    fun updateReminderDateTime(newReminderDateTime: Instant?) {
        val currentState = _state.value

        val reminderTimeError = if (newReminderDateTime != null &&
            currentState.dueDateTime != null &&
            newReminderDateTime > currentState.dueDateTime
        ) {
            Constants.REMINDER_BEFORE_DUE
        } else if (newReminderDateTime != null && newReminderDateTime < Clock.System.now()) {
            Constants.REMINDER_IN_FUTURE
        } else {
            null
        }

        _state.update {
            it.copy(
                reminderDateTime = if (reminderTimeError != null) null else newReminderDateTime,
                reminderDateTimeError = reminderTimeError
            )
        }
    }

    fun updateIsCompleted(newIsCompleted: Boolean) {
        _state.update { it.copy(isCompleted = newIsCompleted) }
    }

    fun saveTodo() {
        launch {
            try {
                val currentState = _state.value

                var hasError = false

                if (currentState.title.isBlank()) {
                    _state.update { it.copy(titleError = Constants.TITLE_REQUIRED) }
                    hasError = true
                }

                if (currentState.dueDateTime == null) {
                    _state.update { it.copy(dueDateTimeError = Constants.DUE_DATE_REQUIRED) }
                    hasError = true
                }

                if (currentState.reminderDateTime != null) {
                    if (currentState.dueDateTime != null &&
                        currentState.reminderDateTime > currentState.dueDateTime
                    ) {
                        _state.update {
                            it.copy(reminderDateTimeError = Constants.REMINDER_BEFORE_DUE)
                        }
                        hasError = true
                    } else if (currentState.reminderDateTime < Clock.System.now()) {
                        _state.update {
                            it.copy(reminderDateTimeError = Constants.REMINDER_IN_FUTURE)
                        }
                        hasError = true
                    }
                }

                if (hasError) return@launch

                _state.update {
                    it.copy(
                        titleError = null,
                        dueDateTimeError = null,
                        reminderDateTimeError = null,
                        isLoading = true
                    )
                }

                val now = Clock.System.now()
                val existingTodo = currentState.todo

                val todoToSave = if (existingTodo != null && !currentState.isCreateMode) {
                    existingTodo.copy(
                        title = currentState.title,
                        description = currentState.description,
                        dueDateTime = currentState.dueDateTime,
                        isCompleted = currentState.isCompleted,
                        reminderDateTime = currentState.reminderDateTime,
                        updatedAt = now
                    )
                } else {
                    Todo(
                        title = currentState.title,
                        description = currentState.description,
                        dueDateTime = currentState.dueDateTime,
                        isCompleted = currentState.isCompleted,
                        reminderDateTime = currentState.reminderDateTime,
                        createdAt = now,
                        updatedAt = now
                    )
                }

                withContext(Dispatchers.IO) {
                    todoUseCases.saveTodo.execute(todoToSave)

                    if (todoToSave.isCompleted) {
                        notificationService.cancelNotification(todoToSave.id)
                    } else if (todoToSave.reminderDateTime != null &&
                        todoToSave.reminderDateTime > Clock.System.now()
                    ) {
                        notificationService.scheduleNotification(todoToSave)
                    } else {
                        notificationService.cancelNotification(todoToSave.id)
                    }
                }

                _state.update {
                    DetailScreenState(
                        todo = todoToSave,
                        title = todoToSave.title,
                        description = todoToSave.description,
                        dueDateTime = todoToSave.dueDateTime,
                        reminderDateTime = todoToSave.reminderDateTime,
                        isCompleted = todoToSave.isCompleted,
                        isEditMode = false,
                        isCreateMode = false,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = Constants.ERROR_SAVING_TASK + (e.message ?: e.toString())
                    )
                }
            }
        }
    }

    fun cancelEdit() {
        val currentTodo = _state.value.todo
        if (currentTodo != null) {
            _state.update {
                it.copy(
                    title = currentTodo.title,
                    description = currentTodo.description,
                    dueDateTime = currentTodo.dueDateTime,
                    reminderDateTime = currentTodo.reminderDateTime,
                    isCompleted = currentTodo.isCompleted,
                    isEditMode = false,
                    titleError = null,
                    dueDateTimeError = null,
                    reminderDateTimeError = null,
                    error = null
                )
            }
        } else {
            _state.update {
                DetailScreenState()
            }
        }
    }

    fun restoreFromHistory(todo: Todo) {
        startCreateMode()
        _state.update {
            it.copy(
                title = todo.title,
                description = todo.description,
                isCreateMode = true
            )
        }
    }

    fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    data class DetailScreenState(
        val todo: Todo? = null,
        val title: String = Constants.EMPTY_STRING,
        val description: String = Constants.EMPTY_STRING,
        val dueDateTime: Instant? = null,
        val reminderDateTime: Instant? = null,
        val isCompleted: Boolean = false,
        val isEditMode: Boolean = false,
        val isCreateMode: Boolean = false,
        val titleError: String? = null,
        val dueDateTimeError: String? = null,
        val reminderDateTimeError: String? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
    )
}

fun DetailViewModel.DetailScreenState.hasChanged(): Boolean {
    val originalTodo = todo ?: return title.isNotBlank() || description.isNotBlank() ||
            dueDateTime != null || reminderDateTime != null

    return title != originalTodo.title ||
            description != originalTodo.description ||
            dueDateTime != originalTodo.dueDateTime ||
            reminderDateTime != originalTodo.reminderDateTime ||
            isCompleted != originalTodo.isCompleted
}
package com.adr.todo.presentation.ui.main

import com.adr.todo.base.BaseViewModel
import com.adr.todo.domain.model.Todo
import com.adr.todo.domain.usecase.TodoUseCases
import com.adr.todo.service.NotificationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

data class MainScreenState(
    val todayTodos: List<Todo> = emptyList(),
    val laterTodos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class MainViewModel(
    private val todoUseCases: TodoUseCases,
    private val notificationService: NotificationService,
) : BaseViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    private var recentlyDeletedTodo: Todo? = null

    init {
        loadTodos()
        checkReminders()
    }

    private fun loadTodos() {
        launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                todoUseCases.getTodayTodos.execute().collectLatest { todayTodos ->
                    _state.update {
                        it.copy(
                            todayTodos = todayTodos,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "${ERROR_LOADING_TODAY_TASKS}${e.message}"
                    )
                }
            }
        }

        launch {
            try {
                todoUseCases.getLaterTodos.execute().collectLatest { laterTodos ->
                    _state.update { it.copy(laterTodos = laterTodos) }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "${ERROR_LOADING_UPCOMING_TASKS}${e.message}"
                    )
                }
            }
        }
    }

    private fun checkReminders() {
        launch {
            try {
                todoUseCases.checkReminders.execute()
            } catch (e: Exception) {
                println("${ERROR_CHECKING_REMINDERS}${e.message}")
            }
        }
    }

    fun onDeleteTodo(todo: Todo) {
        launch {
            try {
                todoUseCases.deleteTodo.execute(todo.id)
                notificationService.cancelNotification(todo.id)
                recentlyDeletedTodo = todo
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "${ERROR_DELETING_TASK}${e.message}"
                    )
                }
            }
        }
    }

    fun undoDeleteTodo(): Boolean {
        val todoToRestore = recentlyDeletedTodo ?: return false

        launch {
            try {
                todoUseCases.saveTodo.execute(todoToRestore)

                if (!todoToRestore.isCompleted &&
                    todoToRestore.reminderDateTime != null &&
                    todoToRestore.reminderDateTime > Clock.System.now()
                ) {
                    notificationService.scheduleNotification(todoToRestore)
                }

                recentlyDeletedTodo = null
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "${ERROR_RESTORING_TASK}${e.message}"
                    )
                }
            }
        }

        return true
    }

    fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    companion object {
        private const val ERROR_LOADING_TODAY_TASKS = "Error loading today's tasks: "
        private const val ERROR_LOADING_UPCOMING_TASKS = "Error loading upcoming tasks: "
        private const val ERROR_CHECKING_REMINDERS = "Error checking reminders: "
        private const val ERROR_DELETING_TASK = "Error deleting task: "
        private const val ERROR_RESTORING_TASK = "Error restoring task: "
    }
}
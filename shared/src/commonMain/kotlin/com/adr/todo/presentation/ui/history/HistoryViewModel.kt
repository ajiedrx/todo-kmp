package com.adr.todo.presentation.ui.history

import com.adr.todo.base.BaseViewModel
import com.adr.todo.domain.model.Todo
import com.adr.todo.domain.usecase.TodoUseCases
import com.adr.todo.util.Constants
import com.adr.todo.util.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val todoUseCases: TodoUseCases,
) : BaseViewModel() {

    private val _state = MutableStateFlow(HistoryScreenState())
    val state: StateFlow<HistoryScreenState> = _state.asStateFlow()

    init {
        loadCompletedTodos()
    }

    private fun loadCompletedTodos() {
        launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                todoUseCases.getCompletedTodos.execute().collectLatest { completedTodos ->
                    val groupedTodos = completedTodos.groupBy { todo ->
                        DateTimeFormatter.getGroupDate(todo.updatedAt)
                    }
                    _state.update {
                        it.copy(
                            completedTodos = completedTodos,
                            groupedTodos = groupedTodos,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = Constants.ERROR_LOADING_COMPLETED + (e.message ?: e.toString())
                    )
                }
            }
        }
    }

    fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    data class HistoryScreenState(
        val completedTodos: List<Todo> = emptyList(),
        val groupedTodos: Map<String, List<Todo>> = emptyMap(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )
}
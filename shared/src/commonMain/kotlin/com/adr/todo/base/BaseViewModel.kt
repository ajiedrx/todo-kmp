package com.adr.todo.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * Base ViewModel providing common functionality
 */
abstract class BaseViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    /**
     * Clean up resources when ViewModel is no longer used
     */
    fun cleanup() {
        job.cancel()
    }
}
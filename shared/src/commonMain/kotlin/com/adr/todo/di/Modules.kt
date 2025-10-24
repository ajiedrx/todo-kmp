package com.adr.todo.di

import com.adr.todo.data.db.getRoomDatabase
import com.adr.todo.data.db.getTodoDao
import com.adr.todo.data.repository.TodoRepository
import com.adr.todo.data.repository.TodoRepositoryImpl
import com.adr.todo.domain.usecase.TodoUseCases
import com.adr.todo.presentation.ui.detail.DetailViewModel
import com.adr.todo.presentation.ui.history.HistoryViewModel
import com.adr.todo.presentation.ui.main.MainViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val diModules = module {
    single { getRoomDatabase(get()) }
    single { getTodoDao(get()) }

    single<TodoRepository> { TodoRepositoryImpl(get()) }
    single { TodoUseCases(get()) }

    single { MainViewModel(get(), get()) }
    single { DetailViewModel(get(), get()) }
    single { HistoryViewModel(get()) }

    includes(getPlatformSpecificModule())
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(diModules)
    }
}

expect fun getPlatformSpecificModule(): org.koin.core.module.Module